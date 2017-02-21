package com.stanistreet;

public final class RobotParameters {

	static final float width = 0.12F;
	static final float vMax = 0.25F;
	static final float aMax = 1.0F;
	static final float alphaMax = 1.0F;
	
	static final float wheelRadius = 0.035F;
	
	static final float kFeedForwardVelocity = 50F / 0.5F;
	static final float kFeedForwardConstant = 50F;
	static final float kFeedForwardAccel = 10;
	static final float kFeedBackProportional = 1000;
	static final float kFeedBackIntegral = 0;
	static final float kFeedBackDifferential = 0;
}
