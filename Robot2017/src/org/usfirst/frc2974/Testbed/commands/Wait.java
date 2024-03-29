package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPeg;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Wait extends Command{
	private final double startTime;
	private final int waitTime;
	
	@Override
	protected boolean isFinished() {
		return Timer.getFPGATimestamp() - startTime > waitTime;
	}
	
	public Wait(int timeToWait)
	{
		startTime = Timer.getFPGATimestamp();
		waitTime = timeToWait;
	}

}
