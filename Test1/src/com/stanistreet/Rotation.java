package com.stanistreet;

import java.awt.geom.Point2D;

public class Rotation extends Motion {

	private Point2D.Float X;
	private float theta0;
	private float theta1;
	
	public Rotation(Pose initial, float dtheta) {
		this.X = initial.X;
		this.theta0 = initial.theta;
		this.theta1 = initial.theta + dtheta;
	}

	@Override
	public Pose evaluatePose(float s) {
		float r = 1F - s;
		float theta =  0.5F * (r * theta0 + s * theta1);
		return new Pose(X, theta); 
	}
	
	@Override
	public float getLength() {
		return 0;
	}

	@Override
	public float getInitialTheta() {
		return theta0;
	}

	@Override
	public float getFinalTheta() {
		return theta1;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitRotationalAcceleration;
	}
}
