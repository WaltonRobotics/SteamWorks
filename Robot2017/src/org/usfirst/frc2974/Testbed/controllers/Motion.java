package org.usfirst.frc2974.Testbed.controllers;

public class Motion {
	// kK = 0.1
	// kV = 0.4

	public final RobotPair position;
	public final RobotPair velocity;
	public final RobotPair accel;
	public final boolean isDone;
	public Pose pose;
	public double time;

	public Motion(double posLeft, double velLeft, double accLeft, double posRight, double velRight,
		double accRight, boolean isDone, Pose pose_init, double dt) {

		position = new RobotPair(posLeft, posRight);
		velocity = new RobotPair(velLeft, velRight);
		accel = new RobotPair(accLeft, accRight);

		this.isDone = isDone;

		pose = pose_init;
		time = dt;

		// RobotLoggerManager.setFileHandlerInstance("robot.controllers").info("");
	}

	@Override
	public String toString() {
		return String.format("position=%s; velocity=%s; accel=%s", position, velocity, accel);
	}

}
