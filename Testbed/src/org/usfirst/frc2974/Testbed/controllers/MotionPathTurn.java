package org.usfirst.frc2974.Testbed.controllers;

public class MotionPathTurn extends MotionProvider{
	public Pose pose0;
	public Pose pose1;
	
	public MotionPathTurn(Pose pose0, double dAngle, double vCruise, double rotAccelMax){
		super(vCruise, rotAccelMax);
		this.pose0 = pose0;
		this.pose1 = new Pose(pose0.X, pose0.angle + boundAngle(dAngle));
	}

	@Override
	public Pose evaluatePose(double s) {
		double r = 1.0 - s;
		return new Pose(pose0.X, r * pose0.angle + s * pose1.angle);
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitRotationalAcceleration;
	}

	@Override
	public double getLength() {
		return 0;
	}

	@Override
	public double getInitialTheta() {
		return pose0.angle;
	}

	@Override
	public double getFinalTheta() {
		return pose1.angle;
	}
	
}
