package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.MoveBalls;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallMover extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new MoveBalls());
	}

	public void setSpeed(double power) {
		RobotMap.ballMover.set(power);
	}

	public void diable() {
		RobotMap.ballMover.set(0);
	}
}