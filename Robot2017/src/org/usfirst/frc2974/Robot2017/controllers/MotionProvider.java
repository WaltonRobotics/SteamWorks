package org.usfirst.frc2974.Robot2017.controllers;

public abstract class MotionProvider {
	protected Motion initialMotion;
	protected double vCruise;
	protected double aMax;

	protected MotionProvider(double vCruise, double aMax) {
		this.vCruise = vCruise;
		this.aMax = aMax;
	}

	abstract Motion getMotion(double time);

	abstract void initialized(double time, Motion motion);

	abstract double getFinalTime();
}
