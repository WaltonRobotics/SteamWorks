package com.stanistreet;

import java.awt.geom.Point2D;
import java.util.Iterator;

public class QuinticSpline {
	
	private Point2D.Float [] controlPoints = new Point2D.Float[6];
	private float length;
	
	public QuinticSpline(Point2D [] controlPoints) {
		if (controlPoints.length != 6) {
			throw new IllegalArgumentException("Control points array should have 6 elements");
		}
		
		for (int i = 0; i < 6; i++) {
			this.controlPoints[i] = new Point2D.Float();
			this.controlPoints[i].setLocation(controlPoints[i]);
		}
		
		Point2D.Float X0 = evaluate(B(0));
		float length = 0F;
		for (int i = 1; i <= 100; i++) {
			float s = (float)i / 100F;
			Point2D.Float X1 = evaluate(B(s));
			length += (float) X0.distance(X1);
			X0 = X1;
		}
		this.length = length;
	};
	
	public float[] shape(float s, int order) {
		switch (order) {
		case 0:
			return B(s);
		case 1:
			return dBds(s);
		case 2:
			return d2Bds2(s);
		default:
			throw new IllegalArgumentException("Order must be 0, 1 or 2");
		}
	}
	
	private float[] B(float s) {
		float[] result = new float[6];
		float r = 1F - s;
		result[0] =  1F * r * r * r * r * r;
		result[1] =  5F * r * r * r * r * s;
		result[2] = 10F * r * r * r * s * s;
		result[3] = 10F * r * r * s * s * s;
		result[4] =  5F * r * s * s * s * s;
		result[5] =  1F * s * s * s * s * s;
		return result;
	}
	
	private float[] dBds(float s) {
		float[] result = new float[6];
		float r = 1F - s;
		result[0] =  -5F * r * r * r * r;
		result[1] =   5F * r * r * r * r - 20F * r * r * r * s;
		result[2] =  20F * r * r * r * s - 30F * r * r * s * s;
		result[3] = -20F * r * s * s * s + 30F * r * r * s * s;
		result[4] =  -5F * s * s * s * s + 20F * r * s * s * s;
		result[5] =   5F * s * s * s * s;
		return result;
	}
	
	private float[] d2Bds2(float s) {
		float[] result = new float[6];
		float r = 1F - s;
		result[0] =  20F * r * r * r;
		result[1] = -40F * r * r * r +  60F * r * r * s;
		result[2] =  20F * r * r * r - 120F * r * r * s + 60F * r * s * s;
		result[3] =  20F * s * s * s - 120F * r * s * s + 60F * r * r * s;
		result[4] = -40F * s * s * s +  60F * r * s * s;
		result[5] =  20F * s * s * s;
		return result;
	}
	
	public Point2D.Float evaluate(float[] shape) {
		if (shape.length != 6) {
			throw new IllegalArgumentException("Shape array should have 6 elements");
		}
		
		Point2D.Float result = new Point2D.Float();
		for (int i=0; i<6; i++) {
			result.x += shape[i] * controlPoints[i].x;
			result.y += shape[i] * controlPoints[i].y;
		}
		return result;
	}
	
	public static float curvature(Point2D.Float dXds, Point2D.Float d2Xds2) {
		return (dXds.x * d2Xds2.y - dXds.y * d2Xds2.x) / (float)Math.pow((dXds.x * dXds.x + dXds.y * dXds.y), 1.5);
	}
	
	public static Point2D.Float normal(Point2D.Float dXds) {
		Point2D.Float result = new Point2D.Float();
		float ds = (float)Math.sqrt(dXds.x * dXds.x + dXds.y * dXds.y);
		result.x = -dXds.y / ds;
		result.y = dXds.x / ds;
		return result;
	}
	
	public float getLength() {
		return length;
	}
}
