package org.usfirst.frc2974.Testbed.auton;

import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.TurnInTime;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid.DiffDirection;
import org.usfirst.frc2974.Testbed.autoncommands.TurnInTime.Direction;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonRunnable extends CommandGroup {
	
	public TurnInTime.Direction turnDirection;
	public TurnInTime.Direction antiTurnDirection;
	public DriveDiffTrapezoid.DiffDirection diffDirection;
	
	public double timeToGoal;
	public double turnTimeToGoal;
	
    public enum Position{
    	LEFT, RIGHT, CENTER
    }
    
    public  AutonRunnable(Position position, boolean doesShoot) {
    	
    	if(position == Position.CENTER){
    		addSequential(new DriveStraightTrapezoid(0.7, 1.1));
    		addSequential(new DriveStraightTrapezoid(-0.5, 0.7));
        	addSequential(new TurnInTime(0.4,0.25,TurnInTime.Direction.CLOCKWISE));
        	addSequential(new DriveStraightTrapezoid(0.4, 1));
    	}else{
    		if (position == Position.LEFT){
    			turnDirection = TurnInTime.Direction.ANTICLOCKWISE;
    			antiTurnDirection = TurnInTime.Direction.CLOCKWISE;
    			diffDirection = DriveDiffTrapezoid.DiffDirection.CLOCKWISE;
    			timeToGoal=2;
    			turnTimeToGoal=.25;
    		}else{
    			turnDirection = TurnInTime.Direction.CLOCKWISE;
    			antiTurnDirection = TurnInTime.Direction.ANTICLOCKWISE;
    			diffDirection = DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE;
    			timeToGoal=.8;
    			turnTimeToGoal=.8;
    		}
    		addSequential(new DriveStraightTrapezoid(1, 0.1));//move forward enough to turn
        	addSequential(new TurnInTime(0.4,0.16,turnDirection));//turn opposite angle of peg
        	addSequential(new DriveStraightTrapezoid(0.4, 0.5));//move halfway to peg
        	addSequential(new TurnInTime(0.4,0.16,antiTurnDirection));//turn to peg
        	addSequential(new DriveStraightTrapezoid(0.4, 0.5));//move to peg
        	addSequential(new DriveStraightTrapezoid(-0.4, 0.5));//reverse off peg
        	addSequential(new TurnInTime(0.4,turnTimeToGoal,turnDirection));//turn to goal
        	addSequential(new DriveStraightTrapezoid(0.4, timeToGoal));//move to goal
        	if(doesShoot){
            	//addSequential(new Shoot()); TODO add shooting
        	}
    	}
    }
}
