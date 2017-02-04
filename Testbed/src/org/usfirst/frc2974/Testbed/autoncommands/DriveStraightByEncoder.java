package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.Motion;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightByEncoder extends Command implements MotionProvider{

	private Pose initialPose;
	private double l1;
	private double l2;
	private double l3;
	private double t0;
	private double t1;
	private double t2;
	private double t3;
	private double vCruise;
	private double aMax;
	public final double SETTLE_TIME = 1;
	private Drivetrain driveTrain;
	
	public DriveStraightByEncoder(double distance, double vCruise, double aMax) {
		requires(Robot.drivetrain);
		driveTrain = Robot.drivetrain;
		
		synchronized (this) {
			
			l3 = distance;
			this.vCruise = vCruise;
			this.aMax = aMax;
		
		}
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Created DriveStraightByEncoder");
	}
	
	@Override
	protected void initialize() {		
		
		synchronized (this) {
			
			initialPose = Robot.poseEstimator.getPose();
			t0 = Timer.getFPGATimestamp();
			
			double lAcc;
			double lDec;
			double lCruise;
					
			lAcc = vCruise*vCruise/(2*aMax);
			lDec = vCruise*vCruise/(2*aMax);
			lCruise = l3 - lAcc - lDec;
			if(lCruise >= 0) {
				l1 = lAcc;
				l2 = l1 + lCruise;
			
				t1 = vCruise/aMax + t0;
				t2 = lCruise/vCruise + t1;
				t3 = vCruise/aMax + t2;
			}else{
				l1 = l3/2;
				l2 = l3/2;
				t1 = Math.sqrt(l3/aMax + t0*t0);
				t2 = t1;
				t3 = Math.sqrt(l3/aMax + t1*t1) + t1;
			}
						
		}
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info(
				String.format("Calculated path: t0=%3.1f, t1=%3.1f, t2=%3.1f, t3=%3.1f, l0=%5.3f, l1=%5.3f, l2=%5.3f, l3=%5.3f",
						      t0, t1, t2, t3, initialPose.positionWheel.mean(), l1, l2, l3) 
		);
		driveTrain.setControllerMotion(this);
		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Command starts: Controller enabled = " + driveTrain.getControllerStatus());
	}

	@Override
	protected void execute() {		
		if(Timer.getFPGATimestamp() > t3 + SETTLE_TIME){
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
	
	public synchronized Motion getMotion(double time) {
		
		double pos;
		double vel;
		double acc;
		
		if(time > t3) {
			acc = 0;
			vel = 0;
			pos = l3;
		}else if(time > t2) {
			acc = -aMax;
			vel = acc*(time-t3);
			pos = l2+.5*acc*(time*time-t2*t2);
		}else if(time > t1) {
			acc = 0;
			vel = vCruise;
			pos = l1 + vCruise*(time-t1);
		}else if(time >t0) {
			acc = aMax;
			vel = acc*time;
			pos = .5*acc*(time*time-t0*t0);
		}else{
			acc = 0;
			vel = 0;
			pos = 0;
		}
		
		Motion motion = new Motion(initialPose.positionWheel.left + pos, vel, acc, 
				  initialPose.positionWheel.right + pos, vel, acc, time > t3); 
		
		
		return motion;
	}

}
