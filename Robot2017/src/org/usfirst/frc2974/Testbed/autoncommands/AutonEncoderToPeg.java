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
	public static final double MAX_ACCELERATION = .7;

	public static final double MAX_SPEED_TURN = 1;
	public static final double MAX_ACCELERATION_TURN = .5;
	
	private final static Point2D point = new Point2D(0, 0);

	private final static Pose pose = new Pose(point, 0);
	public Position position;
	public boolean doesShoot;
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

	private void addDriveParameters(double angle, double distance) {
		driveTrain.cancelMotion();
		
		driveTrain.addControllerMotion(new MotionPathStraight(pose, MOVING_DISTANCE_LINE, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathTurn(pose, angle, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathStraight(pose, distance, MAX_SPEED, MAX_ACCELERATION));
	}

	@Override
	public void initialize(){
		switch (position) {
		case CENTER:
			addDriveParameters(0,-0.7535);
			break;
		case RIGHT:
			addDriveParameters(PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		case LEFT:
			addDriveParameters(-PEG_ANGLE, MOVING_DISTANCE_PEG);
			break;
		}
		
		this.driveTrain.startMotion();
	}
	
	@Override
	public boolean isFinished() {
		if(driveTrain.isControllerFinished()){
			return true;
		}
		return false;
	}
	
	@Override
	public void end(){
		driveTrain.cancelMotion();
	}
	@Override
	public void interrupted(){
		end();
	}
}
