package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;

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
				if (!Robot.oi.autoGearPlacement.get() && SmartDashboard.getNumber(CAMERA_CENTER_X_KEY, 0) == 0)
					return super.run();

				return State.MOVE_TO_PEG;
			}
		},
		MOVE_TO_PEG {
			@Override
			State run() {
				// TODO move the robot to pegh using spline
				// TODO figure out how to use the distance parameter
				return super.run();
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

			@Override
			void init() {
				startTime = Timer.getFPGATimestamp();
			}

			@Override
			State run() {

				return super.run();
			}
		},
		BACK_UP {
			@Override
			State run() {
				// TODO Moves the robot away from the peg
				return super.run();
			}
		},
		END {
			@Override
			State run() {
				// TODO Auto-generated method stub
				return State.LOOKING;
			}
		};

		void init() {

		}

		State run() {
			return this;
		}

	}

	public AutoGearPlacement() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
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

	// Called once after isFinished returns true
	protected void end() {

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
