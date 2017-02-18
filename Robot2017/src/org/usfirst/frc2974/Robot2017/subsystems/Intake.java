package org.usfirst.frc2974.Robot2017.subsystems;

import org.usfirst.frc2974.Robot2017.RobotMap;
import org.usfirst.frc2974.Robot2017.commands.IntakeCommand;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	public static final double INTAKE_STOP = 0;
	public static final double INTAKE_IN = 1;
	public static final double INTAKE_OUT = -1;

	private Talon intakeTalon;

	public Intake() {
		intakeTalon = RobotMap.intake;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new IntakeCommand());
	}

	public void setIntake(double intakeSpeed) {
		intakeTalon.set(intakeSpeed);
	}
}
