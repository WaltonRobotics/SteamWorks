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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonEncoderToPegGF extends Command {

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
	public ChargeState chargeState = ChargeState.PUSH_TO_PEG;

	private double delayStart;
	private double delayGoal;

	public enum Position {
		RED1, RED3, BLUE1, BLUE3
	}

	public enum ChargeState {
		PUSH_TO_PEG, DELAY, MOVE_FOWARD
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPegGF(Position position) {
		this.driveTrain = Robot.drivetrain;
		this.position = position;

	}

	private void addDriveParametersRed1Gear() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetRed1", 0);

		Pose peg = new Pose(PEG_POINT_RED1, -PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersRed3Gear() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetRed3", 0);

		Pose peg = new Pose(PEG_POINT_RED3, PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersBlue1Gear() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetBlue1", 0);

		Pose peg = new Pose(PEG_POINT_BLUE1, -PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersBlue3Gear() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetBlue3", 0);

		Pose peg = new Pose(PEG_POINT_BLUE3, PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParameters1Forward() {
		driveTrain.cancelMotion();

		MotionProvider offPeg = new MotionPathStraight(ZERO, 0.8, MAX_SPEED, MAX_ACCELERATION);
		MotionProvider turn = new MotionPathTurn(offPeg.getFinalPose(), PEG_ANGLE, 2, 1);

		driveTrain.addControllerMotion(offPeg);
		driveTrain.addControllerMotion(turn);
		driveTrain
				.addControllerMotion(new MotionPathStraight(turn.getFinalPose(), -2.4384, MAX_SPEED, MAX_ACCELERATION));
	}

	private void addDriveParameters3Forward() {
		driveTrain.cancelMotion();

		MotionProvider offPeg = new MotionPathStraight(ZERO, 0.8, MAX_SPEED, MAX_ACCELERATION);
		MotionProvider turn = new MotionPathTurn(offPeg.getFinalPose(), -PEG_ANGLE, 2, 1);

		driveTrain.addControllerMotion(offPeg);
		driveTrain.addControllerMotion(turn);
		driveTrain
				.addControllerMotion(new MotionPathStraight(turn.getFinalPose(), -2.4384, MAX_SPEED, MAX_ACCELERATION));
	}

	@Override
	public void initialize() {
		Robot.poseEstimator.reset();
		delayGoal = Preferences.getInstance().getDouble("drivetrain.pegDelay", 2.5);
		Robot.drivetrain.shiftDown();

		switch (position) {
		case RED1:
			addDriveParametersRed1Gear();
			break;
		case RED3:
			addDriveParametersRed3Gear();
			break;
		case BLUE1:
			addDriveParametersBlue1Gear();
			break;
		case BLUE3:
			addDriveParametersBlue3Gear();
			break;
		}

		this.driveTrain.startMotion();
	}

	@Override
	public void execute() {

		switch (chargeState) {
		case PUSH_TO_PEG:
			if (driveTrain.isControllerFinished()) {
				delayStart = Timer.getFPGATimestamp();
				chargeState = ChargeState.DELAY;
			}
			break;
		case DELAY:
			if (Timer.getFPGATimestamp() - delayStart >= delayGoal) {
				chargeState = ChargeState.MOVE_FOWARD;
			}
			break;
		case MOVE_FOWARD:
			if (position == Position.RED1 || position == Position.BLUE1) {
				addDriveParameters1Forward();
			}
			if (position == Position.RED3 || position == Position.BLUE3) {
				addDriveParameters3Forward();
			}
			break;
		}

		// if (!pushToPeg && driveTrain.isControllerFinished()) {
		// driveTrain.cancelMotion();
		// driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
		// pushToPeg = true;
		// } else if (pushToPeg) {
		// driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
		// }
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
