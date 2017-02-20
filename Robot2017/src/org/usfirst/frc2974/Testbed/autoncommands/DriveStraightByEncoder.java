package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.Motion;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionPathTurn;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;
import org.usfirst.frc2974.Testbed.subsystems.PoseEstimator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightByEncoder extends Command {

	public final double SETTLE_TIME = 1;
	private Drivetrain driveTrain;
	private PoseEstimator poseEstimator;
	private MotionProvider motion;
	private boolean isDashboard;
	public double distance;
	public double speed;
	public double acceleration;
	private double finishedTime;
	private boolean motionFinished;
	
	public DriveStraightByEncoder(boolean isDashboard,double distance, double speed, double acceleration) {
		requires(Robot.drivetrain);
		driveTrain = Robot.drivetrain;
		poseEstimator = Robot.poseEstimator;
		this.isDashboard = isDashboard;
		this.distance = distance;
		this.speed = speed;
		this.acceleration = acceleration;
		
		//RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Created DriveStraightByEncoder");
	}
	
	@Override
	protected void initialize() {	
		if(isDashboard){
			distance = SmartDashboard.getNumber("encoderDistance", 1.57);
			speed = SmartDashboard.getNumber("encoderSpeed", 0.25);
			acceleration = SmartDashboard.getNumber("encoderAccel", 0.25);
		}

		System.out.println(String.format("Distance=%f, Speed=%f, Accel=%f", distance, speed, acceleration));
		motionFinished = false;
		motion = new MotionPathStraight(poseEstimator.getPose(), distance, speed, acceleration);

		driveTrain.addControllerMotion(motion);
		System.out.println(motion.toString());
		System.out.println("Command starts: Controller enabled = " + driveTrain.getControllerStatus());
		driveTrain.startMotion();
	}

	@Override
	protected void execute() {		
		if(!motionFinished && driveTrain.isControllerFinished()){
			finishedTime = Timer.getFPGATimestamp();
			motionFinished = true;
		}
	}

	@Override
	protected synchronized boolean isFinished() {
		return motionFinished && (Timer.getFPGATimestamp() - finishedTime) > SETTLE_TIME;
	}

	@Override
	protected void end() {		
		System.out.print("Command ends: Controller enabled = " + driveTrain.getControllerStatus());
		driveTrain.cancelMotion();
	}

	@Override
	protected void interrupted() {	
		end();
	}

	@Override
	public String toString(){
		return String.format("acceleration = %f,distance = %f,speed = %f",acceleration, distance, speed);
	}
}
