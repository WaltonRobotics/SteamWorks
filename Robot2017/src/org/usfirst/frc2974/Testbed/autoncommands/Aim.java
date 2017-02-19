package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Aim extends Command {

	public enum State {
		AIMING {
			@Override
			public void run(Aim aim) {
				if (/* aimed */false) {
					aim.state = AIMED;
				}
			}
		},
		AIMED {
			@Override
			public void run(Aim aim) {
				if (/* moved */false) {
					aim.state = AIMING;
				}
			}
		};

		public void run(Aim aim) {
		}
	}

	private State state;

	public Aim() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
//		state = state.AIMING;
		state = state.AIMED;	//FIXME
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		state.run(this);
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
	}

	public boolean aimed() {
		return state == State.AIMED;
	}
}
