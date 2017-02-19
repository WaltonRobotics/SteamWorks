package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class Shoot extends Command {
	public enum State {
		Rest{
			@Override
			public void run(Shoot shoot) {
				if(Robot.oi.shoot.get()){
					shoot.state = MotorSpeedsUp;
				}
			}
		},
		MotorSpeedsUp {
			@Override
			public void run(Shoot shoot) {
				SmartDashboard.putBoolean("isShooterAtSpeed", false);
				if (Robot.shooter.isAtSpeed() || Robot.oi.atSpeed.get()) {
					shoot.state = Aiming;
				}
			}
		},
		Aiming {
			@Override
			public void run(Shoot shoot) {
				SmartDashboard.putBoolean("isShooterAtSpeed", true);
				if (Robot.aim.aimed() || Robot.oi.aimOverride.get()) {
					shoot.state = ReadyToShoot;
				}
			}

		},
		ReadyToShoot {
			@Override
			public void run(Shoot shoot) {
				SmartDashboard.putBoolean("isShooterAtSpeed", true);
				if (Robot.oi.shoot.get()) {
					shoot.state = Firing;
				}
			}

		},
		Firing {
			double time = -1;
			final double duration = 0.2;
			@Override
			public void run(Shoot shoot) {
				if(time == -1) time = Timer.getFPGATimestamp();
				if (Timer.getFPGATimestamp() - time < duration) {
					Robot.shooter.index(true);
				} else {
					time = -1;
					Robot.shooter.index(false);
					shoot.state = MotorSpeedsUp;
				}
			}

		};
		public void run(Shoot shoot) {
		}
		public String toString(){
			return name();
		}
	}

	private State state;

	public Shoot() {
		requires(Robot.shooter);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.Rest;
	}

	// Called repeatedly when this Command is scheduled to run
	boolean pressed = false;
	protected void execute() {
		SmartDashboard.putString("Shooter State", state.name());
		SmartDashboard.putNumber("Flywheel Speed", Robot.shooter.getSpeed());
		state.run(this);
		if(Robot.oi.right.getRawButton(5)){
			if(!pressed){
				SmartDashboard.putNumber("ShootSpeed", SmartDashboard.getNumber("ShootSpeed",Shooter.fSPEED)-20);
				pressed = true;
			}
		}else if(Robot.oi.right.getRawButton(4)){
			if(!pressed){
				SmartDashboard.putNumber("ShootSpeed", SmartDashboard.getNumber("ShootSpeed",Shooter.fSPEED)+20);
				pressed = true;
			}
		}else{
			pressed = false;
		}
		if(Robot.oi.shoot.get()){
			Robot.shooter.enable();
		}else{
			Robot.shooter.disable();
			state = State.Rest;
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
