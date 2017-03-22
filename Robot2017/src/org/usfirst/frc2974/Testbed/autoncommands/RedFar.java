package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RedFar extends CommandGroup {

	public RedFar() {
		DriveSplineByEncoder.stateX = -2.55;
		DriveSplineByEncoder.stateY = 1.422;
		DriveSplineByEncoder.stateAngle = -Math.PI / 3;

		addSequential(new DriveSplineByEncoder(false, 0, 0, 0, 0));
		addSequential(new DriveStraightTrapezoid(false, 0.001, 5, DriveStraightTrapezoid.Direction.ANTIFORWARD));
		addSequential(new DriveStraightByEncoder(false, 0.8, 2, 2));
		addSequential(new DriveTurnByEncoder(false, Math.PI / 3, 2, 2));
		addSequential(new DriveStraightByEncoder(false, -2.4384, 2, 2));
	}

}
