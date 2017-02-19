package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Index extends Command {

	double initialTime;

	public Index() {
	}

	protected void initialize() {

		Robot.shooter.index(true);

		initialTime = Timer.getFPGATimestamp();

	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Timer.getFPGATimestamp() - initialTime >= 0.2;
	}

	protected void end() {
		Robot.shooter.index(false);
	}

	protected void interrupted() {
		end();
	}

}
