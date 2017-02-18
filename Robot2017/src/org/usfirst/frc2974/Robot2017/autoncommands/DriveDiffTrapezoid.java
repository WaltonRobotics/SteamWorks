package org.usfirst.frc2974.Robot2017.autoncommands;

import org.usfirst.frc2974.Robot2017.Robot;
import org.usfirst.frc2974.Robot2017.logging.Mode;
import org.usfirst.frc2974.Robot2017.logging.RobotLoggerManager;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
				if (d.diffDirection == DiffDirection.ANTICLOCKWISE) {
					Robot.drivetrain.setSpeeds(power * d.diffPercent, power);
				} else if (d.diffDirection == DiffDirection.CLOCKWISE) {
					Robot.drivetrain.setSpeeds(power, power * d.diffPercent);
				} else if (d.diffDirection == DiffDirection.ANTICLOCKWISEBACK) {
					Robot.drivetrain.setSpeeds(-power, -power * d.diffPercent);
				} else {
					Robot.drivetrain.setSpeeds(-power * d.diffPercent, -power);
				}
			}
		},
		CONST {
			@Override // Constant velocity portion of motion
			public void run(DriveDiffTrapezoid d) {
				if (d.duration - Timer.getFPGATimestamp() <= d.dtaccel) {
					d.state = State.DEC;
					RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands").info(
							"Changing state to Deceleration beacuse there is a need to start decerating to reach 0 before end");

					return;
				}

				if (d.diffDirection == DiffDirection.ANTICLOCKWISE) {
					Robot.drivetrain.setSpeeds(vmax * d.diffPercent, vmax);
				} else if (d.diffDirection == DiffDirection.CLOCKWISE) {
					Robot.drivetrain.setSpeeds(vmax, vmax * d.diffPercent);
				} else if (d.diffDirection == DiffDirection.ANTICLOCKWISEBACK) {
					Robot.drivetrain.setSpeeds(-vmax, -vmax * d.diffPercent);
				} else {
					Robot.drivetrain.setSpeeds(-vmax * d.diffPercent, -vmax);
				}
			}
		},
		DEC {
			@Override // Decelerating portion of motion
			public void run(DriveDiffTrapezoid d) {
				if (d.duration < Timer.getFPGATimestamp() - d.t0) {
					d.state = END;
					RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, "robot.autoncommands")
							.info("Changing state to End beacuse the robot has reached time limit");

					return;
				}

				double power = (d.duration - Timer.getFPGATimestamp()) / d.dtaccel;
				if (d.diffDirection == DiffDirection.ANTICLOCKWISE) {
					Robot.drivetrain.setSpeeds(power * d.diffPercent, power);
				} else if (d.diffDirection == DiffDirection.CLOCKWISE) {
					Robot.drivetrain.setSpeeds(power, power * d.diffPercent);
				} else if (d.diffDirection == DiffDirection.ANTICLOCKWISEBACK) {
					Robot.drivetrain.setSpeeds(-power, -power * d.diffPercent);
				} else {
					Robot.drivetrain.setSpeeds(-power * d.diffPercent, -power);
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
	public boolean isDashboard;

	public enum DiffDirection {
		CLOCKWISE, ANTICLOCKWISE, CLOCKWISEBACK, ANTICLOCKWISEBACK
	}

	public DriveDiffTrapezoid(boolean isDashboard, double amax, double time, double diffPercent,
			DiffDirection diffDirection) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.drivetrain);

		this.isDashboard = isDashboard;
		this.amax = amax;
		duration = time;
		this.diffPercent = diffPercent;
		this.diffDirection = diffDirection;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (isDashboard) {
			amax = SmartDashboard.getNumber("amax", 0);
			duration = SmartDashboard.getNumber("duration", 0);
			diffPercent = SmartDashboard.getNumber("diffPercent", 0);
		}
		state = State.ACC;
		t0 = Timer.getFPGATimestamp();
		dtaccel = t1 - t0;
		triTime = (duration / 2) - t0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		state.run(this);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return duration < Timer.getFPGATimestamp() - t0;// || state == State.END
														// ;
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

	@Override
	public String toString() {
		return String.format("amax = %f, duration = %f, diffPercent = %f", amax, duration, diffPercent);
	}
}
