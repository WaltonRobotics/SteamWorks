package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionPathTurn;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonEncoderToPeg extends Command {
	public static final double MOVING_DISTANCE_LINE = -2.0532; 
	public static final double MOVING_DISTANCE_PEG = -1.778;
	
	public static final double MOVING_DISTANCE_BASELINE = 2.3622;
	// 61.26 degrees
	public static final double PEG_ANGLE = 1.08026;
	public static final double BOILER_ANGLE = 1.57;

	public static final double MAX_SPEED = 1;
	public static final double MAX_ACCELERATION = .8;
	public static final double MAX_CHARGE_ACCELERATION = .9;
	

	public static final double MAX_SPEED_TURN = 1;
	public static final double MAX_ACCELERATION_TURN = .5;
	
	private final static Point2D point = new Point2D(0, 0);

	private final static Pose pose = new Pose(point, 0);
	public Position position;
	public boolean doesShoot;
	private boolean hold;
	final double speed = 0.25;
	final double accel = 0.25;

	public Command toGoalCommand;

	public enum Position {
		LEFT, RIGHT, CENTER
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPeg(Position position) {
		this.driveTrain = Robot.drivetrain;
		this.position = position;
		
	}
	
	private void addDriveParametersCenter() {
		driveTrain.cancelMotion();
		
		driveTrain.addControllerMotion(new MotionPathStraight(pose, -2.00, MAX_SPEED, MAX_ACCELERATION));
	}
	
	private void addDriveParametersLeft(double angle, double distance) {
		driveTrain.cancelMotion();
		
		driveTrain.addControllerMotion(new MotionPathStraight(pose, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathTurn(pose, angle, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathStraight(pose, distance, MAX_SPEED, MAX_CHARGE_ACCELERATION));
	}
	
	private void addDriveParametersRight(double angle, double distance) {
		driveTrain.cancelMotion();
		
		driveTrain.addControllerMotion(new MotionPathStraight(pose, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathTurn(pose, angle, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathStraight(pose, distance, MAX_SPEED, MAX_CHARGE_ACCELERATION));
	}

	@Override
	public void initialize(){
		Robot.poseEstimator.reset();
		hold = false;
		switch (position) {
		case CENTER:
			addDriveParametersCenter();
			break;
		case RIGHT:
			addDriveParametersRight(PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		case LEFT:
			addDriveParametersLeft(-PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		}
		
		this.driveTrain.startMotion();
	}
	
	@Override
	public void execute() {
		if (!hold && driveTrain.isControllerFinished()) {
			driveTrain.cancelMotion();
			driveTrain.setSpeeds(-0.15, -0.15);
			hold = true;
		}
		else if (hold) {
			driveTrain.setSpeeds(-0.25, -0.25);
		}
	}
	
	@Override
	public boolean isFinished() {
//		if(driveTrain.isControllerFinished()){
//			return true;
//		}
		return false;
	}
	
	@Override
	public void end(){
		driveTrain.cancelMotion();
		driveTrain.setSpeeds(0, 0);
	}
	@Override
	public void interrupted(){
		end();
	}
}
