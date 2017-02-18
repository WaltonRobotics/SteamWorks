package org.usfirst.frc2974.Testbed.controllers;


public class MotionPathSpline extends MotionProvider{
	Motion goal;
	
	private Point2D[] controlPoints = new Point2D[4];
	private double length;
	private double angle_init;
	private double angle_fin;
	
	public MotionPathSpline(double vCruise, double aMax, Pose initial, double l0, Pose final_, double l1) {
		super(vCruise, aMax);
		this.controlPoints[0] = initial.X;
		this.controlPoints[1] = initial.offsetPoint(l0);
		this.controlPoints[2] = initial.offsetPoint(-l1);
		this.controlPoints[3] = final_.X;
		
		Point2D Xprev = evaluate(B(0));
		double length = 0;
		for(int i=1; i<=100; i++){
			double s = i/100;
			Point2D Xnext = evaluate(B(s));
			length += Xprev.distance(Xnext);
			Xprev = Xnext;
		}
		
		this.length = length;
		this.angle_init = initial.angle;
		this.angle_fin = final_.angle;
	}


	@Override
	public Pose evaluatePose(double s) {
		Point2D X = evaluate(B(s));
		Point2D dXds = evaluate(dBds(s));	
		double theta = Math.atan2(dXds.y, dXds.x);
		return new Pose(X, theta); //Find the values for v and a and position
	
		
	}
	
	private double[] B(double s){
		double[] result = new double[4];
		double r = 1 -s;
		result[0] = r * r * r;
		result[1] = 3 * r * r * s;
		result[2] = 3 * r * s * s;
		result[3] = s * s * s;
		return result;
	}
	
	@SuppressWarnings("unused")
	private double[] dBds(double s) {
		double[] result = new double[4];
		double r = 1 - s;
		result[0] = -3 * r * r;
		result[1] = 3 * r * r -6 * r * s;
		result[2] = -3 * s * s +6 * r * s;
		result[3] = 3 * s * s;
		return result;
	}
	
	private Point2D evaluate(double[] shape){
		Point2D result = new Point2D(0, 0);
		for(int i=0; i<4; i++) {
			result.x += shape[i] * controlPoints[i].x;
			result.y += shape[i] * controlPoints[i].y;
		}
		return result;
	}

	@Override
	public LimitMode getLimitMode() {
		return LimitMode.LimitLinearAcceleration;
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public double getInitialTheta() {
		// TODO Auto-generated method stub
		return angle_init;
	}

	@Override
	public double getFinalTheta() {
		// TODO Auto-generated method stub
		return angle_fin;
	}
	
	

}
