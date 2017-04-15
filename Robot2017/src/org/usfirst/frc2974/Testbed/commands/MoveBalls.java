package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveBalls extends Command {

	public MoveBalls() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.ballMover);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.ballMover.diable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		// TODO refine this and ask the drivers what they want as key
		Robot.ballMover.setSpeed(Robot.oi.gamepad.getRightY());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.ballMover.diable();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}