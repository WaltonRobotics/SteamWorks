package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Climb extends Command {
	private enum State{
		Disabled{

			@Override
			public void init(Climb climb) {
				Robot.climber.endHold();
			}

			@Override
			public State run(Climb climb) {

				
				if(Robot.oi.startClimb()) {		return Climbing;	}
				return Disabled;
			}
			
		},
		Climbing{

			@Override
			public void init(Climb climb) {
				Robot.climber.set(0);
			}

			@Override
			public State run(Climb climb) {

				
				if(Robot.climber.isHolding){
					Robot.climber.set(Math.max(Robot.climber.getCurrentHoldValue()+Robot.oi.climbY(),-1));
				} else {
					Robot.climber.set(Robot.oi.climbY());
				}
				
				if(Robot.oi.startHold()) {
					Robot.climber.startHold();
				}
				
				if(Robot.oi.endHold()) {
					Robot.climber.endHold();
				}
				
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
		if(newState != state) {
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
