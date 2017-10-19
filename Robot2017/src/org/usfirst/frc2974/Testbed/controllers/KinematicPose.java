package org.usfirst.frc2974.Testbed.controllers;

public class KinematicPose extends Pose{
	public final KinematicState left;
	public final KinematicState right;
	public final double t;
	public final boolean isFinished;
	
	KinematicPose(Pose pose, KinematicState left, KinematicState right, double t, boolean isFinished) {
		super(pose.X, pose.angle);
		this.left = left;
		this.right = right;
		this.t = t;
		this.isFinished = isFinished;
	}
	
	public double getLCenter() {
		return (this.left.length + this.right.length) / 2.0;
	}
	
	public double getVCenter() {
		return(this.left.velocity + this.right.velocity) / 2.0;
	}
	
	public static KinematicPose interpolate(KinematicPose pose0, double p, KinematicPose pose1, double q) {
		Pose pose = Pose.interpolate(pose0, p, pose1, q);
		KinematicState left = KinematicState.interpolate(pose0.left, p, pose1.left, q);
		KinematicState right = KinematicState.interpolate(pose0.right, p, pose1.right, q);
		return new KinematicPose(pose, left, right, p * pose0.t + q * pose1.t, false);
	}
	
	public String toString(){
		return String.format("%s, left:%s, right:%s, t=%f, isFinished=%s", super.toString(), left, right, t, isFinished);
	}
}
