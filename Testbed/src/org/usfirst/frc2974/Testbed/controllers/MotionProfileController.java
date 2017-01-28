package org.usfirst.frc2974.Testbed.controllers;

import java.util.TimerTask;

public class MotionProfileController{
	public double kV, kK, kA, kP;
	public MotionProvider m; 
	public PoseProvider p;
	public TimerTask control;
	
	public MotionProfileController(double kV, double kK, double kA, double kP, double time,
			MotionProvider motion, PoseProvider pose, double period, TimerTask controlTimer){
		m = motion;
		p = pose;
		this.kV = kV;
		this.kK = kK;
		this.kA = kA;
		this.kP = kP;
		period = MotionProfileController.calculate();
		controlTimer = control;
	}

	private static double calculate() {
		return 0;
	}

	
	
}
