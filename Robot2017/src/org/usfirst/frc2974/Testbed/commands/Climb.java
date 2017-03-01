package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Gamepad;
import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.IntakeCommand.State;
import org.usfirst.frc2974.Testbed.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climb extends Command {
	boolean pressed = false;
	public Climb() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.climber);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		SmartDashboard.putNumber("ClimberPower", RobotMap.climber.get());
			if (Robot.oi.gamepad.getRawButton(7)){
				Robot.climber.hold();
			}else{
				Robot.climber.set(Robot.oi.climbY());
			}
			if(Robot.oi.gamepad.getPOVButton(Gamepad.POV.N)){
				if(!pressed){
					SmartDashboard.putNumber("ClimberHold", SmartDashboard.getNumber("ClimberHold",0)+0.05);
					pressed = true;
				}
			}else if(Robot.oi.gamepad.getPOVButton(Gamepad.POV.S)){
				if(!pressed){
					SmartDashboard.putNumber("ClimberHold", SmartDashboard.getNumber("ClimberHold",0)-0.05);
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
