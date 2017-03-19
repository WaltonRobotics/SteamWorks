package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BlueFar extends CommandGroup {

    public BlueFar() {
    	DriveSplineByEncoder.stateX = -2.28;
		DriveSplineByEncoder.stateY = -1.522;
		DriveSplineByEncoder.stateAngle = Math.PI/3;
		
		addSequential(new DriveSplineByEncoder(false, 0, 0, 0, 0));
	    addSequential(new DriveStraightTrapezoid(false, 0.001, 5, DriveStraightTrapezoid.Direction.ANTIFORWARD));
	    addSequential(new DriveStraightByEncoder(false, 0.8, 2, 2));
	    addSequential(new DriveTurnByEncoder(false, -Math.PI/3, 2, 2));
	    addSequential(new DriveStraightByEncoder(false, -2.4384, 2, 2));
    }
}
