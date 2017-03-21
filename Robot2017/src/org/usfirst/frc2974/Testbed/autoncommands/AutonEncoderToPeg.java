package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionPathTurn;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
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

	public static final double MAX_SPEED = 2;
	public static final double MAX_ACCELERATION = 2;
	
	public static final double PEG_SPEED = 1;
	public static final double PEG_ACCELERATION = 1;

//	public static final double MAX_SPEED_TURN = 1;
//	public static final double MAX_ACCELERATION_TURN = .5;
	
	private final static Point2D point = new Point2D(0, 0);

	private final static Pose zero = new Pose(point, 0);
	public Position position;
	public boolean doesShoot;
	private boolean hold;
	final double speed = 0.25;
	final double accel = 0.25;

	public Command toGoalCommand;

	public enum Position {
		RED1, RED2, RED3, BLUE1, BLUE2,BLUE3
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPeg(Position position) {
		this.driveTrain = Robot.drivetrain;
		this.position = position;
		
	}
	
	private void addDriveParametersRed2() {
		driveTrain.cancelMotion();
		
		driveTrain.addControllerMotion(new MotionPathStraight(zero, -2.00, MAX_SPEED, MAX_ACCELERATION));

	}
	
	private void addDriveParametersRed1(double angle, double distance) {
		driveTrain.cancelMotion();
		
		Pose peg = new Pose(new Point2D(-2.44, 1.32), -Math.PI / 3.0);
		
		MotionProvider toPeg = new MotionPathSpline(zero, 1.58, peg, 1.05, MAX_SPEED, MAX_ACCELERATION, false);
		
		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED , PEG_ACCELERATION));
		
		
		
//		driveTrain.addControllerMotion(new MotionPathStraight(zero, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
//		driveTrain.addControllerMotion(new MotionPathTurn(zero, angle, MAX_SPEED, MAX_ACCELERATION));

//		driveTrain.addControllerMotion(new MotionPathStraight(zero, distance, MAX_SPEED, MAX_ACCELERATION));
	}
	
	private void addDriveParametersRed3(double angle, double distance) {
		driveTrain.cancelMotion();

		Pose peg = new Pose(new Point2D(-2.28, -1.67), Math.PI / 3.0 + 0.15);
		
		MotionProvider toPeg = new MotionPathSpline(zero, 1.58, peg, .95, MAX_SPEED, MAX_ACCELERATION, false);
		
		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED , PEG_ACCELERATION));
		
//		driveTrain.addControllerMotion(new MotionPathStraight(zero, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
//		driveTrain.addControllerMotion(new MotionPathTurn(zero, angle, MAX_SPEED, MAX_ACCELERATION));
		

//		driveTrain.addControllerMotion(new MotionPathStraight(zero, distance, MAX_SPEED, MAX_ACCELERATION));
	}
	
	private void addDriveParametersBlue2() {
		driveTrain.cancelMotion();
		
		driveTrain.addControllerMotion(new MotionPathStraight(zero, -2.00, MAX_SPEED, MAX_ACCELERATION));

	}
	
	
	
	private void addDriveParametersBlue1(double angle, double distance) {
		driveTrain.cancelMotion();
		
		Pose peg = new Pose(new Point2D(-2.64, 1.47), -Math.PI / 3.0);
		
		MotionProvider toPeg = new MotionPathSpline(zero, 1.78, peg, 1.05, MAX_SPEED, MAX_ACCELERATION, false);
		
		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED , PEG_ACCELERATION));
		
		
//		driveTrain.addControllerMotion(new MotionPathStraight(zero, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
//		driveTrain.addControllerMotion(new MotionPathTurn(zero, angle, MAX_SPEED, MAX_ACCELERATION));
		

//		driveTrain.addControllerMotion(new MotionPathStraight(zero, distance, MAX_SPEED, MAX_ACCELERATION));
	}
	
	private void addDriveParametersBlue3(double angle, double distance) {
		driveTrain.cancelMotion();
		
		Pose peg = new Pose(new Point2D(-2.28, -1.52), Math.PI / 3.0 + 0.15);
		
		MotionProvider toPeg = new MotionPathSpline(zero, 1.58, peg, .95, MAX_SPEED, MAX_ACCELERATION, false);
		
		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED , PEG_ACCELERATION));
		
		

//		driveTrain.addControllerMotion(new MotionPathStraight(zero, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
//		driveTrain.addControllerMotion(new MotionPathTurn(zero, angle, MAX_SPEED, MAX_ACCELERATION));
		

//		driveTrain.addControllerMotion(new MotionPathStraight(zero, distance, MAX_SPEED, MAX_ACCELERATION));
	}

	@Override
	public void initialize(){
		Robot.poseEstimator.reset();
		hold = false;
		Robot.drivetrain.shiftDown();
		
		switch (position) {
		case RED2:
			addDriveParametersRed2();
			break;
		case RED3:
			addDriveParametersRed3(PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		case RED1:
			addDriveParametersRed1(-PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		case BLUE2:
			addDriveParametersBlue2();
			break;
		case BLUE3:
			addDriveParametersBlue3(PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		case BLUE1:
			addDriveParametersBlue1(-PEG_ANGLE, MOVING_DISTANCE_PEG);
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
