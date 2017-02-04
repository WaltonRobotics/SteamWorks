package com.stanistreet;

import java.awt.geom.Point2D;

public class Kinematics {

	private QuinticSpline spline;
	private KinematicPoint last;
	private KinematicPoint next;
	
	private float s;
	private float v0;
	private float v1;
	private int nPoints;

	public static class KinematicState {
		public final float l;
		public final float v;
		public final float a;
		
		public KinematicState(float l, float v, float a) {
			this.l = l;
			this.v = v;
			this.a = a;
		}		
		
		public static KinematicState interpolate(KinematicState s1, float f1, KinematicState s2, float f2) {
			return new KinematicState(f1 * s1.l + f2 * s2.l, f1 * s1.v + f2 * s2.v, f1 * s1.a + f2 * s2.a);
		}
	}
	
	public class KinematicPoint {
		public final Point2D.Float X;
		public final Point2D.Float dXds;
		public final Point2D.Float d2Xds2;
		public final float kappa;
		public final KinematicState left;
		public final KinematicState right;
		public final float t;
		public final boolean isFinished;
		
		private KinematicPoint(Point2D.Float X, Point2D.Float dXds, Point2D.Float d2Xds2, 
							   float kappa, KinematicState left, KinematicState right, float t, boolean isFinished) {
			this.X = X;
			this.dXds = dXds;
			this.d2Xds2 = d2Xds2;
			this.kappa = kappa;
			this.left = left;
			this.right = right;
			this.t = t;
			this.isFinished = isFinished;
		}
		
		public KinematicState getCenter() {
			return KinematicState.interpolate(left, 0.5F, right, 0.5F);
		}
	}
	
	public Kinematics(QuinticSpline spline, float v0, float v1, int nPoints) {
		this.spline = spline;
		this.nPoints = nPoints;
		this.v0 = v0;
		this.v1 = v1;
		evaluateFirst();
		evaluateNext(1F / nPoints);
	}
		
	public void evaluateNext(float ds) {
		last = next;
		if (s + ds > 1.0F) {
			evaluateLast();
			return;
		}
		
		s += ds;
		
		Point2D.Float X = spline.evaluate(spline.shape(s, 0));
		Point2D.Float dXds = spline.evaluate(spline.shape(s, 1));
		Point2D.Float d2Xds2 =  spline.evaluate(spline.shape(s, 2));
		
		// Calculate next curvature and mean for interval
		float kappa = QuinticSpline.curvature(dXds, d2Xds2);

		// Estimate length assume we're going to move on a constant arc
		float dl = (float) last.X.distance(X);
		float fm = 1F - RobotParameters.width * (kappa + last.kappa) / 4F;
		float fp = 1F + RobotParameters.width * (kappa + last.kappa) / 4F;
		float lLeft = last.left.l + dl * fm;
		float lRight = last.right.l + dl * fp;
		float lCenter = 0.5F * (lLeft + lRight);
		
		// Bound velocity according to max speed that can be achieved on each wheel 
		float v = RobotParameters.vMax * Math.min(1F / fm, 1F / fp);
		float a = 0;
		
		// Bound bulk velocity to what can be achieved considering accelerations
		float vAccel = (float) Math.sqrt(v0 * v0 + RobotParameters.aMax * lCenter);
		float vDecel = (float) Math.sqrt(v1 * v1 - RobotParameters.aMax * (lCenter - spline.getLength()));	
		
		if (v > vAccel) {
			v = vAccel;
			a = RobotParameters.aMax;
		}
		else if (v > vDecel) {
			v = vDecel;
			a = -RobotParameters.aMax;
		}
		
		// Estimate time step
		float t = last.t + dl / (0.25F * (last.left.v + last.right.v + 2 * v));

		KinematicState left = new KinematicState(lLeft, v * fm, a * fm);
		KinematicState right = new KinematicState(lRight, v * fp, a * fp);
		next = new KinematicPoint(X, dXds, d2Xds2, kappa, left, right, t, false);
	}
	
	public void evaluateFirst() {
		s = 0F;
		Point2D.Float X = spline.evaluate(spline.shape(s, 0));
		Point2D.Float dXds = spline.evaluate(spline.shape(s, 1));
		Point2D.Float d2Xds2 =  spline.evaluate(spline.shape(s, 2));
		float kappa = QuinticSpline.curvature(dXds, d2Xds2);
		
		KinematicState left = new KinematicState(0, v0, 0);
		KinematicState right = new KinematicState(0, v0, 0);
		
		last = next = new KinematicPoint(X, dXds, d2Xds2, kappa, left, right, 0, false);
	}
	
	public void evaluateLast() {
		s = 1F;
		Point2D.Float X = spline.evaluate(spline.shape(s, 0));
		Point2D.Float dXds = spline.evaluate(spline.shape(s, 1));
		Point2D.Float d2Xds2 =  spline.evaluate(spline.shape(s, 2));
		float kappa = QuinticSpline.curvature(dXds, d2Xds2);
		
		KinematicState left = new KinematicState(last.left.l, v1, 0);
		KinematicState right = new KinematicState(last.right.l, v1, 0);
		
		next = new KinematicPoint(X, dXds, d2Xds2, kappa, left, right, last.t, true);
	}
	
	private Point2D.Float interpolatePoint(Point2D.Float X1, float f1, Point2D.Float X2, float f2) {
		return new Point2D.Float(f1 * X1.x + f2 * X2.x, f1 * X1.y + f2 * X2.y);
	}
	
	
	public KinematicPoint interpolate(float t) {
	
		if (t <= last.t) {
			return last;
		}
				
		while (t > next.t && !next.isFinished) {
			evaluateNext(1F / nPoints);
		}
		
		if (next.isFinished) {
			return next;
		}
		
		float dt = next.t - last.t;
		float p = (next.t - t) / dt;
		float q = (t - last.t) / dt;
		return new KinematicPoint(interpolatePoint(last.X, p, next.X, q),
								  interpolatePoint(last.dXds, p, next.dXds, q),
								  interpolatePoint(last.d2Xds2, p, next.d2Xds2, q),
								  p * last.kappa + q * next.kappa,				 
								  KinematicState.interpolate(last.left, p, next.left, q),
								  KinematicState.interpolate(last.right, p, next.right, q),
								  p * last.t + q * next.t,
				                  next.isFinished);
	}
	
}
