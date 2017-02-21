package com.stanistreet;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.MotorPort;

public class PIDController {

	private float lLeft = 0;
	private float lRight = 0;

	private KinematicPose target;
	
	MotorPort leftMotorPort;
	MotorPort rightMotorPort;
	
	
	private class Regulator extends Thread {
		private boolean isActive;
			
		public Regulator(PIDController controller) {
			this.isActive = false;
		}
		
		public void run() {
			while (true) {
				setDistance(leftMotorPort.getTachoCount(), rightMotorPort.getTachoCount());
				
				if (isActive) {
					runPID();
				}
				
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException ie) {
					
				}
			}
		}
		
		public synchronized void setActive() {
			isActive = true;
		}
		
		public synchronized void setInactive() {
			isActive = false;
		}
	}
	
	Regulator regulator;
	
	public PIDController(MotorPort leftMotorPort, MotorPort rightMotorPort) {
		this.leftMotorPort = leftMotorPort;
		this.rightMotorPort = rightMotorPort;
		
		regulator = new Regulator(this);
		regulator.setDaemon(true);
		regulator.start();
		
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				controlOff();
			}

			@Override
			public void buttonReleased(Button b) {}
		});

		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				controlOn();
			}

			@Override
			public void buttonReleased(Button b) {}
		});
	}

	private synchronized void setDistance(int leftCount, int rightCount) {
		lLeft = leftCount / 180F * 3.1415926F * RobotParameters.wheelRadius;
		lRight = rightCount / 180F * 3.1415926F * RobotParameters.wheelRadius;
	}
	
	private int roundAndClipPower(float power) {
		return Math.max(Math.abs(Math.round(power)), 100);
	}
	
	private synchronized void runPID() {
		// Feed forward model 
		float leftPower = Math.signum(target.left.v) * RobotParameters.kFeedForwardConstant;
		float rightPower = Math.signum(target.right.v) * RobotParameters.kFeedForwardConstant;
		
		leftPower += RobotParameters.kFeedForwardVelocity * target.left.v;
		rightPower += RobotParameters.kFeedForwardVelocity * target.right.v;
		
		leftPower += RobotParameters.kFeedForwardAccel * target.left.a;
		rightPower += RobotParameters.kFeedForwardAccel * target.right.a;
		
		// Feed back		
		leftPower += RobotParameters.kFeedBackProportional * (target.left.l - lLeft);
		rightPower += RobotParameters.kFeedBackProportional * (target.right.l - lRight);

		leftMotorPort.controlMotor(roundAndClipPower(leftPower), (leftPower > 0) ? MotorPort.FORWARD : MotorPort.BACKWARD);
		rightMotorPort.controlMotor(roundAndClipPower(rightPower), (rightPower > 0) ? MotorPort.FORWARD : MotorPort.BACKWARD);
	}

	public synchronized void setTargetPose(KinematicPose target) {
		this.target = target;
	}
	
	public synchronized void controlOn() {
		regulator.setActive();
		System.out.println("Motors active");
	}
	
	public synchronized void controlOff() {
		regulator.setInactive();
		leftMotorPort.controlMotor(0, MotorPort.FLOAT);
		rightMotorPort.controlMotor(0, MotorPort.FLOAT);
		System.out.println("Motors inactive");
	}
	
}
