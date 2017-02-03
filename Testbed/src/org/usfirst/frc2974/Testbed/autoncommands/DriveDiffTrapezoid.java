package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.logging.Mode;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDiffTrapezoid extends Command {
	public static final double vmax = 1; // Value is 1 because 1 is the max
											// velocity
	public double amax; // Max acceleration (one of the inputs)
	public double duration; // How long to run code (one of inputs)
	public double t1;
	public double t0;
	public double dtaccel;
	public double triTime;
	public double diffPercent;
	public DiffDirection diffDirection;

	private enum State {
		ACC {
			@Override // For accelerating portion of movement - moves to next
						// state when done
			public void run(DriveDiffTrapezoid d) {
				if (d.duration / 2 < Timer.getFPGATimestamp() - d.t0) {
					d.state = State.DEC;
					RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
					.info("Changing state to Deceleration because half time was reached");
					return;
				} else if (Timer.getFPGATimestamp() >= d.t1) {
					d.state = State.CONST;
					RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
					.info("Changing sate to Constant because max speed reached");
					return;
				}

				double power = (Timer.getFPGATimestamp() - d.t0) / d.dtaccel;
				if(d.diffDirection == DiffDirection.ANTICLOCKWISE){
					Robot.drivetrain.setSpeeds(power*d.diffPercent, power);
				}else{
					Robot.drivetrain.setSpeeds(power, power*d.diffPercent);
				}
			}
		},
		CONST {
			@Override // Constant velocity portion of motion
			public void run(DriveDiffTrapezoid d) {
				if (d.duration - Timer.getFPGATimestamp() <= d.dtaccel) {
					d.state = State.DEC;
					RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
					.info("Changing state to Deceleration beacuse there is a need to start decerating to reach 0 before end");
					
					return;
				}

				if(d.diffDirection == DiffDirection.ANTICLOCKWISE){
					Robot.drivetrain.setSpeeds(vmax*d.diffPercent, vmax);
				}else{
					Robot.drivetrain.setSpeeds(vmax, vmax*d.diffPercent);
				}
			}
		},
		DEC {
			@Override // Decelerating portion of motion
			public void run(DriveDiffTrapezoid d) {
				if(d.duration < Timer.getFPGATimestamp() - d.t0)
				{
					d.state = END;
					RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
					.info("Changing state to End beacuse the robot has reached time limit");
					
					return;
				}
				
				double power = (d.duration - Timer.getFPGATimestamp()) / d.dtaccel;
				if(d.diffDirection == DiffDirection.ANTICLOCKWISE){
					Robot.drivetrain.setSpeeds(power*d.diffPercent, power);
				}else{
					Robot.drivetrain.setSpeeds(power, power*d.diffPercent);
				}
			}
		},
		END {
			@Override // Sets speed to 0 and ends program
			public void run(DriveDiffTrapezoid d) {
				RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
						.info("Stopping the robot. Setting speeds to 0");
				Robot.drivetrain.setSpeeds(0, 0);
				d.end();
			}
		};
		public void run(DriveDiffTrapezoid d) {
		}
	}

	private State state;
	
	public enum DiffDirection{
		CLOCKWISE, ANTICLOCKWISE, CLOCKWISEBACK, ANTICLOCKWISEBACK
	}
	
	public DriveDiffTrapezoid(double amax, double time, double diffPercent, DiffDirection diffDirection) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.drivetrain);

		this.amax = amax;
		duration = time;
		t0 = Timer.getFPGATimestamp();
		t1 = vmax / amax;
		dtaccel = t1 - t0;
		triTime = (duration / 2) - t0;
		this.diffPercent = diffPercent;
		this.diffDirection = diffDirection;
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
		return state == State.END; // || duration < Timer.getFPGATimestamp() - t0;
	}

	// Called once after isFinished returns true
	protected void end() {
		RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
				.info("Drive straight for a duration of " + duration + " finished. Setting speeds to 0");
		Robot.drivetrain.setSpeeds(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
