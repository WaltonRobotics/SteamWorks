package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

public class HoldPower extends Command {

	private double power;
	private Drivetrain drivetrain;

	public HoldPower(double power) {
		this.power = power;
		this.drivetrain = Robot.drivetrain;

	}

	@Override
	public void initialize() {
		drivetrain.setSpeeds(power, power);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end() {
		drivetrain.setSpeeds(0, 0);
	}

	@Override
	public void interrupted() {
		end();
	}
}
