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
		try {
			return Driver.valueOf(Driver.class, driver);
		} catch (IllegalArgumentException ignored) {
			return Driver.Tank;
		}
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
		setDriver(Robot.driverSelect.getSelected());
	}

	public void setDriver(String driver) {
		this.driver = convertedDriver(driver);
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
		if (reset || !pref.containsKey("drivetrain.offsetRed1")) {
			pref.putDouble("drivetrain.offsetRed1", 0.2);
		}
		if (reset || !pref.containsKey("drivetrain.offsetRed3")) {
			pref.putDouble("drivetrain.offsetRed3", 0.3);
		}
		if (reset || !pref.containsKey("drivetrain.offsetBlue1")) {
			pref.putDouble("drivetrain.offsetBlue1", 0.5);
		}
		if (reset || !pref.containsKey("drivetrain.offsetBlue3")) {
			pref.putDouble("drivetrain.offsetBlue3", 0.3);
		}
		if (reset || !pref.containsKey("drivetrain.minThrottle")) {
			pref.putDouble("drivetrain.minThrottle", 0.3);
		}
		if (reset || !pref.containsKey("drivetrain.pushToPeg")) {
			pref.putBoolean("drivetrain.pushToPeg", false);
		}
		if (reset || !pref.containsKey("drivetrain.pegDelay")) {
			pref.putDouble("drivetrain.pegDelay", 2.5);
		}
		if (reset || !pref.containsKey("drivetrain.shootDistance")) {
			pref.putDouble("drivetrain.shootDistance", 3.5);
		}
		if (reset || !pref.containsKey("drivetrain.shootAngle")) {
			pref.putDouble("drivetrain.shootAngle", 10.0);
		}
		if (reset || !pref.containsKey("drivetrain.offsetAngleB1")) {
			pref.putDouble("drivetrain.offsetAngleB1", -0.125);
		}
		if (reset || !pref.containsKey("drivetrain.offsetAngleB3")) {
			pref.putDouble("drivetrain.offsetAngleB3", 0.3);
		}
		if (reset || !pref.containsKey("drivetrain.offsetAngleR1")) {
			pref.putDouble("drivetrain.offsetAngleR1", -0.15);
		}
		if (reset || !pref.containsKey("drivetrain.offsetAngleR3")) {
			pref.putDouble("drivetrain.offsetAngleR3", 0.175);
		}
		if (reset || !pref.containsKey("drivetrain.offsetRed1Legacy")) {
			pref.putDouble("drivetrain.offsetRed1Legacy", 0);
		}
		if (reset || !pref.containsKey("drivetrain.offsetRed3Legacy")) {
			pref.putDouble("drivetrain.offsetRed3Legacy", 0);
		}
		if (reset || !pref.containsKey("drivetrain.offsetBlue1Legacy")) {
			pref.putDouble("drivetrain.offsetBlue1Legacy", 0);
		}
		if (reset || !pref.containsKey("drivetrain.offsetBlue3Legacy")) {
			pref.putDouble("drivetrain.offsetBlue3Legacy", 0);
		}
		if (reset || !pref.containsKey("drivetrain.autonLegacy")) {
			pref.putBoolean("drivetrain.autonLegacy", false);
		}
		if (reset || !pref.containsKey("drivetrain.pegExtraR1")) {
			pref.putDouble("drivetrain.pegExtraR1", 0.0);
		}
		if (reset || !pref.containsKey("drivetrain.pegExtraR3")) {
			pref.putDouble("drivetrain.pegExtratR3", 0.0);
		}
		if (reset || !pref.containsKey("drivetrain.pegExtraB1")) {
			pref.putDouble("drivetrain.pegExtraB1", 0.0);
		}
		if (reset || !pref.containsKey("drivetrain.pegExtraB3")) {
			pref.putDouble("drivetrain.pegExtraB3", 0.0);
		}
		if (reset || !pref.containsKey("drivetrain.pegExtraCenter")) {
			pref.putDouble("drivetrain.pegExtraCenter", 0.0);
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