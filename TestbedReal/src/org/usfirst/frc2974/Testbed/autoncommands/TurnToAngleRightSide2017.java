package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.*;

import edu.wpi.first.wpilibj.command.Command;

public class TurnToAngleRightSide2017 extends Command{
	double time = timeSinceInitialized();
	
	public TurnToAngleRightSide2017(){
		requires(Robot.drivetrain);
	}
	
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		Robot.drivetrain.setSpeeds(3, 6);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if(time < 3){
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
