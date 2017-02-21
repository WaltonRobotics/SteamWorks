package com.stanistreet;

public class KinematicPose extends Pose {
	public final KinematicState left;
	public final KinematicState right;
	public final float t;
	public final boolean isFinished;
	
	KinematicPose (Pose pose, KinematicState left, KinematicState right, 
			               float t, boolean isFinished) {
		super(pose.X, pose.theta);
		this.left = left;
		this.right = right;
		this.t = t;
		this.isFinished = isFinished;
	}
	
	public float getLCenter() {
		return (this.left.l + this.right.l) / 2.0F;
	}
	
	public float getVCenter() {
		return (this.left.v + this.right.v) / 2.0F;
	}
	
	public static KinematicPose interpolate(KinematicPose pose0, float p, KinematicPose pose1, float q) {
		Pose pose = Pose.interpolate(pose0, p, pose1, q);
		KinematicState left = KinematicState.interpolate(pose0.left, p, pose1.left, q);
		KinematicState right = KinematicState.interpolate(pose0.right, p, pose1.right, q);
		return new KinematicPose(pose, left, right, p * pose0.t + q * pose1.t, false);
	}
}
