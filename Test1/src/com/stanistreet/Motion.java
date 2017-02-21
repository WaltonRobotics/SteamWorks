package com.stanistreet;

public abstract class Motion {
	public enum LimitMode {
		LimitLinearAcceleration, LimitRotationalAcceleration
	}
	
	public abstract Pose evaluatePose(float s);
	
	public abstract float getLength();
	
	public abstract float getInitialTheta();
	
	public abstract float getFinalTheta();
	
	public abstract LimitMode getLimitMode();
	
	public static float boundTheta(float theta) {
		if (theta > (float)Math.PI) {
			return theta - (float)Math.PI;
		}
		if (theta < -(float)Math.PI) {
			return theta + (float)Math.PI;
		}
		return theta;
	}
}
