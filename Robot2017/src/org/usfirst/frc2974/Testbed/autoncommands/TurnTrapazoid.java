package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnTrapazoid extends Command {
	public static final double vmax = 1; // Value is 1 because 1 is the max
											// velocity
	public double amax; // Max acceleration (one of the inputs)
	public double duration; // How long to run code (one of inputs)
	public double t1;
	public double t0;
	public double dtaccel;
	public double triTime;

	private enum State {
		ACC {
			@Override // For accelerating portion of movement - moves to next
						// state when done
			public void run(TurnTrapazoid d) {
				if (d.duration / 2 < Timer.getFPGATimestamp() - d.t0) {
					d.state = State.DEC;
					return;
				} else if (Timer.getFPGATimestamp() >= d.t1) {
					d.state = State.CONST;
					return;
				}

				double power = (Timer.getFPGATimestamp() - d.t0) / d.dtaccel;
				Robot.drivetrain.setSpeeds(d.direction.directionValue * power, -d.direction.directionValue * power);
			}
		},
		CONST {
			@Override // Constant velocity portion of motion
			public void run(TurnTrapazoid d) {
				if (d.duration - Timer.getFPGATimestamp() <= d.dtaccel) {
					d.state = State.DEC;
					return;
				}

				Robot.drivetrain.setSpeeds(d.direction.directionValue * vmax, -d.direction.directionValue * vmax);
			}
		},
		DEC {
			@Override // Decelerating portion of motion
			public void run(TurnTrapazoid d) {
				if (d.duration < Timer.getFPGATimestamp() - d.t0) {
					d.state = END;
					return;
				}

				double power = (d.duration - Timer.getFPGATimestamp()) / d.dtaccel;
				Robot.drivetrain.setSpeeds(d.direction.directionValue * power, -d.direction.directionValue * power);
			}
		},
		END {
			@Override // Sets speed to 0 and ends program
			public void run(TurnTrapazoid d) {
				Robot.drivetrain.setSpeeds(0, 0);
				d.end();
			}
		};
		public void run(TurnTrapazoid d) {
		}
	}

	public enum Direction {
		CLOCKWISE(1), ANTICLOCKWISE(-1);

		final int directionValue;

		Direction(int directionValue) {
			this.directionValue = directionValue;
		}
	}

	private State state;
	private Direction direction;
	public boolean isDashboard;

	public TurnTrapazoid(boolean isDashboard, double amax, double time, Direction direction) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.drivetrain);

		this.isDashboard = isDashboard;
		this.amax = amax;
		duration = time;
		this.direction = direction;
		t1 = vmax / amax;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (isDashboard) {
			amax = SmartDashboard.getNumber("amax", 0);
			duration = SmartDashboard.getNumber("duartion", 0);
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

	// hello billy
	// billy doesn't live here any more :(
	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return duration < Timer.getFPGATimestamp() - t0;// || state == State.END
														// ;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drivetrain.setSpeeds(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

	@Override
	public String toString() {
		return String.format("amax = %f, duration = %f", amax, duration);
	}
}
