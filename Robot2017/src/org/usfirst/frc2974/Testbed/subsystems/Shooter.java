
package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.Shoot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TrajectoryPoint;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	private CANTalon flywheelMotor;
	private Talon indexer;
	private boolean enabled = false;

	private static final double fSPEED = 14500; // rpm
	private static final double ACCEPTED_ERROR = 400;
	private static final double KP = 0.1;
	private static final double KI = 0.0;
	private static final double KD = 1.0;
	private static final double KF = 0.041;
	private static final double iPOWER = -0.6;

	private double speed = fSPEED;
	private double error = ACCEPTED_ERROR;
	private double iPower = iPOWER;

	public Shooter() {
		flywheelMotor = RobotMap.flywheelMotor;
		indexer = RobotMap.indexer;
		flywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		flywheelMotor.reverseSensor(true);
		flywheelMotor.reverseOutput(true);
		flywheelMotor.setPID(0, 0, 0);
	}

	public static void declarePrefs(boolean reset) {
		Preferences pref = Preferences.getInstance();
		if (reset || !pref.containsKey("shooter.speed")) {
			pref.putDouble("shooter.speed", fSPEED);
		}
		if (reset || !pref.containsKey("shooter.kP")) {
			pref.putDouble("shooter.kP", KP);
		}
		if (reset || !pref.containsKey("shooter.kI")) {
			pref.putDouble("shooter.kI", KI);
		}
		if (reset || !pref.containsKey("shooter.kD")) {
			pref.putDouble("shooter.kD", KD);
		}
		if (reset || !pref.containsKey("shooter.kF")) {
			pref.putDouble("shooter.kF", KF);
		}
		if (reset || !pref.containsKey("shooter.error")) {
			pref.putDouble("shooter.error", ACCEPTED_ERROR);
		}
		if (reset || !pref.containsKey("shooter.indexer.power")) {
			pref.putDouble("shooter.indexer.power", iPOWER);
		}
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Shoot());
	}

	public void enable() {
		// flywheelMotor.setPID(SmartDashboard.getNumber("Wheel Proportional
		// Coefficient", flywheelMotor.getP()), 0, 0);
		// flywheelMotor.setPID(0.25, 0, 0);
		Preferences pref = Preferences.getInstance();
		flywheelMotor.setF(pref.getDouble("shooter.kF", KF));
		flywheelMotor.setP(pref.getDouble("shooter.kP", KP));
		flywheelMotor.setI(pref.getDouble("shooter.kI", KI));
		flywheelMotor.setD(pref.getDouble("shooter.kD", KD));
		iPower = pref.getDouble("shooter.indexer.power", iPOWER);
		error = pref.getDouble("shooter.error", ACCEPTED_ERROR);
		speed = pref.getDouble("shooter.speed", fSPEED);
		flywheelMotor.set(speed);
		enabled = true;
	}

	public void disable() {
		flywheelMotor.set(0);
		enabled = false;
	}

	public boolean enabled() {
		return enabled;
	}

	public boolean isAtSpeed() {
		return Math.abs(flywheelMotor.getSpeed() - speed) < error;
	}

	public void setPowerMode(double power) {
		disable();
		flywheelMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		flywheelMotor.set(power);
	}

	public void endPowerMode() {
		flywheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		enable();
	}

	public void dumpValuesToSamrtDashboard() {
		SmartDashboard.putBoolean("isShooterAtSpeed", isAtSpeed());
		SmartDashboard.putNumber("ShooterRPM", flywheelMotor.getSpeed());
		SmartDashboard.putNumber("ShooterCurrentTrue", flywheelMotor.getOutputCurrent());
		SmartDashboard.putNumber("ShooterVoltageTrue", flywheelMotor.getOutputVoltage());
		SmartDashboard.putNumber("TargetSpeed", speed);
	}

	public void index(boolean on) {
		indexer.setSpeed(on ? iPower : 0);
	}

	public void incrementSpeed(double increment) {
		speed += increment;
	}

	public void decrementSpeed(double increment) {
		speed -= increment;
	}
}