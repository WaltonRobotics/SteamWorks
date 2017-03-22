package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.Command;

public class BotPosition extends Command {

	/**
	 * Selects the command sequence that runs when autonomous starts
	 */

	public Position position;

	public enum Position {
		RedBoiler, RedCenter, RedFar, BlueBoiler, BlueCenter, BlueFar
	}

	public BotPosition(Position position) {
		this.position = position;
	}

	public void initialize() {
		switch (position) {
		case RedBoiler:
			new RedBoiler();
			break;
		case RedCenter:
			new RedCenter();
			break;
		case RedFar:
			new RedFar();
			break;
		case BlueBoiler:
			new BlueBoiler();
			break;
		case BlueCenter:
			new BlueCenter();
			break;
		case BlueFar:
			new BlueFar();
			break;
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
