package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.*;

import edu.wpi.first.wpilibj.command.Command;

public class TurnToAngleLeft2017 extends Command{
	
	double timeAtStart;
	final double duration;
	final double speed;
	
	public TurnToAngleLeft2017(double time, double speed) {
		duration = time;
		this.speed = speed;
		requires(Robot.drivetrain);
	}
	
	@Override
	protected void initialize() {
		timeAtStart = timeSinceInitialized();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.setSpeeds(speed/2, speed);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if(timeSinceInitialized() - timeAtStart < duration){
			return false;
		}
		else{
			return true;
		}	
	}

	@Override
	protected void end() {
		Robot.drivetrain.setSpeeds(0, 0);
	}

	@Override
	protected void interrupted() {
		end();
		
	}
}
