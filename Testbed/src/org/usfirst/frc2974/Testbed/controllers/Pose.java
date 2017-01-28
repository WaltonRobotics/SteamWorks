package org.usfirst.frc2974.Testbed.controllers;

public class Pose {
	public final double positionLeftWheel;
	public final double positionRightWheel;
	public final double x;
	public final double y;
	public final double angle;
	
	public Pose(double posLeft, double posRight, double x, double y, double angle){
		positionLeftWheel = posLeft;
		positionRightWheel = posRight;
		this.x = x;
		this.y = y;
		this.angle = angle;
		
	}
}
