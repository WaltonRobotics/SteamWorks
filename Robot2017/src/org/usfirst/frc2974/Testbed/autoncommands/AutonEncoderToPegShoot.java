package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPegCharge.Position;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPegCharge.State;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
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
public class AutonEncoderToPegShoot extends Command {

	private static final double HOLD_POWER = 0.25;

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

	private final static double PEG_OFF_DISPLACEMENT = 0.8;
	private final static double PEG_OFF_OFFSET = 0.4;
	private final static double CHARGE_DISTANCE = -10;

	private final static Pose ZERO = new Pose(new Point2D(0, 0), 0);

	public Position position;
	double startTime;
	double gearDelay;

	public enum Position {
		RED3, BLUE1;
	}

	public enum State {
		ToPeg {
			@Override
			public State run(AutonEncoderToPegShoot aetps) {
				if (aetps.driveTrain.isControllerFinished()) {
					return State.WaitForGear;
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
		WaitForGear {
			@Override
			public State run(AutonEncoderToPegShoot aetpc) {
				if (true) {
					return Delay;
				}
				return this; // FIXME: correct the if statement, once we know
								// how to use the sensor
			}

			@Override
			public void init(AutonEncoderToPegShoot aetpc) {
				aetpc.driveTrain.cancelMotion();
				aetpc.driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
			}
		},
		Delay {
			@Override
			public State run(AutonEncoderToPegShoot aetpc) {
				if (Timer.getFPGATimestamp() - aetpc.startTime > aetpc.gearDelay) {
					return State.ShootDrive;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderToPegShoot aetpc) {
				aetpc.driveTrain.cancelMotion();
				aetpc.driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
				aetpc.startTime = Timer.getFPGATimestamp();
			}
		},
		ShootDrive {
			@Override
			public State run(AutonEncoderToPegShoot aetps) {
				if (aetps.driveTrain.isControllerFinished()) {
					return State.ShootFire;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderToPegShoot aetpc) {
				Robot.shooter.enable();

				Robot.poseEstimator.reset();
				switch (aetpc.position) {
				case RED3:
					aetpc.addDriveParametersShoot(Math.PI / 3, -aetpc.shootAngle, aetpc.shootDistance);
					break;

				case BLUE1:
					aetpc.addDriveParametersShoot(-Math.PI / 3, aetpc.shootAngle, aetpc.shootDistance);
					break;
				}
				aetpc.driveTrain.startMotion();

			}
		},
		ShootFire {
			public void init(AutonEncoderToPegShoot aetps) {
				Robot.drivetrain.cancelMotion();
				Robot.shooter.index(true);
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
		requires(Robot.shooter);

	}

	private void addDriveParametersShoot(double pegAngle, double shootAngle, double shootDistance) {
		driveTrain.cancelMotion();

		Point2D displacement = new Point2D(Math.cos(pegAngle) * shootDistance + 0.2,
				Math.sin(pegAngle) * shootDistance);

		Pose atPeg = new Pose(new Point2D(0, 0), pegAngle);
		Pose displacementPose = new Pose(displacement, pegAngle + shootAngle);

		MotionProvider shootDrive = new MotionPathSpline(atPeg, shootDistance / 3.0, displacementPose,
				shootDistance / 3, MAX_SPEED, MAX_ACCELERATION, true);

		driveTrain.addControllerMotion(shootDrive);
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

	private double shootDistance;
	private double shootAngle;

	@Override
	public void initialize() {
		gearDelay = Preferences.getInstance().getDouble("drivetrain.pegDelay", 2.5);
		shootDistance = Preferences.getInstance().getDouble("drivetrain.shootDistance", 3);
		shootAngle = Math.PI * Preferences.getInstance().getDouble("driveTrain.shootAngle", 10) / 180;

		state = State.ToPeg;
		state.init(this);
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
		Robot.shooter.index(false);
		Robot.shooter.disable();
	}

	@Override
	public void interrupted() {
		end();
	}
}
