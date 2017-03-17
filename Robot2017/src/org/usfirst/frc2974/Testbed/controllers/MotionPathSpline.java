package org.usfirst.frc2974.Testbed.controllers;


public class MotionPathSpline extends MotionProvider{
	Motion goal;
	
	private Point2D[] controlPoints = new Point2D[4];
	private double length;
	private double angle0;
	private double angle1;
	private boolean isForwards;
	
	public MotionPathSpline(Pose initial, double l0, Pose final_, double l1, double vCruise, double aMax, boolean isForwards) {
		super(vCruise, aMax);
		this.controlPoints[0] = initial.X;
		this.controlPoints[1] = initial.offsetPoint(isForwards? l0:-l0);
		this.controlPoints[2] = final_.offsetPoint(isForwards? -l1:l1);
		this.controlPoints[3] = final_.X;
		
		Point2D Xprev = evaluate(B(0));
		double length = 0;
		for(int i=1; i<=100; i++){
			double s = (double)i/100.0;
			Point2D Xnext = evaluate(B(s));
			length += Xprev.distance(Xnext);
			Xprev = Xnext;
		}
		
		this.length = length;
		this.angle0 = initial.angle;
		this.angle1 = final_.angle;
		this.isForwards = isForwards;
		System.out.println(initial.toString());
		System.out.println(controlPoints[1].toString());
		System.out.println(controlPoints[2].toString());
		System.out.println(final_.toString());

	}


	@Override
	public Pose evaluatePose(double s) {
		Point2D X = evaluate(B(s));
		Point2D dXds = evaluate(dBds(s));
		double theta;
		if(isForwards){
			theta = Math.atan2(dXds.y, dXds.x);
		}
		else{
			theta = Math.atan2(-dXds.y, -dXds.x);
		}
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
		return isForwards? length: -length;
	}

	@Override
	public double getInitialTheta() {
		return angle0;
	}

	@Override
	public double getFinalTheta() {
		return angle1;
	}
	
	

}
