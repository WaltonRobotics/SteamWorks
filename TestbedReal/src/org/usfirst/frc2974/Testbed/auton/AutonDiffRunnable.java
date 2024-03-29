package org.usfirst.frc2974.Testbed.auton;

import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.TurnInTime;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid.DiffDirection;
import org.usfirst.frc2974.Testbed.autoncommands.TurnInTime.Direction;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonDiffRunnable extends CommandGroup {
	
	public DriveDiffTrapezoid.DiffDirection diffDirection;
	public DriveDiffTrapezoid.DiffDirection antiDiffDirection;
	public Position position;
	public boolean doesShoot;
	
	public Command toGoalCommand;
	
    public enum Position{
    	LEFT, RIGHT, CENTER
    }
    
    public  AutonDiffRunnable(Position position, boolean doesShoot) {
    	this.position = position;
    	this.doesShoot = doesShoot;
    	switch(position) {
    	
    	case CENTER:
    		addSequential(new DriveStraightTrapezoid(false, 0.7, 1.1,DriveStraightTrapezoid.Direction.FORWARD));
    		addSequential(new DriveDiffTrapezoid(false, 0.5, 0.9,0.2,DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK));
        	addSequential(new DriveStraightTrapezoid(false, 0.7, 1.3,DriveStraightTrapezoid.Direction.FORWARD));//check
    		break;
    		
    	case LEFT:
    		directionAuton(DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE,
    				DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISEBACK,
    				new DriveStraightTrapezoid(false, 0.7,1.2,DriveStraightTrapezoid.Direction.FORWARD));
    		break;
    		
    	case RIGHT:
    		directionAuton(DriveDiffTrapezoid.DiffDirection.CLOCKWISE,
    				DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK,
    				new DriveDiffTrapezoid(false, 0.7,1.8,0.65,DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE));
    		break;
    	}
        if(doesShoot){
            //addSequential(new Shoot()); TODO add shooting
        }
    }

	private void directionAuton(DiffDirection diffDirection, DiffDirection antiDiffDirection,
			Command toGoalCommand) {
		addSequential(new DriveDiffTrapezoid(false, 0.7, 1.8, 0.65,diffDirection));//move onto peg
    	addSequential(new DriveDiffTrapezoid(false, 0.7, 1.2,0.2,antiDiffDirection));//reverse off peg
    	addSequential(toGoalCommand);
	}
}
