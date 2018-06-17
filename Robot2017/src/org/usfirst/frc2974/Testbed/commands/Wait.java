package org.usfirst.frc2974.Testbed.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Wait extends Command {

	private final double startTime;
	private final int waitTime;

	public Wait(int timeToWait) {
		startTime = Timer.getFPGATimestamp();
		waitTime = timeToWait;
	}

	@Override
	protected boolean isFinished() {
		return Timer.getFPGATimestamp() - startTime > waitTime;
	}

}
