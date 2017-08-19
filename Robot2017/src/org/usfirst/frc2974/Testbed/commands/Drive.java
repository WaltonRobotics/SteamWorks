package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain.Driver;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Command {

	private Drivetrain drivetrain = Robot.drivetrain;
	private double minThrottle= 0;

	public Drive() {
		drivetrain = Robot.drivetrain;
		requires(drivetrain);
	}

	protected void initialize() {
		drivetrain.setDriver(Robot.driverSelect.getSelected());
		
		updateMinThrottle();
	}
	
	private void updateMinThrottle()
	{
		minThrottle = Preferences.getInstance().getDouble("drivetrain.minThrottle", 0.3);
	}

	public double getLeftThrottle() {
		if (Math.abs(Robot.oi.left.getY()) < 0.3) {
			return 0;
		}
		return Robot.oi.left.getY();
	}

	public double getRightThrottle() {
		if (Math.abs(Robot.oi.right.getY()) < 0.3) {
			return 0;
		}
		return Robot.oi.right.getY();

	}
	
	public double getZThrottle() {
		if (Math.abs(Robot.oi.left.getZ()) < 0.3) {
			return 0;
		}
		return Robot.oi.right.getZ();

	}
	
	public double getTurn() {
		if (Math.abs(Robot.oi.right.getX()) < 0.1) {
			return 0;
		}
		return Robot.oi.right.getX();

	}

	public double getLeftTurn() {
		if (Math.abs(Robot.oi.left.getX()) < 0.1) {
			return 0;
		}
		return Robot.oi.left.getX();

	}

	private void tankDrive() {
		Robot.drivetrain.setSpeeds(-getLeftThrottle(), -getRightThrottle());
	}
	
	private void cheesyDrive() {
		double throttle = (-getLeftThrottle() + 1)/2;
		double forward = -getRightThrottle();
		double turn = getTurn();
		Robot.drivetrain.setSpeeds(throttle * (forward - turn), throttle * (forward + turn));
		//System.out.println("Cheesy Error Stuff: throttle: "+throttle+"; forward: "+forward+"; turn: "+turn+"; speeds: ("+RobotMap.left.get()+", "+(-RobotMap.right.get())+")");		
	}
	
	private void robertDrive() {
		double throttle = Math.pow((Robot.oi.left.getAxis(Joystick.AxisType.kZ) + 1)/2,2);
		
//		used for testing
//		updateMinThrottle();
		throttle = Math.max(minThrottle, throttle);
		
		double forward = -getLeftThrottle();
		double turn = getLeftTurn();
		Robot.drivetrain.setSpeeds(throttle * (forward + turn), throttle * (forward - turn));		
	}
	
	// Called repeatedly when this Command is scheduled to run
	//change
	protected void execute() {
		if (Robot.oi.shiftUp.get()||Robot.oi.shiftUpAlt.get())
				drivetrain.shiftUp();
		if (Robot.oi.shiftDown.get()||Robot.oi.shiftDownAlt.get())
				drivetrain.shiftDown();
		
		switch (drivetrain.getDriver()) {
		case Tank: 
			tankDrive();
			break;
		case Cheesy:
			cheesyDrive();
			break;
		case Robert:
			robertDrive();
			break;
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
	}
}
