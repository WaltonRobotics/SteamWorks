package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.Climb;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {

	public static final double CLIMBER_HOLD = .1;

	private Talon climberMotor;

	public Climber() {
		climberMotor = RobotMap.climber;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new Climb());
	}

	public void hold() {
		// climberMotor.set(CLIMBER_HOLD);
		climberMotor.set(Robot.pref.getDouble("ClimberHold", CLIMBER_HOLD));
	}

	public void set(double power) {
		climberMotor.set(power);
	}
}
