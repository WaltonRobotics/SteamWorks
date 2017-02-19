package org.usfirst.frc2974.Testbed.controllers;

public class MotionPathSpline extends MotionProvider {
	Motion goal;

	public MotionPathSpline(Motion goal, double vCruise, double aMax) {
		super(vCruise, aMax);
		this.goal = goal;
	}

	@Override
	Motion getMotion(double time) {

		return null;
	}

	@Override
	void initialized(double time, Motion motion) {

	}

	@Override
	double getFinalTime() {

		return 0;
	}

}
