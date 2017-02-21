package com.stanistreet;

public class KinematicState {
	public final float l;
	public final float v;
	public final float a;
	
	public KinematicState(float l, float v, float a) {
		this.l = l;
		this.v = v;
		this.a = a;
	}
	
	public static KinematicState interpolate(KinematicState state0, float p, KinematicState state1, float q) {
		return new KinematicState(p * state0.l  + q * state1.l, state1.v, state1.a);
	};
}