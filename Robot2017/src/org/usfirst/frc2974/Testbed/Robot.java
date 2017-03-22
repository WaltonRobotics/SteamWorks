package org.usfirst.frc2974.Testbed;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc2974.Testbed.auton.AutonDiffRunnable;
//import org.usfirst.frc2974.Testbed.auton.AutonEncoderWithVision;
import org.usfirst.frc2974.Testbed.autoncommands.Aim;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPeg;
import org.usfirst.frc2974.Testbed.autoncommands.BotPosition;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveSplineByEncoder;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightByEncoder;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveTurnByEncoder;
import org.usfirst.frc2974.Testbed.autoncommands.RedBoiler;
import org.usfirst.frc2974.Testbed.autoncommands.RedFar;
import org.usfirst.frc2974.Testbed.commands.SetMotionControllerConstants;
import org.usfirst.frc2974.Testbed.commands.TestShooterPower;
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
	private SendableChooser<Command> autoChooser;
	public static Drivetrain drivetrain;
	public static PoseEstimator poseEstimator;
	public static Shooter shooter;
	public static Hopper hopper;
	public static Intake intake;
	public static Climber climber;
	public static Aim aim;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		RobotMap.init();
		declarePrefs();

		poseEstimator = new PoseEstimator();
		drivetrain = new Drivetrain();
		shooter = new Shooter();
		hopper = new Hopper();
		intake = new Intake();
		climber = new Climber();
		aim = new Aim();

		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();

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
		SmartDashboard.putBoolean("isForwards", true);

		SmartDashboard.putNumber("autonXOffset", 0);
	}

	private void declarePrefs() {
		boolean reset;
		Preferences pref = Preferences.getInstance();
		reset = pref.getBoolean("reset", false);

		if (reset || !pref.containsKey("isCompBot")) {
			pref.putBoolean("isCompBot", true);
		}

		Drivetrain.declarePrefs(reset);
		Shooter.declarePrefs(reset);
		Climber.declarePrefs(reset);

		pref.putBoolean("reset", false);
	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	public void disabledInit() {
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		dumpSmartDashboardValues();
	}

	public void autonomousInit() {
		drivetrain.shiftDown();

		try {
			autonomousCommand = autoChooser.getSelected();
			autonomousCommand.start();
		} catch (NullPointerException e) {
			autonomousCommand = new BotPosition(BotPosition.Position.BlueCenter);
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		// poseEstimator.updatePose();
		dumpSmartDashboardValues();
		Scheduler.getInstance().run();

	}

	private void createAutonomousChooser() {

		autoChooser = new SendableChooser<Command>();
		autoChooser.addDefault("Do Nothing", null);
		// autoChooser.addObject("CrossLine", new
		// DriveStraightTrapezoid(false,1,0.6,DriveStraightTrapezoid.Direction.ANTIFORWARD));
		// autoChooser.addObject("Station 1", new
		// AutonEncoderToPeg(AutonEncoderToPeg.Position.LEFT));
		// autoChooser.addObject("Station 2", new
		// AutonEncoderToPeg(AutonEncoderToPeg.Position.CENTER));
		// autoChooser.addObject("Station 3", new
		// AutonEncoderToPeg(AutonEncoderToPeg.Position.RIGHT));
		// autoChooser.addObject("Auton move to peg and boiler", new
		// AutonEncoderWithVision(true));
		// autoChooser.addObject("Auton move forward past line", new
		// AutonEncoderWithVision(false));
		autoChooser.addObject("Red1", new BotPosition(BotPosition.Position.RedFar));
		autoChooser.addObject("Red2", new BotPosition(BotPosition.Position.RedCenter));
		autoChooser.addObject("Red3", new BotPosition(BotPosition.Position.RedBoiler));
		autoChooser.addObject("Blue1", new BotPosition(BotPosition.Position.BlueBoiler));
		autoChooser.addObject("Blue2", new BotPosition(BotPosition.Position.BlueCenter));
		autoChooser.addObject("Blue3", new BotPosition(BotPosition.Position.BlueFar));

		SmartDashboard.putData("Auto", autoChooser);
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		// RobotMap.compressor.start();
		createTestButtons();

		Scheduler.getInstance().add(aim); // TODO add other commands
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		// poseEstimator.updatePose();
		dumpSmartDashboardValues();
		Scheduler.getInstance().run();
	}

	public void createTestButtons() {

		SmartDashboard.putData("MoveTimeForward", new DriveStraightTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
				SmartDashboard.getNumber("Duration", 0), DriveStraightTrapezoid.Direction.FORWARD));
		SmartDashboard.putData("MoveTimeAntiforward",
				new DriveStraightTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
						SmartDashboard.getNumber("Duration", 0), DriveStraightTrapezoid.Direction.ANTIFORWARD));
		SmartDashboard.putData("DiffTimeClockWise",
				new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
						SmartDashboard.getNumber("Duration", 0), SmartDashboard.getNumber("DiffPercent", 0),
						DriveDiffTrapezoid.DiffDirection.CLOCKWISE));
		SmartDashboard.putData("DiffTimeAntiClockWise",
				new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
						SmartDashboard.getNumber("Duration", 0), SmartDashboard.getNumber("DiffPercent", 0),
						DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE));
		SmartDashboard.putData("DiffTimeClockWiseBack",
				new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
						SmartDashboard.getNumber("Duration", 0), SmartDashboard.getNumber("DiffPercent", 0),
						DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK));
		SmartDashboard.putData("DiffTimeAntiClockWiseBack",
				new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
						SmartDashboard.getNumber("Duration", 0), SmartDashboard.getNumber("DiffPercent", 0),
						DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISEBACK));
		SmartDashboard.putData("SetMotionControllerConstants", new SetMotionControllerConstants());

		SmartDashboard.putData("MotionEncoder", new DriveStraightByEncoder(true, 0, 0, 0));
		SmartDashboard.putData("TurnByEncoder", new DriveTurnByEncoder(true, 0, 0, 0));
		SmartDashboard.putData("TurnBySpline", new DriveSplineByEncoder(true, 0, 0, 0, 0));

		SmartDashboard.putData("testShooterPower", new TestShooterPower());
		SmartDashboard.putData("testToPegCenter", new AutonEncoderToPeg(AutonEncoderToPeg.Position.CENTER));
		SmartDashboard.putData("testToPegLeft", new AutonEncoderToPeg(AutonEncoderToPeg.Position.LEFT));
		SmartDashboard.putData("testToPegRight", new AutonEncoderToPeg(AutonEncoderToPeg.Position.RIGHT));
	}

	@Override
	public void testInit() {
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

	private void dumpSmartDashboardValues() {
		drivetrain.dumpSmartdashboardValues();
		poseEstimator.dumpSmartdashboardValues();
		shooter.dumpValuesToSamrtDashboard();
		climber.dumpSmartdashboardValues();
	}
}
