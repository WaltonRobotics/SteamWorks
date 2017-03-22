package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BlueCenter extends CommandGroup {

	public BlueCenter() {
		addSequential(new DriveStraightByEncoder(false, -2.134, 2, 2));

//		DriveSplineByEncoder.stateX = -2.134;
//		DriveSplineByEncoder.stateY = 0;
//		DriveSplineByEncoder.stateAngle = 0;
//
//		addSequential(new DriveSplineByEncoder(false, 0, 0, 0, 0));
	}
}
