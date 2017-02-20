package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.Shoot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	private CANTalon flywheelMotor;
	private Talon indexer;
	boolean enabled = false;

	public static final double fSPEED = -3000; // rpm
	public static final double ACCEPTED_ERROR = 200;

	public Shooter() {
		flywheelMotor = RobotMap.flywheelMotor;
		indexer = RobotMap.indexer;
		SmartDashboard.putNumber("ShootSpeed", fSPEED);
		flywheelMotor.setPID(0.3, 0, 0);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Shoot());
	}

	public void enable() {
		flywheelMotor.setPID(SmartDashboard.getNumber("Wheel Proportional Coefficient", flywheelMotor.getP()), 0, 0);
		flywheelMotor.setSetpoint(rpmToEncoder(SmartDashboard.getNumber("ShootSpeed", 0)));
		enabled = true;
	}

	public void disable() {
		flywheelMotor.setSetpoint(0);
		enabled = false;
	}
	
	public boolean enabled(){
		return enabled;
	}

	public boolean isAtSpeed() {
		return Math.abs(getSpeed() - SmartDashboard.getNumber("ShootSpeed", 0)) < ACCEPTED_ERROR;
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