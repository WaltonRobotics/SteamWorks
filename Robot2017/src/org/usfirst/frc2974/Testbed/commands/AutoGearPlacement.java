package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightByEncoder;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoGearPlacement extends Command {
	private static final String CAMERA_CENTER_X_KEY = "mm peg centerX";

	private enum State {
		LOOKING {
			@Override
			State run() {
				if (!Robot.oi.autoGearPlacement.get() && SmartDashboard.getNumber(CAMERA_CENTER_X_KEY, 0) == 0
						&& !Robot.gearIntake.hasGear())
					return super.run();

				return State.MOVE_TO_PEG;
			}
		},
		MOVE_TO_PEG {
			@Override
			void init() {
				Robot.drivetrain.cancelMotion();
				
				Robot.drivetrain.addControllerMotion(createCameraSpline(0));
				
				Robot.drivetrain.startMotion();
			}

			@Override
			State run() {
				// TODO move the robot to pegh using spline
				// TODO figure out how to use the distance parameter
				if(Robot.drivetrain.isControllerFinished())
					return State.WAIT_FOR_GEAR_REMOVAL;
					
					return super.run();
			}
			
			private final Pose ZERO = new Pose(new Point2D(0, 0), 0);
			
			public static final double PEG_SPEED = 1;
			public static final double PEG_ACCELERATION = 1;
			
			private MotionProvider createCameraSpline(double offset) {
				Robot.drivetrain.cancelMotion();

				double x = -1.016 - offset;
				double y = SmartDashboard.getNumber("mm peg centerX", 0) / 1000;
				double controlPoint = Math.abs(x / 2);

				Point2D pegPoint = new Point2D(x, y);

				Pose peg = new Pose(pegPoint, 0);

				System.out.println(y);

				return new MotionPathSpline(ZERO, controlPoint, peg, controlPoint, PEG_SPEED, PEG_ACCELERATION, false);
			}
		},
		WAIT_FOR_GEAR_REMOVAL {
			@Override
			State run() {
				if (Robot.gearIntake.hasGear())
					return State.WAIT_FOR_BACKING;

				return super.run();
			}
		},
		WAIT_FOR_BACKING {

			double startTime;
			double delay;

			@Override
			void init() {
				startTime = Timer.getFPGATimestamp();
				delay = Preferences.getInstance().getDouble("drivetrain.pegDelay", 2.5);
			}

			@Override
			State run() {
				if (Timer.getFPGATimestamp() - startTime < delay)
					return super.run();

				return State.BACK_UP;
			}
		},
		BACK_UP {
			@Override
			void init() {
				Robot.drivetrain.cancelMotion();
				
				Pose pose = new Pose(new Point2D(0, 0), 0);
				
				MotionPathStraight forwardDrive = new MotionPathStraight(pose, .5,1, 1);
				
				Robot.drivetrain.addControllerMotion(forwardDrive);
				
				Robot.drivetrain.startMotion();
			}
			
			@Override
			State run() {
				if(Robot.drivetrain.isControllerFinished())
				return END;
				
				return super.run();
			}
		},
		END {
			@Override
			void init() {
				super.init();
			}
			
			@Override
			State run() {
				return State.LOOKING;
			}
		};

		void init() {

		}

		State run() {
			return this;
		}
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.LOOKING;
		state.init();
	}

	private State state;

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.oi.autoGearPlacement.get())
			state = State.LOOKING;

		else {
			State newState = state.run();

			if (state != newState) {
				state = newState;
				state.init();
			}
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
