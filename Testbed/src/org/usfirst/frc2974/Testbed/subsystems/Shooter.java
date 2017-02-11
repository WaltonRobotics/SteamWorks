package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem{
	
	private com.ctre.talonsrx.CANTalon flywheelMotor;
	private DigitalOutput indexer;
	
	private final double SPEED = 6000; //rpm
	
	public Shooter() {
		flywheelMotor = RobotMap.flywheelMotor;
	}

	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(Robot.aim);
	}
	
	public void enable() {
		flywheelMotor.set(SPEED);
	}
	
	public void disable() {
		flywheelMotor.set(0);
	}	
	
	public boolean isAtSpeed() {
		return (60*102.4*flywheelMotor.getSpeed()) >= SPEED;
		//60 sec/min * 10 ticks/sec * 1 rev/1024 ticks = rpm
	}
	
	public void index() {
		indexer.set(true);
	}

}