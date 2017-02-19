package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.commands.IntakeCommand.State;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Climb extends Command {
	public Climb() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.climber);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.oi.hold())
			Robot.climber.hold();
		else
			Robot.climber.set(Robot.oi.climbY());

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}