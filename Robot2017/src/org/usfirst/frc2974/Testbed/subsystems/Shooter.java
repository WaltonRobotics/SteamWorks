
package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.Shoot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	private CANTalon flywheelMotor;
	private Talon indexer;
	boolean enabled = false;

	public static final double fSPEED = -2225; // rpm
	public static final double ACCEPTED_ERROR = 50;

	public Shooter() {
		flywheelMotor = RobotMap.flywheelMotor;
		indexer = RobotMap.indexer;
		flywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		SmartDashboard.putNumber("ShootSpeed", fSPEED);
		flywheelMotor.setPID(0.3, 0, 0);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Shoot());
	}

	public void enable() {
		//flywheelMotor.setPID(SmartDashboard.getNumber("Wheel Proportional Coefficient", flywheelMotor.getP()), 0, 0);
		flywheelMotor.setPID(0.25, 0, 0);
		flywheelMotor.set(SmartDashboard.getNumber("ShootSpeed", fSPEED)*8);
		enabled = true;
	}

	public void disable() {
		flywheelMotor.set(0);
		enabled = false;
	}
	
	public boolean enabled(){
		return enabled;
	}

	public boolean isAtSpeed() {
		return Math.abs(getSpeed() - Math.abs(SmartDashboard.getNumber("ShootSpeed", 0))) < ACCEPTED_ERROR;
	}
	
	public double getSpeed(){
		return encoderToRpm(-flywheelMotor.getSpeed());
	}
	
	public double encoderToRpm(double encoder){
		return 60. * (10. / 1024.) * (encoder / 4.);
		// 60 sec/min * 10 ticks/sec * 1 rev/1024 ticks * 1 tick/4 quarterTicks= rpm
	}
	public double rpmToEncoder(double rpm){
		//System.out.println(1/encoderToRpm(rpm));
		return 1/encoderToRpm(rpm);
		//return(SmartDashboard.getNumber("ShootSpeed",fSPEED)/5000);
	}
	
	public void setPowerMode(double power)
	{
		disable();
		flywheelMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		flywheelMotor.set(power);
	}
	
	public void endPowerMode()
	{
		flywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		enable();
	}
	
	public void dumpValuesToSamrtDashboard()
	{
		SmartDashboard.putNumber("currentRPM", getSpeed());
		SmartDashboard.putBoolean("isShooterAtSpeed", isAtSpeed());
		
	}

	public void index(boolean on) {
		indexer.setSpeed(on ? -.75 : 0);
	}

}