package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Gamepad;
import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.IntakeCommand.State;
import org.usfirst.frc2974.Testbed.subsystems.Climber;
import org.usfirst.frc2974.Testbed.subsystems.Intake;
import org.usfirst.frc2974.Testbed.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climb extends Command {
	private boolean pressed = false;
	private Climber climber;
	public Climb() {
		// Use requires() here to declare subsystem dependencies
		climber = Robot.climber;
		requires(climber);
	}
	public enum State {
		Holding {
			@Override
			public void run(Climb climb) {
				//if 2 buttons are pressed go normal
				if(Robot.oi.climbY() != 0) {
					Robot.climber.set(Robot.oi.climbY());
				}
				Robot.climber.setHold();
			}
		},
		NormalClimbing {
			@Override
			public void run(Climb climb) {
				Robot.climber.set(Robot.oi.climbY());
			}
		};
		public void run(Climb climb) {
		}
	}
	
	private State state;
	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.NormalClimbing;
		climber.setHold();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		
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
