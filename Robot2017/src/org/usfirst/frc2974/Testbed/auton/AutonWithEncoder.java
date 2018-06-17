package org.usfirst.frc2974.Testbed.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPeg;
import org.usfirst.frc2974.Testbed.autoncommands.HoldPower;

/**
 *
 */
public class AutonWithEncoder extends CommandGroup {

	public AutonWithEncoder(AutonEncoderToPeg.Position position) {
		addSequential(new AutonEncoderToPeg(position));
		addSequential(new HoldPower(-0.2));
		// addSequential(new Wait(3));
		// addSequential(new AutonEncoderToBaseline(position));
	}
}
