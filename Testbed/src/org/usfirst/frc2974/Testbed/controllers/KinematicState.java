package org.usfirst.frc2974.Testbed.controllers;

public class KinematicState {
	public final double l;
	public final double v;
	public final double a;
	
	public KinematicState(double l, double v, double a) {
		this.l = l;
		this.v = v;
		this.a = a;
	}
	
	public static KinematicState interpolate(KinematicState state0, double p, KinematicState state1, double q) {
		return new KinematicState(p * state0.l + q * state1.l, state1.v, state1.a);
	}
}
