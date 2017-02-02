package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonDiffRunnable extends CommandGroup {
	
	public TurnInTime.Direction turnDirection;
	public TurnInTime.Direction antiTurnDirection;
	
	public double timeToGoal;
	public double turnTimeToGoal;
	
    public enum Position{
    	LEFT, RIGHT, CENTER
    }
    
    public  AutonDiffRunnable(Position position, boolean doesShoot) {
    	
    	if(position == Position.CENTER){
    		addSequential(new DriveStraightTrapezoid(0.4, 0.5));
    		addSequential(new DriveStraightTrapezoid(-0.4, 0.2));
        	addSequential(new TurnInTime(0.4,0.25,TurnInTime.Direction.CLOCKWISE));
        	addSequential(new DriveStraightTrapezoid(0.4, 1));
    	}else{
    		if (position == Position.LEFT){
    			turnDirection = TurnInTime.Direction.ANTICLOCKWISE;
    			antiTurnDirection = TurnInTime.Direction.CLOCKWISE;
    			timeToGoal=2;
    			turnTimeToGoal=.25;
    		}else{
    			turnDirection = TurnInTime.Direction.CLOCKWISE;
    			antiTurnDirection = TurnInTime.Direction.ANTICLOCKWISE;
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
        	//addSequential(new Shoot()); TODO add shooting
    	}
    }
}
