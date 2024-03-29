// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc2974.Testbed;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.Talon;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static Talon left;
	public static Talon right;
	public static Talon indexer;
	
	public static CANTalon flywheelMotor;
	
	public static Encoder encoderLeft;
	public static Encoder encoderRight;
	
	public static Compressor compressor;
	public static Solenoid pneumaticsShifter;

    public static void init() {
    	left = new Talon(0);
    	right = new Talon(1);
    	
    	compressor = new Compressor();
    	pneumaticsShifter = new Solenoid(0);
    	
    	encoderRight = new Encoder(new DigitalInput(0),new DigitalInput(1));
    	encoderLeft = new Encoder(new DigitalInput(2), new DigitalInput(3));    	
    	
    	
    }
}
