package org.usfirst.frc2974.Testbed.controllers;

public class Pose {

	public final Point2D X;
	public final double angle;

	public Pose(Point2D X, double angle) {
		this.X = X;
		this.angle = angle;
	}

	public static Pose interpolate(Pose pose0, double p, Pose pose1, double q) {
		return new Pose(Point2D.interpolate(pose0.X, p, pose1.X, q), p * pose0.angle + q * pose1.angle);
	}

	public Point2D offsetPoint(double l) {
		return new Point2D(X.x + l * Math.cos(angle), X.y + l * Math.sin(angle));
	}

	@Override
	public String toString() {
		return String.format("x=%f, y=%f, angle=%f", X.x, X.y, angle);
	}

}
