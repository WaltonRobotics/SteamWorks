package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.commands.HopperControl;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hopper extends Subsystem {

    DigitalInput laser;
    DigitalOutput piston;

    public void initDefaultCommand() {
    	//setDefaultCommand(new HopperControl());
    }
    
    public boolean noBallsInLeft() {
    	return laser.get();
    }
    
    public void setPiston(boolean on) {
    	piston.set(on);
    }
}

