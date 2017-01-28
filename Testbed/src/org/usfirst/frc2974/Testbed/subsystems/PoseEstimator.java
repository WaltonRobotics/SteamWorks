package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.controllers.PoseProvider;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PoseEstimator extends Subsystem implements PoseProvider{
	
	private Encoder encoderLeft = RobotMap.encoderLeft;
	private Encoder encoderRight = RobotMap.encoderRight;
	
	public double positionLeftWheel;
	public double positionRightWheel;
	public double x;
	public double y;
	public double angle;
	
	public void updatePose() {
		positionLeftWheel = encoderLeft.getDistance();
		positionRightWheel = encoderRight.getDistance();
	}
	
	public Pose getPose() {
		
		
		
		return new Pose(positionLeftWheel, positionRightWheel, x, y, angle);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
}
