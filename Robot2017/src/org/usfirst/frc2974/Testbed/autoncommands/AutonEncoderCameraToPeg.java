package org.usfirst.frc2974.Testbed.autoncommands;

import java.util.Objects;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPeg.Position;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderCameraToPeg.State;
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
public class AutonEncoderCameraToPeg extends Command {

	public static final double MAX_SPEED = 2;
	public static final double MAX_ACCELERATION = 2;

	public static final double PEG_SPEED = 1;
	public static final double PEG_ACCELERATION = 1;

	private final static double PEG_ANGLE = Math.PI / 3;

	private final static Pose ZERO = new Pose(new Point2D(0, 0), 0);

	private final static double PEG_OFF_DISPLACEMENT = 0.8;
	private final static double PEG_OFF_OFFSET = 0.4;
	private final static double CHARGE_DISTANCE = -4.5;

	public Position position;
	public Control control;
	private State state;

	private boolean pushToPeg;
	private static final double HOLD_POWER = 0.25;

	double gearDelay;
	double startTime;
	double chargeAngle;
	double shootAngle;
	double shootDistance;

	public enum Position {
		RED1, RED3, CENTER, BLUE1, BLUE3
	}

	public enum Control {
		NOTHING, CHARGE, SHOOT
	}

	public enum State {
		ToOffset {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				if (aectp.driveTrain.isControllerFinished()) {
					return ReadCamera;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				Robot.drivetrain.shiftDown();
				Robot.poseEstimator.reset();

				switch (aectp.position) {
				case CENTER:
					aectp.addDriveParametersCenter();
					break;
				case BLUE1:
					aectp.addDriveParametersSide(new Point2D(-1.98, 0.95), -Math.PI / 3,
							Preferences.getInstance().getDouble("drivetrain.offsetBlue1", 0));
					aectp.chargeAngle = -Math.PI / 3;
					break;
				case BLUE3:
					aectp.addDriveParametersSide(new Point2D(-1.98, -0.80), Math.PI / 3,
							Preferences.getInstance().getDouble("drivetrain.offsetBlue3", 0));
					aectp.chargeAngle = Math.PI / 3;
					break;
				case RED1:
					aectp.addDriveParametersSide(new Point2D(-1.98, 0.80), -Math.PI / 3,
							Preferences.getInstance().getDouble("drivetrain.offsetRed1", 0));
					aectp.chargeAngle = -Math.PI / 3;
					break;
				case RED3:
					aectp.addDriveParametersSide(new Point2D(-1.98, -0.95), Math.PI / 3,
							Preferences.getInstance().getDouble("drivetrain.offsetRed3", 0));
					aectp.chargeAngle = Math.PI / 3;
					break;
				}
				aectp.driveTrain.startMotion();
			}
		},
		ReadCamera {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				return DriveToPeg;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				aectp.driveTrain.cancelMotion();
				aectp.driveTrain.addControllerMotion(aectp.createCameraSpline());
			}
		},
		DriveToPeg {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				if (aectp.driveTrain.isControllerFinished()) {
					if (Objects.equals(aectp.control, Control.NOTHING)) {
						return End;
					} else {
						return Delay;
					}
				}
				return this;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				// aectp.driveTrain.startMotion();
			}
		},
		Delay {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				if (Timer.getFPGATimestamp() - aectp.startTime > aectp.gearDelay) {
					if (Objects.equals(aectp.control, Control.CHARGE)) {
						return Charge;
					} else {
						return ShootDrive;
					}
				}
				return this;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				aectp.driveTrain.cancelMotion();
				aectp.driveTrain.setSpeeds(-HOLD_POWER, -HOLD_POWER);
				aectp.startTime = Timer.getFPGATimestamp();
			}
		},
		Charge {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				if (aectp.driveTrain.isControllerFinished()) {
					return End;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				Robot.poseEstimator.reset();
				aectp.addDriveParametersGoForward(aectp.chargeAngle);
				aectp.driveTrain.startMotion();
			}
		},
		ShootDrive {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				if (aectp.driveTrain.isControllerFinished()) {
					return ShootFire;
				}
				return this;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				Robot.poseEstimator.reset();
				aectp.addDriveParametersGoForward(aectp.chargeAngle);
				aectp.driveTrain.startMotion();
			}
		},
		ShootFire {
			public void init(AutonEncoderCameraToPeg aectp) {
				Robot.drivetrain.cancelMotion();
				Robot.shooter.index(true);
			}
		},
		End {
			@Override
			public State run(AutonEncoderCameraToPeg aectp) {
				return this;
			}

			@Override
			public void init(AutonEncoderCameraToPeg aectp) {
				aectp.driveTrain.cancelMotion();
				aectp.driveTrain.setSpeeds(0, 0);
			}
		};
		public State run(AutonEncoderCameraToPeg aectp) {
			return this;
		}

		public void init(AutonEncoderCameraToPeg aectp) {
		}
	}

	private Drivetrain driveTrain;

	public AutonEncoderCameraToPeg(Position position, Control control) {
		this.driveTrain = Robot.drivetrain;
		this.control = control;
		this.position = position;
	}

	private void addDriveParametersCenter() {
		driveTrain.cancelMotion();

		driveTrain.addControllerMotion(new MotionPathStraight(ZERO, -0.984, MAX_SPEED, MAX_ACCELERATION));
	}

	private void addDriveParametersSide(Point2D pegPoint, double angle, double offset) {
		driveTrain.cancelMotion();

		Pose peg = new Pose(pegPoint, PEG_ANGLE);

		double l0 = pegPoint.x * 2 / 3;
		double l1 = pegPoint.y * 2 / 3;

		driveTrain.addControllerMotion(new MotionPathSpline(ZERO, l0, peg, l1, MAX_SPEED, MAX_ACCELERATION, false));
	}

	private MotionProvider createCameraSpline() {
		driveTrain.cancelMotion();

		double x = -1.016;
		double y = SmartDashboard.getNumber("mm peg centerX", 0) / 1000;
		double l0 = x / 2;
		double l1 = y / 2;

		Point2D pegPoint = new Point2D(x, y);

		Pose peg = new Pose(pegPoint, 0);

		System.out.println(y);

		return new MotionPathSpline(ZERO, l0, peg, l1, PEG_SPEED, PEG_ACCELERATION, false);
	}

	private void addDriveParametersGoForward(double pegAngle) {
		driveTrain.cancelMotion();

		Point2D displacement = new Point2D(Math.cos(pegAngle) * PEG_OFF_DISPLACEMENT + PEG_OFF_OFFSET,
				Math.sin(pegAngle) * PEG_OFF_DISPLACEMENT);

		Pose atPeg = new Pose(new Point2D(0, 0), pegAngle);
		Pose displacementPose = new Pose(displacement, 0);

		MotionProvider chargeBack = new MotionPathSpline(atPeg, PEG_OFF_DISPLACEMENT * 2 / 3, displacementPose,
				PEG_OFF_OFFSET * 2 / 3, MAX_SPEED, MAX_ACCELERATION, true);

		driveTrain.addControllerMotion(chargeBack);
		driveTrain.addControllerMotion(
				new MotionPathStraight(chargeBack.getFinalPose(), CHARGE_DISTANCE, MAX_SPEED, MAX_ACCELERATION));
	}

	private void addDriveParametersShoot(double pegAngle) {
		driveTrain.cancelMotion();

		Point2D displacement = new Point2D(Math.cos(pegAngle) * shootDistance + 0.2,
				Math.sin(pegAngle) * shootDistance);

		Pose atPeg = new Pose(new Point2D(0, 0), pegAngle);
		Pose displacementPose = new Pose(displacement, pegAngle + shootAngle);

		MotionProvider shootDrive = new MotionPathSpline(atPeg, shootDistance / 3.0, displacementPose,
				shootDistance / 3, MAX_SPEED, MAX_ACCELERATION, true);

		driveTrain.addControllerMotion(shootDrive);
	}

	public void initialize() {
		Robot.poseEstimator.reset();
		pushToPeg = Preferences.getInstance().getBoolean("drivetrain.pushToPeg", false);
		Robot.drivetrain.shiftDown();

		gearDelay = Preferences.getInstance().getDouble("drivetrain.pegDelay", 2.5);
		shootDistance = Preferences.getInstance().getDouble("drivetrain.shootDistance", 3);
		shootAngle = Math.PI * Preferences.getInstance().getDouble("driveTrain.shootAngle", 10) / 180;

		state = State.ToOffset;
		state.init(this);
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
