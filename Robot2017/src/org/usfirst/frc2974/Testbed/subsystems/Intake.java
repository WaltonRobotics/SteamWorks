package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.IntakeCommand;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Intake extends Subsystem {

	public static final double INTAKE_STOP = 0;
	public static final double INTAKE_IN = 0.7;
	public static final double INTAKE_OUT = -0.7;
	public static final double INTAKE_DUMP = -.8;

	private Talon intakeTalon;

	public Intake() {
		intakeTalon = RobotMap.intake;
		SmartDashboard.putBoolean("IsCompBot", true);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new IntakeCommand());
	}

	public void setIntake(double intakeSpeed) {
		if(SmartDashboard.getBoolean("IsCompBot",true)){
			intakeTalon.set(-intakeSpeed);
		}else{
			intakeTalon.set(intakeSpeed);
		}
	}
}
