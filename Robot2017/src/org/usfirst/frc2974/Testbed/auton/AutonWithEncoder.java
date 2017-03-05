package org.usfirst.frc2974.Testbed.auton;


import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPeg;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonWithEncoder extends CommandGroup {

    public AutonWithEncoder(AutonEncoderToPeg.Position position) {
    		addSequential(new AutonEncoderToPeg(position));
    		//addSequential(new Wait(3));
    		//addSequential(new AutonEncoderToBaseline(position));
    }
}
