package org.usfirst.frc2974.Testbed.controllers;

public class KinematicState {

  // The length of /FIXME/
  public final double length;

  public final double velocity;
  public final double acceleration;

  public KinematicState(double l, double v, double a) {
    this.length = l;
    this.velocity = v;
    this.acceleration = a;
  }

  public static KinematicState interpolate(KinematicState state0, double p, KinematicState state1,
      double q) {
    return new KinematicState(p * state0.length + q * state1.length, state1.velocity,
        state1.acceleration);
  }

  @Override
  public String toString() {
    return String.format("length=%f, velocity=%f, acceleration=%f", length, velocity, acceleration);
  }
}
