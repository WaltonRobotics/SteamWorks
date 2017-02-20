package com.stanistreet;

import java.awt.geom.Point2D;

public class Linear extends Motion {

	private Point2D.Float[] controlPoints = new Point2D.Float[2];
	private float length;
	private float theta;

	public Linear(Pose initial, float length) {
		controlPoints[0] = initial.X;
		controlPoints[1] = initial.offsetPoint(length);
		this.length = length;
		this.theta = initial.theta;
	}
	
		
	private float[] B(float s) {
		float[] result = new float[2];
		float r = 1F - s;
		result[0] =  r;
		result[1] =  s;
		return result;
	}
	
	private float[] dBds(float s) {
		float[] result = new float[2];
		result[0] =  -1F;
		result[1] =   1F;
		return result;
	}

	@Override
	public Pose evaluatePose(float s) {
		Point2D.Float X = evaluate(B(s));
		Point2D.Float dXds = evaluate(dBds(s));	
		float theta = (float) Math.atan2(dXds.y, dXds.x);
		return new Pose(X, theta);
	}
	
	private Point2D.Float evaluate(float[] shape) {
		Point2D.Float result = new Point2D.Float();
		for (int i=0; i<2; i++) {
			result.x += shape[i] * controlPoints[i].x;
			result.y += shape[i] * controlPoints[i].y;
		}
		return result;
	}

	@Override
	public float getLength() {
		return length;
	}

	@Override
	public float getInitialTheta() {
		return theta;
	}

	@Override
	public float getFinalTheta() {
		return theta;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitLinearAcceleration;
	}
}
