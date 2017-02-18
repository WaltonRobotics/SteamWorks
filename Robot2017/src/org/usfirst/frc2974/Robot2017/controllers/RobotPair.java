package org.usfirst.frc2974.Robot2017.controllers;

public class RobotPair {

	public final double left;
	public final double right;

	public RobotPair(double left, double right) {
		this.left = left;
		this.right = right;
	}

	public double mean() {
		return (this.left + this.right) / 2;
	}

	@Override
	public String toString() {
		return String.format("%f, %f", left, right);
	}

}
