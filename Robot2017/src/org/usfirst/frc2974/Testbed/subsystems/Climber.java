package org.usfirst.frc2974.Testbed.subsystems;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.Climb;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {

	public static final double CLIMBER_HOLD = .3;

	private Talon climberMotor;
	private double hold;

	public Climber() {
		climberMotor = RobotMap.climber;
	}
	public static void declarePrefs(boolean reset){
		Preferences pref = Preferences.getInstance();
		if(reset || !pref.containsKey("climber.hold")){
			pref.putDouble("climber.hold", CLIMBER_HOLD);
		}
	}
	public void initDefaultCommand() {
		setDefaultCommand(new Climb());
	}
	
	public void dumpSmartdashboardValues() {
		SmartDashboard.putNumber("Climber hold", hold);
		SmartDashboard.putNumber("Climber current", getCurrent());
		SmartDashboard.putNumber("Climber power", climberMotor.get()); 
		
	}
	
	public void setHold(){
		Preferences pref = Preferences.getInstance();
		hold = pref.getDouble("climber.hold", CLIMBER_HOLD);
	}
	
	public void changeHoldTo(double newHold){
		hold = newHold;
	}

	public void hold() {
		set(hold);
	}

	public void set(double power) {
		climberMotor.set(power);
	}
	
	public double getCurrent() {
		return RobotMap.powerPanel.getCurrent(RobotMap.CLIMBER_MOTOR_POWER_CHANNEL);
	}
	
	public double getHold(){
		return hold;
	}
}
