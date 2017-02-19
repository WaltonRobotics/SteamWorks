package org.usfirst.frc2974.Testbed.controllers;

import java.util.ArrayDeque;
import java.util.TimerTask;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;

import edu.wpi.first.wpilibj.Timer;

public class MotionProfileController{
	
	private double kV, kK, kA, kP;
	private Kinematics currentKinematics = null;
	private KinematicPose staticKinematicPose;
	private ArrayDeque<MotionProvider> motions = new ArrayDeque<MotionProvider>();
	private PoseProvider poseProvider;
	private java.util.Timer controller;
	private double period;
	private boolean isEnabled;
	
	private class MPCTask extends TimerTask{
		
		@Override
		public void run() {
			calculate();
		}
		
	}
	
	public MotionProfileController(double kV, double kK, double kA, double kP,
			PoseProvider poseProvider, double period){
		
		this.poseProvider = poseProvider;
		this.kV = kV;
		this.kK = kK;
		this.kA = kA;
		this.kP = kP;
		this.period = period;
		
		controller = new java.util.Timer();
		
		controller.schedule(new MPCTask(), 0L, (long)(period*1000));
	
		staticKinematicPose = Kinematics.staticPose(poseProvider.getPose(), poseProvider.getWheelPositions(), Timer.getFPGATimestamp());
	}
	
	public void free() {
		controller.cancel();
		RobotLoggerManager.setFileHandlerInstance("robot.controller").info("MPCTask is destroyed.");
	}
	
	public synchronized void setMotion(MotionProvider motion) {
		motions.add(motion);
	}
	
	public synchronized void enable() {
		if (!motions.isEmpty()) {
			MotionProvider newMotion = motions.pop();
			currentKinematics = new Kinematics(newMotion, poseProvider.getWheelPositions(), Timer.getFPGATimestamp(), 0, 0, nPoints);
			isEnabled = true;
		}
	}
	
	public synchronized void cancel() {
		isEnabled = false;
		currentKinematics = null;
		motions.clear();
		Robot.drivetrain.setSpeeds(0, 0);
	}
	
	public boolean getEnabled() {
		return isEnabled;
	}
	
	public synchronized double getKV() {
		return kV;
	}
	
	public synchronized void setKV(double kV) {
		this.kV = kV;
	}	

	public synchronized double getKK() {
		return kK;
	}
	
	public synchronized void setKK(double kK) {
		this.kK = kK;
	}
	
	public synchronized double getKA() {
		return kA;
	}
	
	public synchronized void setKA(double kA) {
		this.kA = kA;
	}
		
	public synchronized double getKP() {
		return kP;
	}
	
	public synchronized void setKP(double kP) {
		this.kP = kP;
	}
	
	private void calculate() {
		
		double leftPower = 0;
		double rightPower = 0;
		boolean enabled;
		
		synchronized (this) {
			enabled = this.isEnabled;
		}
		
		if(enabled) {
			
			double time = Timer.getFPGATimestamp();
		
			RobotPair wheelPositions = poseProvider.getWheelPositions();
			
			KinematicPose kinematicPose;
			if (currentKinematics != null) {
				kinematicPose = currentKinematics.interpolatePose(time);
			} else {
				kinematicPose = staticKinematicPose;
			}
			
			//System.out.println("time:" + time+ " " + pose.positionWheel + " " + kinematicPose.);
			synchronized (this) {
				//feed forward
				leftPower += (kV * kinematicPose.left.v + kK) + kA * kinematicPose.left.a;
				rightPower += (kV * kinematicPose.right.v + kK) + kA * kinematicPose.right.a;
				//feed back		
				leftPower +=	kP * (kinematicPose.left.l - pose.positionWheel.left);
				rightPower += kP * (kinematicPose.right.l - pose.positionWheel.right);
				
			}
			
			leftPower = Math.max(-1, Math.min(1, leftPower));
			rightPower = Math.max(-1, Math.min(1, rightPower));
		//	System.out.println(String.format("LP=%f,RP=%f", leftPower,rightPower));
			Robot.drivetrain.setSpeeds(leftPower, rightPower);
		
			if(kinematicPose.isFinished) {
				if (!motions.isEmpty()) {
					MotionProvider newMotion = motions.pop();
					currentKinematics = new Kinematics(newMotion, currentKinematics.getWheelPositions(), currentKinematics.getTime(), 0, 0, nPoints);
				}
				else {
					currentKinematics = null;
					staticKinematicPose = Kinematics.staticPose(currentKinematics.getPose(), currentKinematics.getWheelPositions(), currentKinematics.getTime());
				}
			}			
		}
	}
}