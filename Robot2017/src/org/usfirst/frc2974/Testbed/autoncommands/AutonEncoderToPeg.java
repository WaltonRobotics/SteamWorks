package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.auton.AutonDiffRunnable.Position;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionPathTurn;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonEncoderToPeg extends Command {

	public Position position;
	public boolean doesShoot;
	final double speed = 0.25;
	final double accel = 0.25;

	public Command toGoalCommand;

	public enum Position {
		LEFT, RIGHT, CENTER
	}

	private Drivetrain driveTrain;

	public AutonEncoderToPeg(Position position, boolean doesShoot, Drivetrain driveTrain) {
		this.driveTrain = driveTrain;
		this.position = position;
		this.doesShoot = doesShoot;
		switch (position) {
		case CENTER:
			addDriveParameters(0, 0);
			break;
		case LEFT:
			addDriveParameters(0, 0);
			break;
		case RIGHT:
			addDriveParameters(0, 0);
			break;
		}
	}

	private void addDriveParameters(double distance, double angle) {
		driveTrain.addControllerMotion(new MotionPathStraight(null, 0, 0, 0));// move
																				// forward
																				// distance
		driveTrain.addControllerMotion(new MotionPathTurn(null, 0, 0, 0));// turn
																			// to
																			// peg
	}

	@Override
	public void initialize(){
		this.driveTrain.startMotion();
	}
	
	@Override
	public boolean isFinished() {
		if(driveTrain.isControllerFinished()){
			return true;
		}
		return false;
	}

}
