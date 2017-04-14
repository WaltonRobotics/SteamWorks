package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.MoveBall;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallMover extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new MoveBall());
	}

	public void setSpeed(double power) {
		RobotMap.balls.set(power);
	}

	public void diable() {
		RobotMap.balls.set(0);
	}
}