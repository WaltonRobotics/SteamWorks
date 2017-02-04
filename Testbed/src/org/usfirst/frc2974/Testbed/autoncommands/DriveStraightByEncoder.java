package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.Motion;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightByEncoder extends Command {

	public final double SETTLE_TIME = 1;
	private Drivetrain driveTrain;
	private MotionPathStraight motion;
	
	public DriveStraightByEncoder(double distance, double vCruise, double aMax) {
		requires(Robot.drivetrain);
		driveTrain = Robot.drivetrain;
		
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Created DriveStraightByEncoder");
	}
	
	@Override
	protected void initialize() {	
		double distance = SmartDashboard.getNumber("encoderDistance", 0);
		double speed = SmartDashboard.getNumber("encoderSpeed", 0);
		double acceleration = SmartDashboard.getNumber("encoderAccel", 0);
		motion = new MotionPathStraight(distance, speed, acceleration);
		
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info(motion.toString());
		
		driveTrain.setControllerMotion(motion);
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Command starts: Controller enabled = " + driveTrain.getControllerStatus());
	}

	@Override
	protected void execute() {		
		if(Timer.getFPGATimestamp() > motion.getFinalTime() + SETTLE_TIME){
			driveTrain.cancelMotion();
			RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").warning("Timed Out - motion stopped");

		}
	}

	@Override
	protected synchronized boolean isFinished() {
		return !driveTrain.getControllerStatus();
	}

	@Override
	protected void end() {		
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Command ends: Controller enabled = " + driveTrain.getControllerStatus());
	}

	@Override
	protected void interrupted() {	
		driveTrain.cancelMotion();
	}
}
