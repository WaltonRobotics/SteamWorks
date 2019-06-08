package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShifting extends Command{
	
	private double delayStart;
	private static boolean activated;
	
	public AutoShifting() {
		requires(Robot.drivetrain);
	}
	
	private Talon left = RobotMap.left;
	private Talon right = RobotMap.right;
	
	@Override
	protected void initialize() {
		
	}
	
	@Override
	protected void execute() {
		
	/**
	 * Tries to shift up if motors are going above 85% speed, and are within 10% of each other.
	 * Tries to shift down if motors go below 65% speed, or aren't within 10% of each other.
	 * If the driver chooses to manually shift, wait 5 seconds before attempting again.
	 */
		
		if(activated) {
			if(Robot.oi.shiftDown.get() || Robot.oi.shiftUp.get()) 
				delayStart = Timer.getFPGATimestamp();
		
			if(
					Math.abs(right.getSpeed() - left.getSpeed()) <= .1 && 
					(right.getSpeed() >= .85 && left.getSpeed() >= .85)
					&& (Timer.getFPGATimestamp() - delayStart) >= 5) {
			
				Robot.drivetrain.shiftUp();
			
			}else if(
					Math.abs(right.getSpeed() - left.getSpeed()) >= .1 || 
					(right.getSpeed() <= .65 || left.getSpeed() <= .65)
					&& (Timer.getFPGATimestamp() - delayStart) >= 5) {
			
				Robot.drivetrain.shiftDown();
			
			}
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	@Override
	protected void end() {
				
	}
	@Override
	protected void interrupted() {
		end();
	}
	
	public static void enable() {
		activated = true;
	}
	
	public static void disable() {
		activated = false;
	}
	
}
