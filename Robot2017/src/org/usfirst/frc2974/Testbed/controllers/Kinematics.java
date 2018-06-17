package org.usfirst.frc2974.Testbed.controllers;

public class Kinematics {

	// The width of the robot in meters.
	public static final double robotWidth = .70485;

	private MotionProvider motion;
	private KinematicPose lastPose;
	private KinematicPose nextPose;

	private double s;
	private double v0;
	private double v1;
	private int nPoints;
	private double l0;

	public Kinematics(MotionProvider motion, RobotPair wheelPosition, double t, double v0, double v1,
		int nPoints) {
		this.motion = motion;
		this.s = 0.0;
		this.v0 = v0;
		this.v1 = v1;
		this.nPoints = nPoints;

		evaluateFirstPose(wheelPosition, t);
		evaluateNextPose(1.0 / nPoints);
	}

	public static KinematicPose staticPose(Pose pose, RobotPair wheelPosition, double t) {
		KinematicState left = new KinematicState(wheelPosition.left, 0, 0);
		KinematicState right = new KinematicState(wheelPosition.right, 0, 0);
		return new KinematicPose(pose, left, right, t, false);
	}

	private void evaluateFirstPose(RobotPair wheelPosition, double t) {
		Pose pose = motion.evaluatePose(0);
		KinematicState left = new KinematicState(wheelPosition.left, v0, 0);
		KinematicState right = new KinematicState(wheelPosition.right, v0, 0);

		lastPose = nextPose = new KinematicPose(pose, left, right, t, false);
		l0 = lastPose.getLCenter();
		System.out.println("First Pose: " + lastPose.toString());
	}

	private void evaluateLastPose() {
		Pose pose = motion.evaluatePose(1.0);
		KinematicState left = new KinematicState(lastPose.left.length, v1, 0);
		KinematicState right = new KinematicState(lastPose.right.length, v1, 0);

		lastPose = nextPose = new KinematicPose(pose, left, right, lastPose.t, true);
		System.out.println("Last Pose: " + lastPose.toString());
	}

	private void evaluateNextPose(double ds) {
		lastPose = nextPose;

		if (lastPose.isFinished) {
			evaluateLastPose();
			return;
		}

		boolean isFinished = false;
		if (s + ds > 1.0) {
			s = 1.0;
			isFinished = true;
		} else {
			s += ds;
		}

		// calculate the next pose from given motion
		Pose pose = motion.evaluatePose(s);
		double direction = Math.signum(motion.getLength());

		// estimate angle to turn s
		double dl = lastPose.X.distance(pose.X) * direction;
		double dAngle = MotionProvider.boundAngle(pose.angle - lastPose.angle);

		// estimate lengths each wheel will turn
		double dlLeft = (dl - dAngle * robotWidth / 2);
		double dlRight = (dl + dAngle * robotWidth / 2);

		// assuming one of the wheels will limit motion, calculate time this step will
		// take
		double dt = Math.max(Math.abs(dlLeft), Math.abs(dlRight)) / motion.vCruise;
		double a = 0.0; // acceleration doesn't matter if following steady motion
		// System.out.println(String.format("s=%f, dl=%f, dlLeft=%f, dlRight=%f, dt=%f,
		// direction=%f", s, dl, dlLeft, dlRight, dt, direction));
		// bound time steps for initial/final acceleration
		switch (motion.getLimitMode()) {
			case LimitLinearAcceleration:
				// assuming constant linear acceleration from initial/to final speeds
				double v = Math.abs(dl) / dt;
				double lMidpoint = lastPose.getLCenter() + .5 * dl - l0;

				double vAccel = Math.sqrt(v0 * v0 + motion.aMax * Math.abs(lMidpoint));
				double vDecel =
					Math.sqrt(v1 * v1 + motion.aMax * (Math.abs(motion.getLength() - lMidpoint)));

				if (vAccel < v && vAccel < vDecel) {
					a = motion.aMax;
					dt = Math.abs(dl) / vAccel;
				}

				if (vDecel < v && vDecel < vAccel) {
					a = -motion.aMax;
					dt = Math.abs(dl) / vDecel;
				}

				// System.out.println(String.format("v=%f, vaccel=%f, vdecel=%f", v, vAccel,
				// vDecel));
				break;
			case LimitRotationalAcceleration:
				// assuming constant angular acceleration from/to zero angular speed
				double omega = Math.abs(dlRight - dlLeft) / dt / robotWidth;
				double thetaMidpoint = lastPose.angle + .5 * dAngle;

				double omegaAccel = Math.sqrt(motion.aMax
					* Math.abs(MotionProvider.boundAngle(thetaMidpoint - motion.getInitialTheta())));
				double omegaDecel = Math.sqrt(motion.aMax
					* Math.abs(MotionProvider.boundAngle(thetaMidpoint - motion.getFinalTheta())));
				// System.out.println("OmegaAccel=" + omegaAccel);
				if (omegaAccel < omega && omegaAccel < omegaDecel) {
					dt = Math.abs(dlRight - dlLeft) / omegaAccel / robotWidth;
				}

				// System.out.println("OmegaDecel=" + omegaDecel);
				if (omegaDecel < omega && omegaDecel < omegaAccel) {
					dt = Math.abs(dlRight - dlLeft) / omegaDecel / robotWidth;
				}
				break;
		}

		// create new kinematic state. Old state is retained to interpolate positions,
		// new state contains estimate for speed and accel
		KinematicState left =
			new KinematicState(lastPose.left.length + dlLeft, dlLeft / dt, a * direction);
		KinematicState right =
			new KinematicState(lastPose.right.length + dlRight, dlRight / dt, a * direction);

		nextPose = new KinematicPose(pose, left, right, lastPose.t + dt, isFinished);
		System.out.println("Next Pose " + nextPose.toString());
	}

	// calculates the path to follow within a particular distance
	public KinematicPose interpolatePose(double t) {

		// System.out.println(String.format("Last t=%3.1f Next t=%3.1f Now t=%3.1f",
		// lastPose.t, nextPose.t, t));

		if (t <= lastPose.t) {
			return lastPose;
		}

		if (lastPose.isFinished) {
			return lastPose;
		}

		while (t > nextPose.t) {
			evaluateNextPose(1.0 / nPoints);
			if (lastPose.isFinished) {
				return lastPose;
			}
		}

		double dt = nextPose.t - lastPose.t;
		double p = (nextPose.t - t) / dt;
		double q = (t - lastPose.t) / dt;

		return KinematicPose.interpolate(lastPose, p, nextPose, q);
	}

	public Pose getPose() {
		return new Pose(nextPose.X, nextPose.angle);
	}

	public RobotPair getWheelPositions() {
		return new RobotPair(nextPose.left.length, nextPose.right.length);
	}

	public double getTime() {
		return nextPose.t;
	}

	@Override
	public String toString() {
		return String.format("Last pose = %s Next pose = %s", lastPose, nextPose);
	}
}
