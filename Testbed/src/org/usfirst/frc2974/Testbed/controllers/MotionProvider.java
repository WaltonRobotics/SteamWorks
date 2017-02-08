package org.usfirst.frc2974.Testbed.controllers;

public interface MotionProvider {
	Motion getMotion(double time);
	void initialized(double time, Motion motion);
	double getFinalTime();
}
