package org.usfirst.frc2974.Testbed.controllers;

public abstract class MotionProvider {
  public enum LimitMode {
    LimitLinearAcceleration, LimitRotationalAcceleration
  }

  protected Motion initialMotion;
  protected double vCruise;
  protected double aMax;

  protected MotionProvider(double vCruise, double aMax) {
    if (vCruise == 0)
      throw new IllegalArgumentException("vCruise cannot be 0");
    this.vCruise = vCruise;

    if (aMax == 0)
      throw new IllegalArgumentException("aMax cannot be 0");
    this.aMax = aMax;
  }

  public abstract Pose evaluatePose(double s);

  public abstract LimitMode getLimitMode();

  public abstract double getLength();

  public abstract double getInitialTheta();

  public abstract double getFinalTheta();

  public static double boundAngle(double angle) {
    if (angle > Math.PI) {
      return angle - Math.PI;
    }
    if (angle < -Math.PI) {
      return angle + Math.PI;
    }
    return angle;
  }

  public Pose getFinalPose() {
    return evaluatePose(1);
  }

  public Pose getInitialPose() {
    return evaluatePose(0);
  }

}
