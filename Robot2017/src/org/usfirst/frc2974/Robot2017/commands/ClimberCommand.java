package org.usfirst.frc2974.Robot2017.commands;

import org.usfirst.frc2974.Robot2017.Robot;
import org.usfirst.frc2974.Robot2017.commands.IntakeCommand.State;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimberCommand extends Command {
	private State state;
	public enum State {
		Climbing{
			@Override
			public void run(ClimberCommand climberCommand){
				if(Robot.oi.climbStop.get()) {
					Robot.climber.stopClimbing();
					climberCommand.state = State.NotClimbing;
				}
				Robot.climber.startClimbing();
			}
		},NotClimbing{
			@Override
			public void run(ClimberCommand climberCommand){
				if(Robot.oi.climbStart.get()) {
					Robot.climber.startClimbing();
					climberCommand.state = State.Climbing;
				}
					Robot.climber.stopClimbing();
			}
			
		};
		public void run(ClimberCommand climberCommand){
		}
	}
    public ClimberCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = State.NotClimbing;
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
    	end();
    }
}
