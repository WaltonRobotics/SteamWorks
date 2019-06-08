package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ReadMotionControllerConstants extends Command{

	private boolean isDone;
	
	@Override
	protected void initialize() {
		
		Robot.drivetrain.readConstants();
		
		isDone = true;
		
	}

	@Override
	protected void execute() {
		
		
	}

	@Override
	protected boolean isFinished() {
		
		return isDone;
	}

	@Override
	protected void end() {
		
		
	}

	@Override
	protected void interrupted() {
		end();
		
	}

}
