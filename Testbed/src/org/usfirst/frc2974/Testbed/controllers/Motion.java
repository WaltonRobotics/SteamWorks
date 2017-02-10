package org.usfirst.frc2974.Testbed.controllers;

import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;

public class Motion{
	
	public final RobotPair position;
	public final RobotPair velocity;
	public final RobotPair accel;
	public final boolean isDone;
	
	public Motion(double posLeft, double velLeft, double accLeft, double posRight, 
			double velRight, double accRight, boolean isDone){
		
		position = new RobotPair(posLeft, posRight);
		velocity = new RobotPair(velLeft, velRight);
		accel = new RobotPair(accLeft, accRight);
		
		this.isDone = isDone;
		
//		RobotLoggerManager.setFileHandlerInstance("robot.controllers").info("");
		
	}
	
	
	
}
