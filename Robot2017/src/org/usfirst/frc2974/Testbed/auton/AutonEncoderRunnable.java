package org.usfirst.frc2974.Testbed.auton;

import org.usfirst.frc2974.Testbed.auton.AutonDiffRunnable.Position;

import org.usfirst.frc2974.Testbed.autoncommands.DriveTurnByEncoder;
import org.usfirst.frc2974.Testbed.commands.Shoot;
import org.usfirst.frc2974.Testbed.autoncommands.DriveSplineByEncoder;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightByEncoder;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonEncoderRunnable extends CommandGroup {

	public Position position;
	public boolean doesShoot;
	final double speed = 0.25;
	final double accel = 0.25;

	public Command toGoalCommand;

	public enum Position {
		LEFT, RIGHT, CENTER
	}

	public AutonEncoderRunnable(Position position, boolean doesShoot) {
		this.position = position;
		this.doesShoot = doesShoot;
		switch (position) {
		case CENTER:
			addSequential(DriveSplineByEncoder.driveToPeg());
			if(doesShoot) {
				addSequential(new DriveStraightByEncoder(false, -0.05, speed, accel));
				addSequential(new DriveTurnByEncoder(false, -Math.PI / 2, speed, accel)); // Boolean, angle, speed, and rotational accel
				addSequential(DriveSplineByEncoder.driveToBoiler());
			}
			break;

		case LEFT:
			addSequential(DriveSplineByEncoder.driveToPeg()); 
			if(doesShoot){
				addSequential(new DriveStraightByEncoder(false, -0.2, speed, accel));
				addSequential(new DriveTurnByEncoder(false, -Math.PI / 3, speed, accel)); // Boolean, angle, speed, and rotational accel
				addSequential(DriveSplineByEncoder.driveToBoiler());// check			
			}
			break;
		case RIGHT:
			addSequential(DriveSplineByEncoder.driveToPeg());
			if(doesShoot){
				addSequential(new DriveStraightByEncoder(false, -0.05, speed, accel));
				addSequential(DriveSplineByEncoder.driveToBoiler());// check
			}
			break;
		}
		if (doesShoot) {
			//addSequential(new Shoot()); TODO add shooting
		}
	}
}
