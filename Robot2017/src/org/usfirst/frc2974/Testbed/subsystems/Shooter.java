package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.Shoot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

	private CANTalon flywheelMotor;
	private Talon indexer;

	public static final double SPEED = -6000; // rpm
	public static final double ACCEPTED_ERROR = 200;

	public Shooter() {
		flywheelMotor = RobotMap.flywheelMotor;
		indexer = RobotMap.indexer;
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Shoot());
	}

	public void enable() {
		flywheelMotor.set(rpmToEncoder(SPEED));
	}

	public void disable() {
		flywheelMotor.set(0);
	}

	public boolean isAtSpeed() {
		return Math.abs(getSpeed() - SPEED) < ACCEPTED_ERROR;
	}
	
	public double getSpeed(){
		return encoderToRpm(-flywheelMotor.getSpeed());
	}
	
	public double encoderToRpm(double encoder){
		return 60. * (10. / 1024.) * (encoder / 4.);
		// 60 sec/min * 10 ticks/sec * 1 rev/1024 ticks * 1 tick/4 quarterTicks= rpm
	}
	public double rpmToEncoder(double rpm){
		return 1/encoderToRpm(rpm);
	}

	public void index(boolean on) {
		indexer.setSpeed(on ? -0.5 : 0);
	}

}