package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class HopperControl extends Command {

	public HopperControl() {
		requires(Robot.hopper);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		//if (Robot.hopper.noBallsInLeft() || Robot.oi.hopperControl.get()) {
		if (Robot.oi.hopperControl.get()) {
			Robot.hopper.setPiston(false);
		} else {
			Robot.hopper.setPiston(true);
		}
		//SmartDashboard.putBoolean("areBallsLeftInLeftAccordingToLaser", Robot.hopper.noBallsInLeft());
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
