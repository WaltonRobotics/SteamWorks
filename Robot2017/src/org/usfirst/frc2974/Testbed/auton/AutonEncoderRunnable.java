package org.usfirst.frc2974.Testbed.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonEncoderRunnable extends CommandGroup {

	public enum Position{
		LEFT,
		RIGHT,
		CENTER;
	}
	
    public AutonEncoderRunnable(Position position) {
    	
    }
}
