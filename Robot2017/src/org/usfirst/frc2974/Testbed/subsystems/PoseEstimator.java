package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.controllers.Point2D;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.controllers.PoseProvider;
import org.usfirst.frc2974.Testbed.controllers.RobotPair;
import org.usfirst.frc2974.Testbed.logging.CSVWriter;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoseEstimator extends Subsystem implements PoseProvider{
	
	private Encoder encoderLeft = RobotMap.encoderLeft;
	private Encoder encoderRight = RobotMap.encoderRight;
	
	private RobotPair wheelPositions;
	private double angle;
	public static final double wheelDistance = .6477;
	private Point2D X;
	
	public PoseEstimator() {
		
		synchronized (this) {
			//0.0024936392
			X = new Point2D(0, 0);
			angle = 0;
			encoderLeft.setDistancePerPulse(0.0003979673);
			encoderRight.setDistancePerPulse(-0.0003979673);
			reset();
			wheelPositions =  new RobotPair(encoderLeft.getDistance(), encoderRight.getDistance());

		}
	
		//RobotLoggerManager.setFileHandlerInstance("robot.subsystems").info("PoseEstimator is created.");
	}
	
	public synchronized void updatePose() {
		wheelPositions =  new RobotPair(encoderLeft.getDistance(), encoderRight.getDistance());
		
		/*double sr = encoderRight.getDistance() - positionRightWheel;
		double sl = encoderLeft.getDistance() - positionLeftWheel;
		double s;
		boolean goesLeft = sl < sr;
		if(goesLeft){
			s = 1.5*sl;
		}else{
			s = 1.5*sr;
		}
		
		double r = wheelDistance;
		double theta = s/r;
		
		x += r*Math.cos(theta);
		y += r*Math.cos(theta);

		if(goesLeft){
			angle += theta;
		}else{
			angle -= theta;
		}
		*/
		/*positionMiddle = 0.5*(positionLeftWheel+positionRightWheel);
		rate = 0.5*(encoderLeft.getRate() + encoderRight.getRate());
		System.out.println(positionMiddle+","+rate+","+Robot.drivetrain.getSpeeds());
//		CSVWriter.getInstance("PoseEstimator_position").write(positionLeftWheel, positionRightWheel);
 		*/
	}
	
	public synchronized Pose getPose() {
		return new Pose(X, angle);
	}

	public synchronized RobotPair getWheelPositions() {
		return wheelPositions;
	}
	
	public synchronized void reset() {
		encoderLeft.reset();
		encoderRight.reset();
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	public synchronized void dumpSmartdashboardValues() {
		SmartDashboard.putNumber("EncoderLeft", encoderLeft.getDistance());
		SmartDashboard.putNumber("EncoderRight", encoderRight.getDistance());
		
		SmartDashboard.putNumber("VelocityLeft", encoderLeft.getRate());
		SmartDashboard.putNumber("VelocityRight", encoderRight.getRate());
	}
	
}
