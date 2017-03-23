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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutonEncoderToPegCharge extends Command {

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
	double startTime;
	double gearDelay;
	
	private static final double HOLD_POWER = 0.25;

	public enum Position {
		RED1, RED3, BLUE1, BLUE3;
	}

	public enum State {
		ToPeg {
			@Override
			public State run(AutonEncoderToPegCharge aetpc) {
				 if(aetpc.driveTrain.isControllerFinished()){
				 return Delay;
				 }
				return this;
			}

			@Override
			public void init(AutonEncoderToPegCharge aetpc) {
				aetpc.driveTrain.startMotion();
			}
		},
		Delay{
			@Override
			public State run(AutonEncoderToPegCharge aetpc) {
				if(Timer.getFPGATimestamp() - aetpc.startTime > aetpc.gearDelay){
					return Charge;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderToPegCharge aetpc) {
				aetpc.driveTrain.cancelMotion();
				aetpc.driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
				aetpc.startTime = Timer.getFPGATimestamp();
			}
		},
		Charge{
			@Override
			public State run(AutonEncoderToPegCharge aetpc) {
				 if(aetpc.driveTrain.isControllerFinished()){
				 return End;
				 }
				return this;
			}

			@Override
			public void init(AutonEncoderToPegCharge aetpc) {
				Robot.poseEstimator.reset();
				switch (aetpc.position) {
				case RED3:
					aetpc.addDriveParametersRed3GF();
					break;
				case RED1:
					aetpc.addDriveParametersRed1GF();
					break;
				case BLUE3:
					aetpc.addDriveParametersBlue3GF();
					break;
				case BLUE1:
					aetpc.addDriveParametersBlue1GF();
					break;
				}
				aetpc.driveTrain.startMotion();
			}
		},
		End{
			@Override
			public State run(AutonEncoderToPegCharge aetpc) {
				return this;
			}

			@Override
			public void init(AutonEncoderToPegCharge aetpc) {
				aetpc.driveTrain.cancelMotion();
				aetpc.driveTrain.setSpeeds(0, 0);
			}
		};
		public State run(AutonEncoderToPegCharge aetpc) {
			return this;
		}
		
		public void init(AutonEncoderToPegCharge aetpc) {
		}
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPegCharge(Position position) {
		this.driveTrain = Robot.drivetrain;
		this.position = position;

	}

	private void addDriveParametersRed1() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetRed1", 0);

		Pose peg = new Pose(PEG_POINT_RED1, -PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersRed3() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetRed3", 0);

		Pose peg = new Pose(PEG_POINT_RED3, PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersBlue1() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetBlue1", 0);

		Pose peg = new Pose(PEG_POINT_BLUE1, -PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersBlue3() {
		driveTrain.cancelMotion();

		double offset = Preferences.getInstance().getDouble("drivetrain.offsetBlue3", 0);

		Pose peg = new Pose(PEG_POINT_BLUE3, PEG_ANGLE);
		peg.offsetPoint(-offset);

		MotionProvider toPeg = new MotionPathSpline(ZERO, PEG_START_LENGTH + offset, peg, PEG_END_LENGTH, MAX_SPEED,
				MAX_ACCELERATION, false);

		driveTrain.addControllerMotion(toPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(toPeg.getFinalPose(), -0.3, PEG_SPEED, PEG_ACCELERATION));

	}

	private void addDriveParametersRed1GF() {
		driveTrain.cancelMotion();
		MotionProvider offPeg = new MotionPathStraight(ZERO, 0.8, MAX_SPEED, MAX_ACCELERATION);
		MotionProvider turn = new MotionPathTurn(offPeg.getFinalPose(),Math.PI / 3, MAX_SPEED, MAX_ACCELERATION);
		driveTrain.addControllerMotion(offPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(turn.getFinalPose(), -2.4384, MAX_SPEED, MAX_ACCELERATION));
	}

	private void addDriveParametersRed3GF() {
		driveTrain.cancelMotion();
		MotionProvider offPeg = new MotionPathStraight(ZERO, 0.8, MAX_SPEED, MAX_ACCELERATION);
		MotionProvider turn = new MotionPathTurn(offPeg.getFinalPose(),-Math.PI / 3, MAX_SPEED, MAX_ACCELERATION);
		driveTrain.addControllerMotion(offPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(turn.getFinalPose(), -2.4384, MAX_SPEED, MAX_ACCELERATION));
	}

	private void addDriveParametersBlue1GF() {
		driveTrain.cancelMotion();
		MotionProvider offPeg = new MotionPathStraight(ZERO, 0.8, MAX_SPEED, MAX_ACCELERATION);
		MotionProvider turn = new MotionPathTurn(offPeg.getFinalPose(),Math.PI / 3, MAX_SPEED, MAX_ACCELERATION);
		driveTrain.addControllerMotion(offPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(turn.getFinalPose(), -2.4384, MAX_SPEED, MAX_ACCELERATION));
	}

	private void addDriveParametersBlue3GF() {
		driveTrain.cancelMotion();
		MotionProvider offPeg = new MotionPathStraight(ZERO, 0.8, MAX_SPEED, MAX_ACCELERATION);
		MotionProvider turn = new MotionPathTurn(offPeg.getFinalPose(),-Math.PI / 3, MAX_SPEED, MAX_ACCELERATION);
		driveTrain.addControllerMotion(offPeg);
		driveTrain.addControllerMotion(new MotionPathStraight(turn.getFinalPose(), -2.4384, MAX_SPEED, MAX_ACCELERATION));
	}

	@Override
	public void initialize() {
		Robot.poseEstimator.reset();
		gearDelay = Preferences.getInstance().getDouble("drivetrain.pegDelay", 2.5);
		Robot.drivetrain.shiftDown();

		switch (position) {
		case RED3:
			addDriveParametersRed3();
			break;
		case RED1:
			addDriveParametersRed1();
			break;
		case BLUE3:
			addDriveParametersBlue3();
			break;
		case BLUE1:
			addDriveParametersBlue1();
			break;
		}
	}

	State state = State.ToPeg;
	@Override
	public void execute() {
		State newState = state.run(this);
		if (newState != state) {
			state = newState;
			SmartDashboard.putString("AutonState", state.name());
			state.init(this);
		}
	}

	@Override
	public boolean isFinished() {
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
