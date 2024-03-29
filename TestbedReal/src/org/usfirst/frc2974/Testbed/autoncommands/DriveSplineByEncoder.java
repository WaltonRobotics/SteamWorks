package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.MotionPathSpline;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;
import org.usfirst.frc2974.Testbed.subsystems.PoseEstimator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSplineByEncoder extends Command{
	public final double SETTLE_TIME = 1;
	private Drivetrain driveTrain;
	private PoseEstimator poseEstimator;
	private MotionProvider motion;
	private boolean isDashboard;
	public double distance;
	public double speed;
	public double acceleration;
	public double angle;
	private double finishedTime;
	private boolean motionFinished;
	
	public DriveSplineByEncoder(boolean isDashboard,double distance, double speed, double acceleration, double angle) {
		requires(Robot.drivetrain);
		driveTrain = Robot.drivetrain;
		poseEstimator = Robot.poseEstimator;
		this.isDashboard = isDashboard;
		this.distance = distance;
		this.speed = speed;
		this.acceleration = acceleration;
		this.angle = angle;
		
		//RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Created DriveStraightByEncoder");
	}
	
	@Override
	protected void initialize() {	
		if(isDashboard){
			distance = SmartDashboard.getNumber("encoderDistance", 0);
			speed = SmartDashboard.getNumber("encoderSpeed", 0);
			acceleration = SmartDashboard.getNumber("encoderAccel", 0);
			angle = SmartDashboard.getNumber("encoderAngle", 0);
		}

		System.out.println(String.format("Distance=%f, Speed=%f, Accel=%f", distance, speed, acceleration));
		motionFinished = false;
		Pose init = poseEstimator.getPose();
		Pose final_ = new Pose(new Point2D(init.X.x + distance, init.X.y + distance), angle);
		motion = new MotionPathSpline(init, distance / 2, final_, distance/2, speed, acceleration);
		
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
	
	public static DriveSplineByEncoder driveToBoiler(){
		return new DriveSplineByEncoder(false, SmartDashboard.getNumber("Camera distance goal", 0), 0.25, 0.25, 
				SmartDashboard.getNumber("Camera angle goal", 0));
	}

	@Override
	public String toString(){
		return String.format("acceleration = %f,distance = %f,speed = %f",acceleration, distance, speed);
	}
}
