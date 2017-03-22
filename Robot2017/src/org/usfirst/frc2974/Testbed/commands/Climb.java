package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Climb extends Command {
	private enum State {
		Disabled {

			@Override
			public void init(Climb climb) {
				Robot.climber.endHold();
			}

			@Override
			public State run(Climb climb) {

				if (Robot.oi.startHold()) {
					return Climbing;
				}
				Robot.climber.set(Robot.oi.climbY(), false);

				return Disabled;
			}

		},
		Climbing {

			@Override
			public void init(Climb climb) {
				Robot.climber.hold();
				Robot.climber.startHold();
			}

			@Override
			public State run(Climb climb) {

				if (Robot.oi.toggleHold()) {
					Robot.climber.endHold();
					return Disabled;
				}

				if (Robot.oi.startHold()) {
					Robot.climber.hold();
				}
				
				Robot.climber.set(Robot.oi.climbY(), Robot.oi.climbBoost());

				return Climbing;
			}

		};

		public abstract void init(Climb climb);

		public abstract State run(Climb climb);
	}

	private State state;
	private Climber climber;

	public Climb() {
		// Use requires() here to declare subsystem dependencies
		climber = Robot.climber;
		requires(climber);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.Disabled;
		state.init(this);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		State newState = state.run(this);
		if (newState != state) {
			newState.init(this);
			state = newState;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.climber.hold();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
