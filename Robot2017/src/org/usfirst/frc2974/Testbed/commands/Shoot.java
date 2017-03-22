package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Gamepad;
import org.usfirst.frc2974.Testbed.OI;
import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.subsystems.Shooter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class Shoot extends Command {
	public enum State {
		Rest {
			@Override
			public State run(Shoot shoot) {
				if (Robot.oi.shoot.get()) {
					Robot.shooter.enable();
					return MotorSpeedsUp;
				}
				return this;
			}

			@Override
			public void init(Shoot shoot) {
				Robot.shooter.disable();
				Robot.shooter.index(false); // FIXME Should be in disable()
			}
		},
		MotorSpeedsUp {
			@Override
			public State run(Shoot shoot) {
				if (!Robot.oi.shoot.get()) {
					return Rest;
				} else if (Robot.shooter.isAtSpeed() || Robot.oi.atSpeed.get()) {
					return Aiming;
				}
				return this;
			}

			@Override
			public void init(Shoot shoot) {
				Robot.shooter.index(false);
			}
		},
		Aiming {
			@Override
			public State run(Shoot shoot) {
				if (!Robot.oi.shoot.get()) {
					return Rest;
				}
				return Firing;
			}

			@Override
			public void init(Shoot shoot) {
			}

		},
		Firing {
			double time = -1;
			final double duration = 0.75;

			@Override
			public State run(Shoot shoot) {
				if (!Robot.oi.shoot.get()) {
					return Rest;
				} else if ((Timer.getFPGATimestamp() - time) > duration) {
					if (SmartDashboard.getBoolean("ShootTraps", true)) {
						return Trap;
					} else {
						return MotorSpeedsUp;
					}
				}
				return this;
			}

			@Override
			public void init(Shoot shoot) {
				Robot.shooter.index(true);
				time = Timer.getFPGATimestamp();
			}

		},
		Trap {
			final double duration = 0.5;
			double time;

			@Override
			public State run(Shoot shoot) {
				if (!Robot.oi.shoot.get()) {
					return Rest;
				} else if ((Timer.getFPGATimestamp() - time) > duration) {
					return MotorSpeedsUp;
				}
				return this;
			}

			@Override
			public void init(Shoot shoot) {
				time = Timer.getFPGATimestamp();
			}
		};

		public abstract void init(Shoot shoot);

		public abstract State run(Shoot shoot);

		public String toString() {
			return name();
		}
	}

	private State state;
	private Shooter shooter;

	public Shoot() {
		shooter = Robot.shooter;
		requires(shooter);
		SmartDashboard.putBoolean("ShootTraps", false);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.Rest;
		SmartDashboard.putString("Shooter State", state.name());
	}

	// Called repeatedly when this Command is scheduled to run
	boolean pressed = false;
	boolean isShootStart = false;

	protected void execute() {
		State newState = state.run(this);
		if (newState != state) {
			state = newState;
			SmartDashboard.putString("Shooter State", state.name());
			state.init(this);
		}
		if (Robot.oi.right.getRawButton(5) || Robot.oi.gamepad.getPOVButton(Gamepad.POV.E)) {
			if (!pressed) {
				shooter.incrementSpeed(50);
				pressed = true;
			}
		} else if (Robot.oi.right.getRawButton(4) || Robot.oi.gamepad.getPOVButton(Gamepad.POV.W)) {
			if (!pressed) {
				shooter.decrementSpeed(50);
				pressed = true;
			}
		} else {
			pressed = false;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.shooter.disable();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
