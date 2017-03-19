package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RedCenter extends CommandGroup {

    public RedCenter() {
        addSequential(new DriveStraightByEncoder(false, -2.134, 2, 2));
    }
}
