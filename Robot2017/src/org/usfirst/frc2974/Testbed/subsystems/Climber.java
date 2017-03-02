package org.usfirst.frc2974.Testbed.subsystems;

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

	private static final double CLIMBER_HOLD = .1;

	private Talon climberMotor;

	public Climber() {
		climberMotor = RobotMap.climber;
		SmartDashboard.putNumber("ClimberHold", CLIMBER_HOLD);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new Climb());
	}

	public void hold() {
		// climberMotor.set(CLIMBER_HOLD);
		climberMotor.set(SmartDashboard.getNumber("ClimberHold", CLIMBER_HOLD));
	}

	public void set(double power) {
		climberMotor.set(power);
	}
}
