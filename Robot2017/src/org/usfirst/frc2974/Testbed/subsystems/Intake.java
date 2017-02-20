package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.IntakeCommand;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	public static final double INTAKE_STOP = 0;
	public static final double INTAKE_IN = 0.55;
	public static final double INTAKE_OUT = -0.55;
	public static final double INTAKE_DUMP = -.7;

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
