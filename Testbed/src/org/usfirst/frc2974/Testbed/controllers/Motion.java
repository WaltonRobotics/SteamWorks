package org.usfirst.frc2974.Testbed.controllers;

public class Motion{
	public final double positionLeft;
	public final double velocityLeft;
	public final double accelLeft;
	public final double positionRight;
	public final double velocityRight;
	public final double accelRight;
	
	public Motion(double posLeft, double velLeft, double accLeft, double posRight, 
			double velRight, double accRight){
		positionLeft = posLeft;
		velocityLeft = velLeft;
		accelLeft = accLeft;
		positionRight = posRight;
		velocityRight = velRight;
		accelRight = accRight;
		
	}
}
