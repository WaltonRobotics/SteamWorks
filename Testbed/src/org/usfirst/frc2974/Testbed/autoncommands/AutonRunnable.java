package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonRunnable extends CommandGroup {
    
    public  AutonRunnable() {
    	
    	//CENTER
    	addSequential(null);//TODO move forward 110.3"-chassie width
    	addSequential(null);//move backward 50"
    	addSequential(null);//turn to goal
    	addSequential(null);//move to goal 
    	
    	//LEFT/RIGHT
    	addSequential(null);//move forward enough to turn
    	addSequential(null);//turn opposite angle of peg
    	addSequential(null);//move halfway to peg
    	addSequential(null);//turn to peg
    	addSequential(null);//move to peg
    	addSequential(null);//reverse off peg
    	addSequential(null);//turn to goal
    	addSequential(null);//move to goal
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
