package org.usfirst.frc2974.Robot2017.subsystems;

import org.usfirst.frc2974.Robot2017.RobotMap;
import org.usfirst.frc2974.Robot2017.commands.Climb;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {

	private static final double CLIMBER_HOLD = .1;

	private Spark climberMotor;

	public Climber() {
		climberMotor = RobotMap.climber;
	}

	public void initDefaultCommand() {
		// setDefaultCommand(new Climb());
	}

	public void hold() {
		// climberMotor.set(CLIMBER_HOLD);
		climberMotor.set(SmartDashboard.getNumber("Climber Hold", 0));
	}

	public void set(double power) {
		climberMotor.set(power);
	}
}
