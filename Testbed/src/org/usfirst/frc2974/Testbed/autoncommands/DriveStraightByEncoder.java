package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.Motion;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Pose;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightByEncoder extends Command implements MotionProvider{

	private Pose initialPose;
	private double l1;
	private double l2;
	private double l3;
	private double t0;
	private double t1;
	private double t2;
	private double t3;
	private double vCruise;
	private double aMax;
	
	public DriveStraightByEncoder(double distance, double vCruise, double aMax) {
		l3 = distance;
		this.vCruise = vCruise;
		this.aMax = aMax;
	}
	
	@Override
	protected void initialize() {		
		initialPose = Robot.poseEstimator.getPose();
		t0 = Timer.getFPGATimestamp();
		
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
			t1 = Math.sqrt(l3/aMax + t0*t0);
			t2 = t1;
			t3 = Math.sqrt(l3/aMax + t1*t1) + t1;
		}
						
	}

	@Override
	protected void execute() {		
		
	}

	@Override
	protected boolean isFinished() {
		return(Timer.getFPGATimestamp() > t3);
	}

	@Override
	protected void end() {		
		
	}

	@Override
	protected void interrupted() {		
		end();
	}
	
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
			pos = l2+.5*acc*(time*time-t2*t2);
		}else if(time > t1) {
			acc = 0;
			vel = vCruise;
			pos = l1 + vCruise*(time-t1);
		}else if(time >t0) {
			acc = aMax;
			vel = acc*time;
			pos = .5*acc*(time*time-t0*t0);
		}else{
			acc = 0;
			vel = 0;
			pos = 0;
		}
		
		return new Motion(initialPose.positionLeftWheel + pos, vel, acc, 
						  initialPose.positionRightWheel + pos, vel, acc);
	}

}
