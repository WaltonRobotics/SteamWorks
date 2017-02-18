package org.usfirst.frc2974.Robot2017.subsystems;

import org.usfirst.frc2974.Robot2017.Robot;
import org.usfirst.frc2974.Robot2017.RobotMap;
import org.usfirst.frc2974.Robot2017.commands.*;
import org.usfirst.frc2974.Robot2017.controllers.MotionProfileController;
import org.usfirst.frc2974.Robot2017.controllers.MotionProvider;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**

 *

 */

public class Drivetrain extends Subsystem {

	private final double PERIOD = .01;
	public final double DEFAULTKV = 0;
	public final double DEFAULTKK = 0;
	public final double DEFAULTKA = 0;
	public final double DEFAULTKP = 0;

	private Talon right = RobotMap.right;
	private Talon left = RobotMap.left;

	Solenoid shifter = RobotMap.pneumaticsShifter;

	private MotionProfileController controller;

	public synchronized void setSpeeds(double leftSpeed, double rightSpeed) {
		right.set(-rightSpeed);
		left.set(leftSpeed);
	}

	public synchronized double getSpeeds() {
		return 0.5 * (right.get() + left.get());
	}

	public Drivetrain() {

		controller = new MotionProfileController(DEFAULTKV, DEFAULTKK, DEFAULTKA, DEFAULTKP, Robot.poseEstimator,
				PERIOD);

	}

	public boolean getControllerStatus() {
		return controller.getEnabled();
	}

	public void cancelMotion() {
		controller.cancel();
	}

	public void setControllerMotion(MotionProvider motion) {
		controller.setMotion(motion);
	}

	public void shiftUp() {
		if (!shifter.get()) {
			shifter.set(true);
		}
	}

	public void shiftDown() {
		if (shifter.get()) {
			shifter.set(false);
		}
	}

	public void dumpSmartdashboardValues() {

		//

	}

	public void initDefaultCommand() {

		setDefaultCommand(new Drive());

	}

	public void setConstants() {

		double kV = SmartDashboard.getNumber("kV", DEFAULTKV);
		double kK = SmartDashboard.getNumber("kK", DEFAULTKK);
		double kA = SmartDashboard.getNumber("kA", DEFAULTKA);
		double kP = SmartDashboard.getNumber("kP", DEFAULTKP);

		System.out.println(String.format("kV=%f, kK=%f, kA=%f, kP=%f", kV, kK, kA, kP));

		controller.setKV(kV);
		controller.setKK(kK);
		controller.setKA(kA);
		controller.setKP(kP);

	}

	public void readConstants() {
		SmartDashboard.putNumber("kV", controller.getKV());
		SmartDashboard.putNumber("kK", controller.getKK());
		SmartDashboard.putNumber("kA", controller.getKA());
		SmartDashboard.putNumber("kP", controller.getKP());
	}
}