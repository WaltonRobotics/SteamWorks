package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.Command;

public class BotPosition extends Command{
	
	public Position position;
	
	public enum Position {
		RedBoiler, RedFar, BlueBoiler, BlueFar
	}
	
	public BotPosition(Position position) {
		this.position = position;		
	}
	
	public void initialize() {
		switch (position) {
		case RedBoiler:
			new RedBoiler();
			break;
		case RedFar:
			new RedFar();
			break;
		case BlueBoiler:
			break;
		case BlueFar:
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
