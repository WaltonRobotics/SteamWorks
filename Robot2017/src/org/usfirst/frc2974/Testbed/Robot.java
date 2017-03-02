
// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

/*Constants :
 * kK = 0.215
 * kP = 15
 * kV = 0.368
 * kA = 0.025
 */
package org.usfirst.frc2974.Testbed;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc2974.Testbed.auton.AutonDiffRunnable;
//import org.usfirst.frc2974.Testbed.auton.AutonEncoderWithVision;
import org.usfirst.frc2974.Testbed.autoncommands.Aim;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveSplineByEncoder;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightByEncoder;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveTurnByEncoder;
import org.usfirst.frc2974.Testbed.commands.ReadMotionControllerConstants;
import org.usfirst.frc2974.Testbed.commands.SetMotionControllerConstants;
import org.usfirst.frc2974.Testbed.commands.TestShooterPower;
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
    public static Shooter shooter;
    public static Hopper hopper;
    public static Intake intake;
    public static Climber climber;
    public static Aim aim;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    RobotMap.init();
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    	//	poseEstimator = new PoseEstimator();
    	poseEstimator = new PoseEstimator();
        drivetrain = new Drivetrain();

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        shooter = new Shooter();
        hopper = new Hopper();
        intake = new Intake();
        climber = new Climber();
        aim = new Aim();

        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

//        autonomousCommand = new AutonomousCommand();
        
//        boiler = new CommandGroup();
//		boiler.addSequential(new DriveTurnByEncoder(true ,0, 0, 0));
//		boiler.addSequential(DriveSplineByEncoder.driveToBoiler());
//		
//		peg = new CommandGroup();
//		peg.addSequential(new DriveTurnByEncoder(true ,0, 0, 0));
//		peg.addSequential(DriveSplineByEncoder.driveToBoiler());

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        createAutonomousChooser();
        SmartDashboard.putNumber("Duration", 0);
        SmartDashboard.putNumber("aMax", 0);    
        SmartDashboard.putNumber("DiffPercent", 0);  
        SmartDashboard.putNumber("Distance", 0);  
        SmartDashboard.putNumber("vCruise", 0);  
        SmartDashboard.putNumber("encoderDistance", 0);
        SmartDashboard.putNumber("encoderAccel", 0);
        SmartDashboard.putNumber("encoderSpeed", 0);
        SmartDashboard.putNumber("encoderAngle", 0);

    }
    
//    private CommandGroup boiler;
//    private CommandGroup peg;

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
        autonomousCommand = autoChooser.getSelected();
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	//poseEstimator.updatePose();
        Scheduler.getInstance().run();
        
    }
    
    private void createAutonomousChooser(){
    	
    	autoChooser = new SendableChooser<CommandGroup>();
    	autoChooser.addDefault("Do Nothing", null);
    	autoChooser.addObject("DiffLeft (Shoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.LEFT, true));
    	autoChooser.addObject("DiffMiddle (Shoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.CENTER, true));
    	autoChooser.addObject("DiffRight (Shoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.RIGHT, true));
    	autoChooser.addObject("DiffLeft (NoShoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.LEFT, false));
    	autoChooser.addObject("DiffMiddle (NoShoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.CENTER, false));
    	autoChooser.addObject("DiffRight (NoShoot)", new AutonDiffRunnable(AutonDiffRunnable.Position.RIGHT, false));
    	//autoChooser.addObject("Auton move to peg and boiler", new AutonEncoderWithVision(true));
    	//autoChooser.addObject("Auton move forward past line", new AutonEncoderWithVision(false));
    	SmartDashboard.putData("Auto", autoChooser);
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
      //  RobotMap.compressor.start();
        createTestButtons();

        Scheduler.getInstance().add(aim);	//TODO add other commands
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//poseEstimator.updatePose();
        Scheduler.getInstance().run();
        drivetrain.dumpSmartdashboardValues();
        poseEstimator.dumpSmartdashboardValues();
        shooter.dumpValuesToSamrtDashboard();
//        createTestButtons();
    }
    
    public void createTestButtons(){
    	
      	SmartDashboard.putData("MoveTimeForward",new DriveStraightTrapezoid(true, SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0), DriveStraightTrapezoid.Direction.FORWARD));
      	SmartDashboard.putData("MoveTimeAntiforward",new DriveStraightTrapezoid(true, SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0), DriveStraightTrapezoid.Direction.ANTIFORWARD));
      	SmartDashboard.putData("DiffTimeClockWise",new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.CLOCKWISE));
      	SmartDashboard.putData("DiffTimeAntiClockWise",new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE));
      	SmartDashboard.putData("DiffTimeClockWiseBack",new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK));
      	SmartDashboard.putData("DiffTimeAntiClockWiseBack",new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax",0)
    			,SmartDashboard.getNumber("Duration",0)
    			,SmartDashboard.getNumber("DiffPercent", 0)
    			,DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISEBACK));
      	SmartDashboard.putData("MotionEncoder", new DriveStraightByEncoder(true, 0, 0, 0));
		SmartDashboard.putData("SetMotionControllerConstants", new SetMotionControllerConstants());
		SmartDashboard.putData("ReadMotionControllerConstants", new ReadMotionControllerConstants());
		
		SmartDashboard.putData("TurnByEncoder", new DriveTurnByEncoder(true, 0, 0, 0));
		SmartDashboard.putData("TurnBySpline", new DriveSplineByEncoder(true, 0, 0, 0, 0));
		
		SmartDashboard.putData("testShooterPower", new TestShooterPower());

//		SmartDashboard.putData("Test peg with vision", peg);
//		SmartDashboard.putData("Test boiler with vision", boiler);
		//SmartDashboard.putData("testSplineEncoder", command);
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
}

