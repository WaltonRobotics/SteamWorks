package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.GearIntakeControl;
import org.usfirst.frc2974.Testbed.commands.HopperControl;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearIntake extends Subsystem {

	private Solenoid piston;

	public GearIntake() {
		piston = RobotMap.flapCylinder;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new GearIntakeControl());
	}

	public void setPiston(boolean deployed) {
		piston.set(deployed);
	}
}
