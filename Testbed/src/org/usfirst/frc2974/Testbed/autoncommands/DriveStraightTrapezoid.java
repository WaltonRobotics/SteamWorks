package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightTrapezoid extends Command {
	public static final double vmax = 1;	//Value is 1 because 1 is the max velocity
	public double amax;   // Max acceleration (one of the inputs)
	public double duration;    // How long to run code (one of inputs)
	public double t1;
	public double t0;
	public double dtaccel;
	public double triTime;
	
	public enum State{
		ACC{
			@Override // For accelerating portion of movement - moves to next state when done
			public void run(DriveStraightTrapezoid d) {
				if(Timer.getFPGATimestamp() >= d.t1) {
					d.state_trap = State.CONST;
					return;
				}
				
				double power = (Timer.getFPGATimestamp() - d.t0) / d.dtaccel;
				Robot.drivetrain.setSpeeds(power, power);
			}
		},CONST{ 
			@Override // Constant velocity portion of motion
			public void run(DriveStraightTrapezoid d) {
				if(d.duration - Timer.getFPGATimestamp() <= d.dtaccel) {
					d.state_trap = State.DEC; 
					return;
				}
				Robot.drivetrain.setSpeeds(vmax, vmax);
			}
		}
		,DEC{
			@Override // Decelerating portion of motion
			public void run(DriveStraightTrapezoid d){
				double power = (d.duration - Timer.getFPGATimestamp()) / d.dtaccel;
				Robot.drivetrain.setSpeeds(power, power);
			}
		}
		,ACC_TRI{
			@Override
			public void run(DriveStraightTrapezoid d){
				if(Timer.getFPGATimestamp() > d.triTime){
					d.state_tri = State.DEC_TRI;
					return;
				}
				double power = (Timer.getFPGATimestamp() - d.t0) / d.triTime;
				Robot.drivetrain.setSpeeds(power, power);
			}
		}
		,DEC_TRI{
			@Override
			public void run(DriveStraightTrapezoid d){
				double power = (d.duration - Timer.getFPGATimestamp()) / d.triTime;
				Robot.drivetrain.setSpeeds(power, power);
			}
		}
		,END{
			@Override // Sets speed to 0 and ends program
			public void run(DriveStraightTrapezoid d){
				Robot.drivetrain.setSpeeds(0, 0);
				d.end();
			}
		};
		public void run(DriveStraightTrapezoid d) {
		}
	}
	
	private State state_trap;
	private State state_tri;
	private State end;

    public DriveStraightTrapezoid(double amax, double time) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.drivetrain);
         
         this.amax = amax;
         duration = time;
         t0 = Timer.getFPGATimestamp();
         t1 = vmax/amax;
         dtaccel = t1 - t0;
         triTime = (duration / 2) - t0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state_trap = State.ACC;
    	state_tri = State.ACC_TRI;
    	end = State.END;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(triTime < dtaccel){
    		state_tri.run(this);
    	}
    	else{
    		state_trap.run(this);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return duration < Timer.getFPGATimestamp() - t0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	end.run(this);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
