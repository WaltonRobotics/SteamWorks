package org.usfirst.frc2974.Robot2017.controllers;

public class MotionPathStraight extends MotionProvider{
	private Motion initialMotion;
	private double l1;
	private double l2;
	private double l3;
	private double t0;
	private double t1;
	private double t2;
	private double t3;
	
	public MotionPathStraight(double distance, double vCruise, double aMax){
		super(vCruise, aMax);
		synchronized (this) {
			l3 = distance;
		}
		

	}
	@Override
	public Motion getMotion(double time) {
		double pos;
		double vel;
		double acc;
		
		if(time > t3) {
			acc = 0;
			vel = 0;
			pos = l3;
		}else if(time > t2) {
			acc = -aMax;
			vel = acc*(time-t3);
			pos = l3+.5*acc*(Math.pow(time-t3, 2));
		}else if(time > t1) {
			acc = 0;
			vel = vCruise;
			pos = l1 + vCruise*(time-t1);
		}else if(time >t0) {
			acc = aMax;
			vel = acc*(time-t0);
			pos = .5*acc*(Math.pow(time-t0, 2));
		}else{
			acc = 0;
			vel = 0;
			pos = 0;
		}
		
		Motion motion = new Motion(initialMotion.position.left + pos, vel, acc, 
				  initialMotion.position.right + pos, vel, acc, time > t3); 
		
		
		return motion;
	}

	@Override
	public void initialized(double time, Motion motion) {
		synchronized (this) {
			initialMotion = motion;
			t0 = time;
			
			double lAcc;
			double lDec;
			double lCruise;
					
			lAcc = vCruise*vCruise/(2*aMax);
			lDec = vCruise*vCruise/(2*aMax);
			lCruise = l3 - lAcc - lDec;
			if(lCruise >= 0) {
				l1 = lAcc;
				l2 = l1 + lCruise;
			
				t1 = vCruise/aMax + t0;
				t2 = lCruise/vCruise + t1;
				t3 = vCruise/aMax + t2;
			}else{
				l1 = l3/2;
				l2 = l3/2;
				t1 = Math.sqrt(l3/aMax) + t0;
				t2 = t1;
				t3 = Math.sqrt(l3/aMax) + t1;
			}			
		}
		
	}
	
	@Override
	public String toString(){
		double l0 = initialMotion!=null?initialMotion.position.mean():Float.NaN;
		return String.format("Calculated path: t0=%3.1f, t1=%3.1f, t2=%3.1f, t3=%3.1f, l0=%5.3f, l1=%5.3f, l2=%5.3f, l3=%5.3f",
						      t0, t1, t2, t3, l0, l1, l2, l3);
		
	}
	@Override
	public double getFinalTime() {
		return t3;
	}

}
