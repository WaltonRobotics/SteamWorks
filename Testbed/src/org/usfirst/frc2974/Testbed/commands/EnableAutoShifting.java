package org.usfirst.frc2974.Testbed.commands;

import edu.wpi.first.wpilibj.command.Command;

public class EnableAutoShifting extends Command{

	private boolean isDone;
	
	@Override
	protected void initialize() {		
		
		AutoShifting.enable();
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
