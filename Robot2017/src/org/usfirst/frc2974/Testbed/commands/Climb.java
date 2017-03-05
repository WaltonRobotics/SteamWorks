package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Gamepad;
import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.IntakeCommand.State;
import org.usfirst.frc2974.Testbed.subsystems.Climber;
import org.usfirst.frc2974.Testbed.subsystems.Shooter;

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

	// Called just before this Command runs the first time
	protected void initialize() {
		climber.setHold();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.oi.isHold()) {
			Robot.climber.hold();
		}else{
			Robot.climber.set(Robot.oi.climbY());
		}
		if(Robot.oi.gamepad.getPOVButton(Gamepad.POV.N)){
			if(!pressed){
				climber.incrementHold();
				pressed = true;
			}
		}else if(Robot.oi.gamepad.getPOVButton(Gamepad.POV.S)){
			if(!pressed){
				climber.decrementHold();
				pressed = true;
			}
		}else{
			pressed = false;
		}
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
