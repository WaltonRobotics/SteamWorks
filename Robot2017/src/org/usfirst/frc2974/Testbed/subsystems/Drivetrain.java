package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.*;
import org.usfirst.frc2974.Testbed.controllers.MotionProfileController;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**

 *

 */

public class Drivetrain extends Subsystem {

	private static final double PERIOD = .005;
	private static final double DEFAULTKV = 0.5;
	private static final double DEFAULTKK = 0;
	private static final double DEFAULTKA = 0.1;
	private static final double DEFAULTKP = 20;
	private static final Driver DEFAULTDRIVER = Driver.Robert;

	public enum Driver {
		Robert {
			public String getName() {
				return "Robert";
			}
		},
		Tank {
			public String getName() {
				return "Tank";
			}
		},
		Cheesy {
			public String getName() {
				return "Cheesy";
			}
		};
		public String getName() {
			return null;
		}
	}

	public Driver convertedDriver(String driver) {
		for (int i = 0; i < Driver.values().length; i++) {
			if (driver == Driver.values()[i].getName()) {
				return Driver.values()[i];
			}
		}
		return Driver.Robert;
	}

	private Talon right = RobotMap.right;
	private Talon left = RobotMap.left;
	Solenoid shifter = RobotMap.pneumaticsShifter;
	private Driver driver;
	private MotionProfileController controller;

	public synchronized void setSpeeds(double leftSpeed, double rightSpeed) {
		right.set(rightSpeed);
		left.set(-leftSpeed);
	}

	public Drivetrain() {
		controller = new MotionProfileController(Robot.poseEstimator, PERIOD);
		setConstants();
		setDriver();
	}

	public void setDriver() {
		driver = convertedDriver(Preferences.getInstance().getString("drivetrain.driver", "Robert"));
	}

	public Driver getDriver() {
		return driver;
	}

	public static void declarePrefs(boolean reset) {
		Preferences pref = Preferences.getInstance();
		if (reset || !pref.containsKey("drivetrain.kV")) {
			pref.putDouble("drivetrain.kV", DEFAULTKV);
		}
		if (reset || !pref.containsKey("drivetrain.kK")) {
			pref.putDouble("drivetrain.kK", DEFAULTKK);
		}
		if (reset || !pref.containsKey("drivetrain.kA")) {
			pref.putDouble("drivetrain.kA", DEFAULTKA);
		}
		if (reset || !pref.containsKey("drivetrain.kP")) {
			pref.putDouble("drivetrain.kP", DEFAULTKP);
		}
		if (reset || !pref.containsKey("drivetrain.driver")) {
			pref.putString("drivetrain.driver", DEFAULTDRIVER.getName());
		}
	}

	public boolean getControllerStatus() {
		return controller.getEnabled();
	}

	public void cancelMotion() {
		controller.cancel();
	}

	public void startMotion() {
		controller.enable();
	}

	public void addControllerMotion(MotionProvider motion) {
		controller.addMotion(motion);
	}

	public boolean isControllerFinished() {
		return controller.isFinished();
	}

	public void shiftUp() {
		if (shifter.get()) {
			shifter.set(false);
		}
	}

	public void shiftDown() {
		if (!shifter.get()) {
			shifter.set(true);
		}
	}

	public void dumpSmartdashboardValues() {
		SmartDashboard.putNumber("Power Left", left.get());
		SmartDashboard.putNumber("Power Right", right.get());

		SmartDashboard.putNumber("Raw Left", left.getRaw());
		SmartDashboard.putNumber("Raw Right", right.getRaw());

	}

	public void initDefaultCommand() {
		setDefaultCommand(new Drive());
	}

	public void setConstants() {
		Preferences pref = Preferences.getInstance();
		double kV = pref.getDouble("drivetrain.kV", DEFAULTKV);
		double kK = pref.getDouble("drivetrain.kK", DEFAULTKK);
		double kA = pref.getDouble("drivetrain.kA", DEFAULTKA);
		double kP = pref.getDouble("drivetrain.kP", DEFAULTKP);
		System.out.println(String.format("kV=%f, kK=%f, kA=%f, kP=%f", kV, kK, kA, kP));
		controller.setKV(kV);
		controller.setKK(kK);
		controller.setKA(kA);
		controller.setKP(kP);

	}
}