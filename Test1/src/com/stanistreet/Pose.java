package com.stanistreet;

import java.awt.geom.Point2D;

public class Pose {

	public final Point2D.Float X;
	public final float theta;
	
	public Pose(Point2D.Float X, float theta) {
		this.X = X;
		this.theta = theta;
	}
	
	public Point2D.Float offsetPoint(float l) {
		return new Point2D.Float(X.x + l * (float)Math.cos(theta), X.y + l * (float)Math.sin(theta));
	}
	
	public static Pose interpolate(Pose pose0, float p, Pose pose1, float q) {
		return new Pose(new Point2D.Float(p * pose0.X.x + q * pose1.X.x, p * pose0.X.y + q * pose1.X.y), 
				        p * pose0.theta + q * pose1.theta);
	}
}
