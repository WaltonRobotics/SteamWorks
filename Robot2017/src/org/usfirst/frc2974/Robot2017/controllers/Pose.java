package org.usfirst.frc2974.Robot2017.controllers;

public class Pose {
	public final RobotPair positionWheel;
	public final double x;
	public final double y;
	public final double angle;

	public Pose(double posLeft, double posRight, double x, double y, double angle) {
		positionWheel = new RobotPair(posLeft, posRight);
		this.x = x;
		this.y = y;
		this.angle = angle;

	}
}
