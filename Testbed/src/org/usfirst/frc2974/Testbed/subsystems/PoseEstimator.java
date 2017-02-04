package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.controllers.PoseProvider;
import org.usfirst.frc2974.Testbed.logging.CSVWriter;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoseEstimator extends Subsystem implements PoseProvider{
	
	private Encoder encoderLeft = RobotMap.encoderLeft;
	private Encoder encoderRight = RobotMap.encoderRight;
	
	private double positionLeftWheel;
	private double positionRightWheel;
	private double x;
	private double y;
	private double angle;
	
	public PoseEstimator() {
		
		synchronized (this) {
			
			encoderLeft.setDistancePerPulse(0.005);
			encoderRight.setDistancePerPulse(0.005);

		}
		
		reset();
		RobotLoggerManager.setFileHandlerInstance("robot.subsystems").info("PoseEstimator is created.");
	}
	
	public synchronized void updatePose() {
		
		positionLeftWheel = encoderLeft.getDistance();
		positionRightWheel = encoderRight.getDistance();
		CSVWriter.getInstance("PoseEstimator_position").write(positionLeftWheel, positionRightWheel);
	}
	
	public synchronized Pose getPose() {
		
		return new Pose(positionLeftWheel, positionRightWheel, x, y, angle);
		
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
