package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutonEncoderToPegShoot extends Command {

	public static final double MAX_SPEED = 2;
	public static final double MAX_ACCELERATION = 2;

	public static final double PEG_SPEED = 1;
	public static final double PEG_ACCELERATION = 1;

	private final static Point2D PEG_POINT_RED3 = new Point2D(-2.28, -1.52);
	private final static Point2D PEG_POINT_BLUE1 = new Point2D(-2.64, 1.47);
	
	private final static double PEG_ANGLE = Math.PI / 3;
	private final static double PEG_START_LENGTH = 1.58;
	private final static double PEG_END_LENGTH = 0.95;

	private final static Pose ZERO = new Pose(new Point2D(0, 0), 0);

	public Position position;

	private static final double HOLD_POWER = 0.25;

	public enum Position {
		RED3, BLUE1;
	}

	public enum State {
		ToPeg {
			@Override
			public State run(AutonEncoderToPegShoot aetps) {
				if (aetps.driveTrain.isControllerFinished()) {
					return Shoot;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderToPegShoot aetps) {
				Robot.drivetrain.shiftDown();
				Robot.poseEstimator.reset();

				switch (aetps.position) {
				case RED3:
					aetps.addDriveParametersRed3();
					break;
				case BLUE1:
					aetps.addDriveParametersBlue1();
					break;
				}
				aetps.driveTrain.startMotion();
			}
		},
		Shoot {
			@Override
			public State run(AutonEncoderToPegShoot aetps) {
				Robot.shooter.index(true);
				Robot.shooter.enable();
				return this;
			}

			@Override
			public void init(AutonEncoderToPegShoot aetps) {
			}
		};
		
		public State run(AutonEncoderToPegShoot aetps) {
			return this;
		}

		public void init(AutonEncoderToPegShoot aetps) {
		}
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPegShoot(Position position) {
		this.driveTrain = Robot.drivetrain;
		this.position = position;

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
	
	@Override
	public void initialize() {
		state = State.ToPeg;
		state.init(this);
		requires(Robot.shooter);
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
