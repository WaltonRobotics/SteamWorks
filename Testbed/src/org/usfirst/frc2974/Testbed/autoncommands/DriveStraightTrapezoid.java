package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightTrapezoid extends Command {
	public static final double vmax = 7;	//We should make this a real number hehehe
	public final double amax;
	public final double duration;
	public final double t1;
	public final double t0;
	
	public enum State{
		ACC{
			@Override
			public void run(DriveStraightTrapezoid d) {
				if(Timer.getFPGATimestamp() >= d.t1) {
					d.state = State.CONST;
					return;
				}
				
				double power = (Timer.getFPGATimestamp() - d.t0) / (d.t1 - d.t0);
				Robot.drivetrain.setSpeeds(power, power);
			}
		},CONST{
			@Override
			public void run(DriveStraightTrapezoid d) {
				if(d.duration - Timer.getFPGATimestamp() <= d.t1 - d.t0) {
					d.state = State.DEC; 
				}
				Robot.drivetrain.setSpeeds(1, 1);
			}
		}
		,DEC{
			@Override
			public void run(DriveStraightTrapezoid d){
				double power = (d.duration - Timer.getFPGATimestamp() - d.t0) / (d.t1 - d.t0);
				Robot.drivetrain.setSpeeds(power, power);
			}
		};
		public void run(DriveStraightTrapezoid d) {
		}
	}
	
	private State state;

    public DriveStraightTrapezoid(double amax, double time) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.drivetrain);
         
         this.amax = amax;
         duration = time;
         t0 = Timer.getFPGATimestamp();
         t1 = vmax/amax + t0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = State.ACC;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	state.run(this);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return duration == Timer.getFPGATimestamp() - t0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.setSpeeds(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
