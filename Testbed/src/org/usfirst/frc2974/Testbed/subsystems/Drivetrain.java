// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.*;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Drivetrain extends Subsystem {
	
//	public PIDControllerAccel leftController;
//	public PIDControllerAccel rightController;
	
	private Talon right = RobotMap.right;
	private Talon left = RobotMap.left;
	
	private Encoder encoderLeft = RobotMap.encoderLeft;
	private Encoder encoderRight = RobotMap.encoderRight;
	
	Solenoid shifter = RobotMap.pneumaticsShifter;
	
	public void setSpeeds(double leftSpeed, double rightSpeed) {
		right.set(rightSpeed);
		left.set(-leftSpeed);
	}

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public Drivetrain() {
		resetEncoders();
		
		encoderLeft.setDistancePerPulse(0.005);
		encoderRight.setDistancePerPulse(0.005);
		
		encoderLeft.setPIDSourceType(PIDSourceType.kDisplacement);
		encoderRight.setPIDSourceType(PIDSourceType.kDisplacement);
		
		
	}
	
	public void shiftUp() {
		if(!shifter.get()) {
			shifter.set(true);
		}
	}
	
	public void shiftDown() {
		if(shifter.get()) {
			shifter.set(false);
		}
	}
	
	public void dumpSmartdashboardValues() {
		SmartDashboard.putNumber("EncoderLeft", encoderLeft.getDistance());
		SmartDashboard.putNumber("EncoderRight", encoderRight.getDistance());
		
		SmartDashboard.putNumber("VelocityLeft", encoderLeft.getRate());
		SmartDashboard.putNumber("Velocity Right", encoderRight.getRate());
	}
	
	public void resetEncoders() {
		encoderLeft.reset();
		encoderRight.reset();
	}
	
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new Drive());

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND LOL CAPS

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}

