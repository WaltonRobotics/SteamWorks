package org.usfirst.frc2974.Testbed.controllers;

import edu.wpi.first.wpilibj.Timer;

public class Kinematics {
	
	  private final double robotWidth = 0.64135; // Practice bot
	
	  private MotionProvider motion;
	  private KinematicPose lastPose;
	  private KinematicPose nextPose;
	  
	  private double s;
	  private double v0;
	  private double v1;
	  private int nPoints;
	  
	  public Kinematics(MotionProvider motion, RobotPair wheelPosition, double t, double v0, double v1, int nPoints) {
		  this.motion = motion;
		  this.s = 0.0;
		  this.v0 = v0;
		  this.v1 = v1;
		  this.nPoints = nPoints;
		  
		  evaluateFirstPose(wheelPosition, t);
		  evaluateNextPose(1.0 / nPoints);
	  }
	  
	  public static KinematicPose staticPose(Pose pose, RobotPair wheelPosition, double t) {
		  KinematicState left = new KinematicState(wheelPosition.left, 0, 0);
		  KinematicState right = new KinematicState(wheelPosition.right, 0, 0);
		  return new KinematicPose(pose, left, right, t, false);
	  }
	  
	  private void evaluateFirstPose(RobotPair wheelPosition, double t) {
		  System.out.println("evaluating first pose");
		  Pose pose = motion.evaluatePose(0);
		  KinematicState left = new KinematicState(wheelPosition.left, v0, 0);
		  KinematicState right = new KinematicState(wheelPosition.right, v0, 0);
		  
		  lastPose = nextPose = new KinematicPose(pose, left, right, t, false);
//		  System.out.println(lastPose.toString());
	  }
	  private void evaluateLastPose(){
		  System.out.println("evaluating last pose");
		  Pose pose = motion.evaluatePose(1.0);
		  KinematicState left = new KinematicState(lastPose.left.l, v1, 0);
		  KinematicState right = new KinematicState(lastPose.right.l, v1, 0);
		  
		  lastPose = nextPose = new KinematicPose(pose, left, right, Double.POSITIVE_INFINITY, true);
//		  System.out.println(lastPose.toString());
	  }
	  
	  private void evaluateNextPose(double ds) {
		  lastPose = nextPose;
		  
		  if (lastPose.isFinished) {
			  evaluateLastPose();
			  return;
		  }
		  System.out.println("evaluating next pose");

		  boolean isFinished = false;
		  if(s + ds > 1.0) {
			  s = 1.0;
			  isFinished = true;
		  }
		  else {
			  s += ds;
		  }
		  
		  //calculate the next pose from given motion
		  Pose pose = motion.evaluatePose(s);
		  
		  //estimate angle to turn 
		  double dl = lastPose.X.distance(pose.X);
		  double dAngle = MotionProvider.boundAngle(pose.angle - lastPose.angle);
		  
		  //estimate lengths each wheel will turn
		  double dlLeft = Math.abs(dl - dAngle * robotWidth / 2);
		  double dlRight = Math.abs(dl + dAngle * robotWidth / 2);
		  
		  //assuming one of the wheels will limit motion, calculate time this step will take
		  double dt = Math.max(dlLeft, dlRight) / motion.vCruise;
		  double a = 0.0; //acceleration doesn't matter if following steady motion
		  System.out.println(String.format("s=%f, dlLeft=%f, dlRight=%f, dt=%f", s, dlLeft, dlRight, dt));
		  //bound time steps for initial/final acceleration
		  switch(motion.getLimitMode()) {
		  case LimitLinearAcceleration:
			  //assuming constant linear acceleration from initial/to final speeds
			  double v = dl/dt;
			  double lMidpoint = lastPose.getLCenter() + .5 * dl;
			  
			  double vAccel = Math.sqrt(v0 * v0 + motion.aMax * lMidpoint);
			  if(vAccel < v) {
				  a = motion.aMax;
				  dt = dl / vAccel;
			  }
			  
			  double vDecel = Math.sqrt(v1 * v1 + motion.aMax * (motion.getLength() - lMidpoint));
			  if(vDecel < v) {
				  a = -motion.aMax;
				  dt = dl / vDecel;
			  }
			  break;
		  case LimitRotationalAcceleration:
			  //assuming constant angular acceleration from/to zero angular speed
			  double omega = Math.abs(dlRight - dlLeft) / dt / robotWidth;
			  double thetaMidpoint = lastPose.angle + .5 * dAngle;
			  
			  double omegaAccel = Math.sqrt(motion.aMax * Math.abs(MotionProvider.boundAngle(thetaMidpoint - motion.getInitialTheta())));
		//	  System.out.println("OmegaAccel=" + omegaAccel);
			  if(omegaAccel < omega) {
				  dt = Math.abs(dlRight - dlLeft) / omegaAccel / robotWidth;
			  }
			  
			  double omegaDecel = Math.sqrt(motion.aMax * Math.abs(MotionProvider.boundAngle(thetaMidpoint - motion.getFinalTheta())));
		//	  System.out.println("OmegaDecel=" + omegaDecel);
			  if(omegaDecel < omega) {
				  dt = Math.abs(dlRight - dlLeft) / omegaDecel / robotWidth;
			  }
			  break;
		  }
		  
		  //create new kinematic state. Old state is retained to interpolate positions, new state contains estimate for speed and accel
		  KinematicState left = new KinematicState(lastPose.left.l + dlLeft, dlLeft / dt, a);
		  KinematicState right = new KinematicState(lastPose.right.l + dlRight, dlRight / dt, a);
		  
		  nextPose = new KinematicPose(pose, left, right, lastPose.t + dt, isFinished);
//		  System.out.println(nextPose.toString());
	  }

	  //calculates the path to follow within a particular distance
	  public KinematicPose interpolatePose(double t) {
		  if(t <= lastPose.t) {
			  return lastPose;
		  }
		  
		  while(t > nextPose.t) {
			  evaluateNextPose(1.0 / nPoints);
		  }
		  
		  if(lastPose.isFinished) {
			  return lastPose;
		  }
		  
		  double dt = nextPose.t - lastPose.t;
		  double p = (nextPose.t - t) / dt;
		  double q = (t - lastPose.t) / dt;
		  
		  return KinematicPose.interpolate(lastPose, p, nextPose, q);
	  }
	  
	  public Pose getPose(){
		  return new Pose(nextPose.X, nextPose.angle);
	  }

	  public RobotPair getWheelPositions() {
		  return new RobotPair(nextPose.left.l, nextPose.right.l);
	  }
	  
	  public double getTime(){
		  return nextPose.t;
	  }
}
