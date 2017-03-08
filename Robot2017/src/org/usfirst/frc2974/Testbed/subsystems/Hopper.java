package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.HopperControl;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hopper extends Subsystem {

	private Solenoid piston;

	public Hopper() {
		piston = RobotMap.hopperRelease;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new HopperControl());
	}

	public void setPistonDown(boolean down) {
		piston.set(down);
	}
}
