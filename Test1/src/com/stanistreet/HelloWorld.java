package com.stanistreet;

import java.awt.geom.Point2D;

import lejos.nxt.*;

public class HelloWorld {

	public static boolean getMeOutOfHere = false;
	public static boolean allDone = false;
	
	public static void testSpeed() {
		MotorPort mpA = MotorPort.A;
		MotorPort mpB = MotorPort.B;
		
		mpA.resetTachoCount();
		mpB.resetTachoCount();
		
		float t0 = System.currentTimeMillis() / 1000F;
		
		mpA.controlMotor(100, MotorPort.FORWARD);
		mpB.controlMotor(100, MotorPort.FORWARD);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		float t1 = System.currentTimeMillis() / 1000F;
		
		float lA = mpA.getTachoCount() / 180F * 3.1415926F * RobotParameters.wheelRadius;
		float lB = mpB.getTachoCount() / 180F * 3.1415926F * RobotParameters.wheelRadius;
		
		mpA.controlMotor(0, MotorPort.FLOAT);
		mpB.controlMotor(0, MotorPort.FLOAT);
		
		System.out.println("v=" + 0.5 * (lA + lB) / (t1 - t0));
	}
	
	public static void testPID() {
		PIDController pid = new PIDController(MotorPort.A, MotorPort.B);
		
		Pose initialPose = new Pose(new Point2D.Float(0.0F, 0.0F), 0F);
		Pose finalPose = new Pose(new Point2D.Float(1.0F, 1.0F), 0F);
				
		CubicSpline spline = new CubicSpline(initialPose, 0.5F, finalPose, 0.5F);
		
		System.out.println("Line length" + spline.getLength());
		
		Kinematics kinematics = new Kinematics(spline, 0.0F, 0.0F, 50);
		
		float t0 = System.currentTimeMillis();
		KinematicPose current = kinematics.interpolatePose(0);
		pid.setTargetPose(current);
		
		int i = 0;

		pid.controlOn();
		
		while (!current.isFinished && !getMeOutOfHere) {
			i++;
			current = kinematics.interpolatePose((System.currentTimeMillis() - t0) / 1000F);
			pid.setTargetPose(current);
		}
		
		pid.controlOff();
		
		System.out.println("n=" + i + " t=" + current.t);
	}
	
	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonPressed(Button b) {
				System.out.println("Let's get out of here...");
				getMeOutOfHere = true;
			}

			@Override
			public void buttonReleased(Button b) {
				allDone = true;
			}
		});
		
		System.out.println("Hello World!");
		
		testPID();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (!allDone) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
