package org.usfirst.frc2974.Testbed.controllers;

import java.util.TimerTask;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;

import edu.wpi.first.wpilibj.Timer;

public class MotionProfileController{
	
	private double kV, kK, kA, kP;
	private MotionProvider m = null; 
	private PoseProvider p;
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
			PoseProvider pose, double period){
		
		p = pose;
		this.kV = kV;
		this.kK = kK;
		this.kA = kA;
		this.kP = kP;
		this.period = period;
		
		controller = new java.util.Timer();
		
		controller.schedule(new MPCTask(), 0L, (long)(period*1000));
						
	}
	
	public void free() {
		controller.cancel();
		RobotLoggerManager.setFileHandlerInstance("robot.controller").info("MPCTask is destroyed.");
	}
	
	public synchronized void setMotion(MotionProvider path) {
		if(m != null) {
			throw new RuntimeException("Can't set motion with existing motion.");
		}
		Pose pose = p.getPose();
		Motion motion = new Motion(pose.positionWheel.left, 0, 0, pose.positionWheel.right, 0, 0, false);
		m = path;
		m.initialized(Timer.getFPGATimestamp(), motion);
		isEnabled = true;
		
		System.out.println(String.format("Setting motion: constants are kV=%f, kK=%f, kA=%f, kP=%f", kV, kK, kA, kP));
		
	}
	
	
	public synchronized void cancel() {
		isEnabled = false;
		m = null;
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
			
			enabled = this.isEnabled && m != null;
		
		}
		
		if(enabled) {
			
			double time = Timer.getFPGATimestamp();
		
			Pose pose = p.getPose();
			Motion motion = m.getMotion(time);
			System.out.println("time:" + time+ " " + pose.positionWheel + " " + motion.position);
			synchronized (this) {
				//feed forward
				leftPower += (kV * motion.velocity.left + kK) + kA * motion.accel.left;
				rightPower += (kV * motion.velocity.right + kK) + kA * motion.accel.right;
				//feed back		
				leftPower +=	kP * (motion.position.left - pose.positionWheel.left);
				rightPower += kP * (motion.position.right - pose.positionWheel.right);
				
			}
			
			leftPower = Math.max(-1, Math.min(1, leftPower));
			rightPower = Math.max(-1, Math.min(1, rightPower));
		//	System.out.println(String.format("LP=%f,RP=%f", leftPower,rightPower));
			Robot.drivetrain.setSpeeds(leftPower, rightPower);
		
			if(motion.isDone) {
				m = null;
				isEnabled = false;
				Robot.drivetrain.setSpeeds(0, 0);
			}
			
		}
		
	}
	
}