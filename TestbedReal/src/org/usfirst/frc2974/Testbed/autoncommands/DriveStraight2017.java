package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.*;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;


public class DriveStraight2017 extends Command {
	/* Drive a set distance in a set amount of time
	 * Use the time function and driveTrain
	 */
	double timeAtStart;
	final double duration;
	final double speed;
	
	public DriveStraight2017(double time, double speed) {
		duration = time;
		this.speed = speed;
		requires(Robot.drivetrain);
	}
	
	@Override
	protected void initialize() {
		timeAtStart = Timer.getFPGATimestamp();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.setSpeeds(speed, speed);
		System.out.println(Timer.getFPGATimestamp() + "/" + timeAtStart + "/" + duration);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		if(Timer.getFPGATimestamp() - timeAtStart < duration){
			return false;
		}else{
			return true;
		}	
	}

	@Override
	protected void end() {
		Robot.drivetrain.setSpeeds(0, 0);
		//new TurnToAngleLeft2017(duration, speed).start();
	}

	@Override
	protected void interrupted() {
		end();
		
	}
	
}
