package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionPathTurn;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonEncoderToPeg extends Command {

	public static final double MAX_SPEED = 2;
	public static final double MAX_ACCELERATION = 2;

	public static final double PEG_SPEED = 1;
	public static final double PEG_ACCELERATION = 1;

	private final static Point2D PEG_POINT_RED1 = new Point2D(-2.44, 1.32);
	private final static Point2D PEG_POINT_RED3 = new Point2D(-2.28, -1.52);
	private final static Point2D PEG_POINT_BLUE1 = new Point2D(-2.64, 1.47);
	private final static Point2D PEG_POINT_BLUE3 = new Point2D(-2.28, -1.52);

	private final static double PEG_ANGLE = Math.PI / 3;
	private final static double PEG_START_LENGTH = 1.58;
	private final static double PEG_END_LENGTH = 0.95;

	private final static Pose ZERO = new Pose(new Point2D(0, 0), 0);

	public Position position;

	private boolean pushToPeg;
	private static final double HOLD_POWER = 0.25;

	public enum Position {
		RED1, RED2, RED3, BLUE1, BLUE2, BLUE3
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPeg(Position position) {
		this.driveTrain = Robot.drivetrain;
		this.position = position;

	}

	private void addDriveParametersRed2() {
		driveTrain.cancelMotion();

		driveTrain.addControllerMotion(new MotionPathStraight(ZERO, -2.00, MAX_SPEED, MAX_ACCELERATION));

	}

	private void addDriveParametersRed1() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetRed1", 0);

		Pose peg = new Pose(PEG_POINT_RED1.offsetPoint(-offset, 0), -PEG_ANGLE);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersRed3() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetRed3", 0);

		Pose peg = new Pose(PEG_POINT_RED3.offsetPoint(-offset, 0), PEG_ANGLE);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersBlue2() {
		driveTrain.cancelMotion();

		driveTrain.addControllerMotion(new MotionPathStraight(ZERO, -2.00, MAX_SPEED, MAX_ACCELERATION));

	}

	private void addDriveParametersBlue1() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetBlue1", 0);

		Pose peg = new Pose(PEG_POINT_BLUE1.offsetPoint(-offset, 0), -PEG_ANGLE);
		
		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersBlue3() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetBlue3", 0);

		Pose peg = new Pose(PEG_POINT_BLUE3.offsetPoint(-offset, 0), PEG_ANGLE);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	@Override
	public void initialize() {
		Robot.poseEstimator.reset();
		pushToPeg = Preferences.getInstance().getBoolean("drivetrain.pushToPeg", false);
		Robot.drivetrain.shiftDown();

		switch (position) {
		case RED2:
			addDriveParametersRed2();
			break;
		case RED3:
			addDriveParametersRed3();
			break;
		case RED1:
			addDriveParametersRed1();
			break;
		case BLUE2:
			addDriveParametersBlue2();
			break;
		case BLUE3:
			addDriveParametersBlue3();
			break;
		case BLUE1:
			addDriveParametersBlue1();
			break;
		}

		this.driveTrain.startMotion();
	}

	@Override
	public void execute() {
		if (!pushToPeg && driveTrain.isControllerFinished()) {
			driveTrain.cancelMotion();
			driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
			pushToPeg = true;
		} else if (pushToPeg) {
			driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
		}
	}

	@Override
	public boolean isFinished() {
		// if(driveTrain.isControllerFinished()){
		// return true;
		// }
		return false;
	}

	@Override
	public void end() {
		driveTrain.cancelMotion();
		driveTrain.setSpeeds(0, 0);
	}

	@Override
	public void interrupted() {
		end();
	}
}
