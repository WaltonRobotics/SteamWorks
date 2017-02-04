
// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc2974.Testbed;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc2974.Testbed.auton.AutonDiffRunnable;
import org.usfirst.frc2974.Testbed.auton.AutonRunnable;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.TurnInTime;
import org.usfirst.frc2974.Testbed.commands.AutoShifting;
import org.usfirst.frc2974.Testbed.logging.CSVWriter;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerDriver;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;
import org.usfirst.frc2974.Testbed.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autonomousCommand;

    public static OI oi;
    private SendableChooser<CommandGroup> autoChooser;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Drivetrain drivetrain;
    public static PoseEstimator poseEstimator;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    RobotMap.init();
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drivetrain = new Drivetrain();

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        poseEstimator = new PoseEstimator();

        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

//        autonomousCommand = new AutonomousCommand();

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        createAutonomousChooser();
        SmartDashboard.putNumber("Duration", 0);
        SmartDashboard.putNumber("aMax", 0);    
        SmartDashboard.putNumber("DiffPercent", 0);  
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	CSVWriter.closeAll();
    	RobotLoggerManager.closeHandlers();
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        Robot.drivetrain.dumpSmartdashboardValues();
        
        CSVWriter.closeAll();
    	RobotLoggerManager.closeHandlers();
    }

    public void autonomousInit() {
        autonomousCommand = new AutonDiffRunnable(AutonDiffRunnable.Position.LEFT,true);
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }
    
    private void createAutonomousChooser(){
    	
    	autoChooser = new SendableChooser<CommandGroup>();//TODO add in commands
    	autoChooser.addDefault("Do Nothing", null);
    	autoChooser.addObject("Left (Shoot)", new AutonRunnable(AutonRunnable.Position.LEFT, true));
    	autoChooser.addObject("Middle (Shoot)", new AutonRunnable(AutonRunnable.Position.CENTER, true));
    	autoChooser.addObject("Right (Shoot)", new AutonRunnable(AutonRunnable.Position.RIGHT, true));
    	autoChooser.addObject("Left (No Shoot)", new AutonRunnable(AutonRunnable.Position.LEFT, false));
    	autoChooser.addObject("Middle (No Shoot)", new AutonRunnable(AutonRunnable.Position.CENTER, false));
    	autoChooser.addObject("Right (No Shoot)", new AutonRunnable(AutonRunnable.Position.RIGHT, false));
    	autoChooser.addObject("DiffLeft (Shoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.LEFT, true));
    	autoChooser.addObject("DiffMiddle (Shoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.CENTER, true));
    	autoChooser.addObject("DiffRight (Shoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.RIGHT, true));
    	SmartDashboard.putData("Auto", autoChooser);
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        RobotMap.compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Robot.drivetrain.dumpSmartdashboardValues();
        createTestButtons();
    }
    
    public void createTestButtons(){
    	SmartDashboard.putData("TurnTimeClockwise",new TurnInTime(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0),TurnInTime.Direction.CLOCKWISE));
    	SmartDashboard.putData("TurnTimeAntiClockwise",new TurnInTime(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0),TurnInTime.Direction.ANTICLOCKWISE));
      	SmartDashboard.putData("MoveTimeForward",new DriveStraightTrapezoid(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0), DriveStraightTrapezoid.Direction.FORWARD));
      	SmartDashboard.putData("MoveTimeAntiforward",new DriveStraightTrapezoid(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0), DriveStraightTrapezoid.Direction.ANTIFORWARD));
      	SmartDashboard.putData("DiffTimeClockWise",new DriveDiffTrapezoid(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.CLOCKWISE));
      	SmartDashboard.putData("DiffTimeAntiClockWise",new DriveDiffTrapezoid(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE));
      	SmartDashboard.putData("DiffTimeClockWiseBack",new DriveDiffTrapezoid(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK));
      	SmartDashboard.putData("DiffTimeAntiClockWiseBack",new DriveDiffTrapezoid(SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISEBACK));
      	
      	
    }
    @Override
    public void testInit() {
    	RobotLoggerDriver.test();
    	
    	CSVWriter.closeAll();
    	RobotLoggerManager.closeHandlers();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
