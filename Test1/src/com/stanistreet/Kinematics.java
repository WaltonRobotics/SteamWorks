package com.stanistreet;

public class Kinematics {

	private Motion motion;
	private KinematicPose lastPose;
	private KinematicPose nextPose;
	
	private float s;
	private float v0;
	private float v1;
	private int nPoints;

	public Kinematics(Motion motion, float v0, float v1, int nPoints) {
		this.motion = motion;
		this.nPoints = nPoints;
		this.v0 = v0;
		this.v1 = v1;
		
		evaluateFirstPose();
		evaluateNextPose(1F / nPoints);
	}
		
	private void evaluateNextPose(float ds) {
		lastPose = nextPose;
		if (s + ds > 1.0) {
			evaluateLastPose();
			return;
		}
		
		s += ds;
		
		// Calculate next pose from defined motion
		Pose pose = motion.evaluatePose(s);

		// Estimate length and angle of rotation
		float dl = (float) lastPose.X.distance(pose.X);
		float dtheta = Motion.boundTheta(pose.theta - lastPose.theta);
		
		// Estimate lengths each wheel will turn
		float dlLeft = dl - dtheta * RobotParameters.width / 2F;
		float dlRight = dl + dtheta * RobotParameters.width / 2F;
				
		// Assuming one of the wheels will limit motion, calculate time this step will take
		float dt = Math.max(dlLeft, dlRight) / RobotParameters.vMax;
		float a = 0;  // Don't really care about acceleration if following steady motion...
		
		// Bound time steps for initial/final acceleration ...
		switch (motion.getLimitMode()) {
		case LimitLinearAcceleration:
			// ... assuming constant linear acceleration from initial/to final speeds
			float v = dl / dt;			
			float lMidpoint = lastPose.getLCenter() + 0.5F * dl;
			
			float vAccel = (float)Math.sqrt(v0 * v0 + RobotParameters.aMax * lMidpoint);
			if (vAccel < v) {
				a = RobotParameters.aMax;
				dt = dl / vAccel;
			}
			
			float vDecel = (float)Math.sqrt(v1 * v1 + RobotParameters.aMax * (motion.getLength() - lMidpoint));
			if (vDecel < v) {
				a = -RobotParameters.aMax;
				dt = dl / vDecel;
			}
			break;
			
		case LimitRotationalAcceleration:
			// ... assuming constant angular acceleration from/to zero angular speed
			float omega = Math.abs(dlRight - dlLeft) / dt / RobotParameters.width;
			float thetaMidpoint = lastPose.theta + 0.5F * dtheta;
			
			float omegaAccel = (float)Math.sqrt(RobotParameters.alphaMax * 
					Math.abs(Motion.boundTheta(thetaMidpoint - motion.getInitialTheta()))); 	
			if (omegaAccel < omega) {
				dt = Math.abs(dlRight - dlLeft) / omegaAccel / RobotParameters.width;
			}
			
			float omegaDecel = (float)Math.sqrt(RobotParameters.alphaMax * 
					Math.abs(Motion.boundTheta(thetaMidpoint - motion.getFinalTheta())));
			if (omegaDecel < omega) {
				dt = Math.abs(dlRight - dlLeft) / omegaDecel / RobotParameters.width;
			}
			break;
		}
		
		// Create new kinematic state. Old state is retained to interpolate positions, new state contains estimate 
		// for speed and acceleration
		KinematicState left = new KinematicState(lastPose.left.l + dlLeft, dlLeft / dt, a * dlLeft / dl);
		KinematicState right = new KinematicState(lastPose.right.l + dlRight, dlRight / dt, a * dlRight / dl);
		nextPose = new KinematicPose(pose, left, right, lastPose.t + dt, false);
	}
	
	private void evaluateFirstPose() {
		Pose pose = motion.evaluatePose(0);
		KinematicState left = new KinematicState(0, v0, 0);
		KinematicState right = new KinematicState(0, v0, 0);

		lastPose = nextPose = new KinematicPose(pose, left, right, 0, false);
	}
	
	private void evaluateLastPose() {
		Pose pose = motion.evaluatePose(1F);
		
		KinematicState left = new KinematicState(lastPose.left.l, v1, 0);
		KinematicState right = new KinematicState(lastPose.right.l, v1, 0);

		lastPose = nextPose = new KinematicPose(pose, left, right, lastPose.t, true);
	}
	
	public KinematicPose interpolatePose(float t) {
		
		if (t <= lastPose.t) {
			return lastPose;
		}
		
		while (t > nextPose.t && !nextPose.isFinished) {
			evaluateNextPose(1F / nPoints);
		}
		
		if (nextPose.isFinished) {
			return nextPose;
		}
		
		float dt = nextPose.t - lastPose.t;
		float p = (nextPose.t - t) / dt;
		float q = (t - lastPose.t) / dt;
		
		return KinematicPose.interpolate(lastPose, p, nextPose, q);
	}
}
