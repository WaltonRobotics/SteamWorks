package org.usfirst.frc2974.Testbed.auton;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionPathTurn;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonEncoderWithVision extends CommandGroup {

	public static final double MOVING_DISTANCE = 2.1082;

	// 70 degrees
	public static final double PEG_ANGLE = 1.222;
	public static final double BOILER_ANGLE = 1.57;

	public static final double MAX_SPEED = 1;
	public static final double MAX_ACCELERATION = .5;

	private final static Point2D point = new Point2D(0, 0);

	private final static Pose pose = new Pose(point, 0);
	/*
	 * public AutonEncoderWithVision(boolean command) { if (command)
	 * autonSequence();
	 * 
	 * else moveForward(); }
	 * 
	 * private void autonSequence() { boolean found = false;
	 * 
	 * Robot.drivetrain .addControllerMotion(new MotionPathStraight(pose,
	 * -MOVING_DISTANCE, MAX_SPEED, MAX_ACCELERATION));
	 * 
	 * if (!(found = hasFoundPeg())) { Robot.drivetrain.addControllerMotion(new
	 * MotionPathTurn(pose, PEG_ANGLE, MAX_SPEED, MAX_ACCELERATION));
	 * 
	 * if (!(found = hasFoundPeg())) { Robot.drivetrain .addControllerMotion(new
	 * MotionPathTurn(pose, PEG_ANGLE * 2, MAX_SPEED, MAX_ACCELERATION)); found
	 * = hasFoundPeg(); } }
	 * 
	 * if (found) movingToPeg(found); }
	 * 
	 * public boolean hasFoundPeg() { return
	 * !(SmartDashboard.getString("status peg", "").equals("too few contours")
	 * && !SmartDashboard.getBoolean("Valid angle peg", false)); }
	 * 
	 * public boolean hasFoundGoal() { return
	 * !(SmartDashboard.getString("status goal",
	 * "").equals("too few contours")); }
	 * 
	 * public void moveForward() { Robot.drivetrain.addControllerMotion(new
	 * MotionPathStraight(pose, 2.54, MAX_SPEED, MAX_ACCELERATION)); }
	 * 
	 * private void movingToPeg(boolean found) {
	 * 
	 * double distance = SmartDashboard.getNumber("Camera distance peg", 0) *
	 * 2.54 / 100; double angle = SmartDashboard.getNumber("Target angle peg",
	 * 0);
	 * 
	 * if (SmartDashboard.getBoolean("Valid angle peg", false)) {
	 * 
	 * Pose final_ = new Pose(new Point2D(pose.X.x + distance, pose.X.y +
	 * distance), angle);
	 * 
	 * Robot.drivetrain.addControllerMotion(new MotionPathSpline(pose, distance
	 * * Math.cos(angle), final_, distance * Math.sin(angle), MAX_SPEED,
	 * MAX_ACCELERATION));
	 * 
	 * moveToBoiler(); }
	 * 
	 * }
	 * 
	 * private void moveToBoiler() { boolean found = false;
	 * 
	 * //70 should be changed as it was put randomly. Units are in inches and
	 * converted to meters. Robot.drivetrain .addControllerMotion(new
	 * MotionPathStraight(pose, 70 * 2.54 / 100, MAX_SPEED, MAX_ACCELERATION));
	 * 
	 * if (!(found = hasFoundPeg())) { Robot.drivetrain.addControllerMotion(new
	 * MotionPathTurn(pose, -BOILER_ANGLE, MAX_SPEED, MAX_ACCELERATION));
	 * 
	 * if (!(found = hasFoundPeg())) { Robot.drivetrain .addControllerMotion(new
	 * MotionPathTurn(pose, BOILER_ANGLE * 2, MAX_SPEED, MAX_ACCELERATION));
	 * found = hasFoundPeg(); } }
	 * 
	 * if (found) driveToBoiler(found); }
	 * 
	 * public void driveToBoiler(boolean found) {
	 * 
	 * final double angle = SmartDashboard.getNumber("Camera angle goal", 0);
	 * 
	 * final double distance = SmartDashboard.getNumber("To Flush goal", 0) *
	 * 2.54 / 100;
	 * 
	 * Pose final_ = new Pose(new Point2D(pose.X.x + distance, pose.X.y +
	 * distance), angle);
	 * 
	 * Robot.drivetrain.addControllerMotion(new MotionPathSpline(pose, distance
	 * * Math.cos(angle), final_, distance * Math.sin(angle), MAX_SPEED,
	 * MAX_ACCELERATION)); }
	 */

}
