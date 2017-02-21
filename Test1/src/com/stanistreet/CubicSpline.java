package com.stanistreet;

import java.awt.geom.Point2D;

public class CubicSpline extends Motion {
	
	private Point2D.Float[] controlPoints = new Point2D.Float[4];
	private float length;
	private float theta0;
	private float theta1;
	
	public CubicSpline(Pose initial, float l0, Pose final_, float l1) {
		this.controlPoints[0] = initial.X;
		this.controlPoints[1] = initial.offsetPoint(l0);
		this.controlPoints[2] = final_.offsetPoint(-l1);
		this.controlPoints[3] = final_.X;
		
		Point2D.Float Xprev = evaluate(B(0));
		float length = 0F;
		for (int i = 1; i <= 100; i++) {
			float s = (float)i / 100F;
			Point2D.Float Xnext = evaluate(B(s));
			length += (float)Xprev.distance(Xnext);
			Xprev = Xnext;
		}
		this.length = length;
		this.theta0 = initial.theta;
		this.theta1 = final_.theta;
	};
	
	private float[] B(float s) {
		float[] result = new float[4];
		float r = 1F - s;
		result[0] =  r * r * r;
		result[1] =  3F * r * r * s;
		result[2] =  3F * r * s * s;
		result[3] =  s * s * s ;
		return result;
	}
	
	private float[] dBds(float s) {
		float[] result = new float[4];
		float r = 1F - s;
		result[0] =  -3F * r * r;
		result[1] =   3F * r * r - 6F * r * s;
		result[2] =  -3F * s * s + 6F * r * s;
		result[3] =   3F * s * s;
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
		for (int i=0; i<4; i++) {
			result.x += shape[i] * controlPoints[i].x;
			result.y += shape[i] * controlPoints[i].y;
		}
		return result;
	}
	
	public float getLength() {
		return length;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitLinearAcceleration;
	}

	@Override
	public float getInitialTheta() {
		// Only needed for rotational limits
		return theta0;
	}

	@Override
	public float getFinalTheta() {
		// Only needed for rotational limits
		return theta1;
	}
}
