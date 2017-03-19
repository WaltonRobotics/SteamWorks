package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveSplineRedRight extends CommandGroup {
	
    public DriveSplineRedRight() {
	    addSequential(new DriveSplineByEncoder(false, 0, 0, 0, 0));
	    addSequential(new DriveStraightTrapezoid(false, 0.0, 5, DriveStraightTrapezoid.Direction.ANTIFORWARD));
	    addSequential(new DriveStraightByEncoder(false, 0.8, 2, 2));
	    addSequential(new DriveTurnByEncoder(false, 2 * Math.PI/3, 2, 2));
	    addSequential(new DriveStraightByEncoder(false, 2.4384, 2, 2));
    }
    
}
