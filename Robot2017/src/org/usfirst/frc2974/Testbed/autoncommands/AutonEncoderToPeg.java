package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.auton.AutonDiffRunnable.Position;
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
	public static final double MOVING_DISTANCE = -2.1082;

	// 70 degrees
	public static final double PEG_ANGLE = 1.222;
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

	private void addDriveParameters(double angle) {
		driveTrain.addControllerMotion(new MotionPathStraight(pose, MOVING_DISTANCE, MAX_SPEED, MAX_ACCELERATION));
		driveTrain.addControllerMotion(new MotionPathTurn(pose, angle, MAX_SPEED, MAX_ACCELERATION));
	}

	@Override
	public void initialize(){
		switch (position) {
		case CENTER:
			addDriveParameters(0);
			break;
		case LEFT:
			addDriveParameters(PEG_ANGLE);
			break;
		case RIGHT:
			addDriveParameters(-PEG_ANGLE);
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
