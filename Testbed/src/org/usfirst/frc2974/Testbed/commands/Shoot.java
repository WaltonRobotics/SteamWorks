package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 *
 */
public class Shoot extends Command {
	public enum State {
		MotorSpeedsUp{
			@Override
			public void run(Shoot shoot){
				if (Robot.shooter.isAtSpeed()){
					shoot.state = Aiming;
				}
			}
		}, Aiming{
			@Override
			public void run(Shoot shoot){
				if (Robot.aim.aimed()){
					shoot.state = ReadyToShoot;
				}
			}
			
		}, ReadyToShoot{
			@Override
			public void run(Shoot shoot){
				if (Robot.oi.shoot.get()){
					shoot.state = Firing;
					shoot.state.run(shoot);
				}
			}
			
		}, Firing{
			@Override
			public void run(Shoot shoot){
				if(Robot.oi.shoot.get()) {
					Robot.shooter.index(true);
				} else {
					Robot.shooter.index(false);
					shoot.state = MotorSpeedsUp;
				}
			}
			
		};
		public void run(Shoot shoot){
		}
	}
	
	private State state;

    public Shoot() {
    	requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = State.MotorSpeedsUp;
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
