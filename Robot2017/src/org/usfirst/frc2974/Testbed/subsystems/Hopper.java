package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.HopperControl;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hopper extends Subsystem {

	DigitalInput laser;
	Solenoid piston;

	public Hopper() {
		laser = RobotMap.laser;
		piston = RobotMap.hopperRelease;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new HopperControl());
	}

	public boolean noBallsInLeft() {
		return laser.get();
	}

	public void setPiston(boolean on) {
		piston.set(on);
	}
}