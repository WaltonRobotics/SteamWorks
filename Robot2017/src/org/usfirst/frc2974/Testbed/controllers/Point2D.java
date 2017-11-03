package org.usfirst.frc2974.Testbed.controllers;

public class Point2D {
  public double x;
  public double y;

  public Point2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // Calculates distance between two points
  public double distance(Point2D other) {
    double deltaX = Math.pow(this.x - other.x, 2);
    double deltaY = Math.pow(this.y - other.y, 2);
    return Math.sqrt(deltaX + deltaY);
  }

  public Point2D offsetPoint(double l, double angle) {
    return new Point2D(x + l * Math.cos(angle), y + l * Math.sin(angle));
  }

  public static Point2D interpolate(Point2D pose0, double p, Point2D pose1, double q) {
    return new Point2D(pose0.x * p + pose1.x * q, pose0.y * p + pose1.y * q);
  }

  @Override
  public String toString() {
    return String.format("x=%f, y=%f", this.x, this.y);
  }
}
