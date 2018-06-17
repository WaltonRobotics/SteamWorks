package org.usfirst.frc2974.Testbed;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderCameraToPeg;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPeg;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPegCharge;
import org.usfirst.frc2974.Testbed.autoncommands.AutonEncoderToPegShoot;
import org.usfirst.frc2974.Testbed.commands.TestShooterPower;
import org.usfirst.frc2974.Testbed.subsystems.Climber;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;
import org.usfirst.frc2974.Testbed.subsystems.GearIntake;
import org.usfirst.frc2974.Testbed.subsystems.Hopper;
import org.usfirst.frc2974.Testbed.subsystems.Intake;
import org.usfirst.frc2974.Testbed.subsystems.PoseEstimator;
import org.usfirst.frc2974.Testbed.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the IterativeRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;
	public static SendableChooser<String> driverSelect;
	public static Drivetrain drivetrain;
	public static PoseEstimator poseEstimator;
	public static Shooter shooter;
	public static Hopper hopper;
	public static Intake intake;
	public static Climber climber;
	public static GearIntake gearIntake;
	Command autonomousCommand;
	private SendableChooser<Command> autoChooser;
	// public static Aim aim;
	private SendableChooser<Command> legacyChooser;

	// public static BallMover ballMover;

	/**
	 * This function is run when the robot is first started up and should be used for any initialization code.
	 */
	@Override
	public void robotInit() {
		RobotMap.init();
		declarePrefs();

		poseEstimator = new PoseEstimator();
		drivetrain = new Drivetrain();
		shooter = new Shooter();
		hopper = new Hopper();
		intake = new Intake();
		climber = new Climber();
		// aim = new Aim();
		gearIntake = new GearIntake();

		// ballMover = new BallMover();

		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();

		createAutonomousChooser();
		createLegacyChooser();
		createDriverSelect();
		// SmartDashboard.putNumber("Duration", 0);
		// SmartDashboard.putNumber("aMax", 0);
		// SmartDashboard.putNumber("DiffPercent", 0);
		// SmartDashboard.putNumber("Distance", 0);
		// SmartDashboard.putNumber("vCruise", 0);
		// SmartDashboard.putNumber("encoderDistance", 0);
		// SmartDashboard.putNumber("encoderAccel", 0);
		// SmartDashboard.putNumber("encoderSpeed", 0);
		// SmartDashboard.putNumber("encoderAngle", 0);
		// SmartDashboard.putBoolean("isForwards", true);
		// SmartDashboard.putBoolean("captureImage", false);
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
		GearIntake.declarePrefs(reset);

		pref.putBoolean("reset", false);
	}

	/**
	 * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting
	 * down.
	 */
	@Override
	public void disabledInit() {
		SmartDashboard.putBoolean("captureImage", false);

		// CameraServer.getInstance().removeCamera("cam0");

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		dumpSmartDashboardValues();
	}

	@Override
	public void autonomousInit() {
		SmartDashboard.putBoolean("captureImage", true);

		drivetrain.shiftDown();
		try {
			if (Preferences.getInstance().getBoolean("drivetrain.autonLegacy", true)) {
				autonomousCommand = legacyChooser.getSelected();
			} else {
				autonomousCommand = autoChooser.getSelected();
			}
			autonomousCommand.start();
		} catch (NullPointerException e) {
			autonomousCommand = new AutonEncoderCameraToPeg(AutonEncoderCameraToPeg.Position.CENTER,
				AutonEncoderCameraToPeg.Control.NOTHING);
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		// poseEstimator.updatePose();
		dumpSmartDashboardValues();
		Scheduler.getInstance().run();

	}

	private void createAutonomousChooser() {
		autoChooser = new SendableChooser<Command>();

		autoChooser.addDefault("Center", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.CENTER, AutonEncoderCameraToPeg.Control.NOTHING));
		autoChooser.addObject("Red 1", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.RED1, AutonEncoderCameraToPeg.Control.NOTHING));
		autoChooser.addObject("Red 3", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.RED3, AutonEncoderCameraToPeg.Control.NOTHING));
		autoChooser.addObject("Blue 1", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.BLUE1, AutonEncoderCameraToPeg.Control.NOTHING));
		autoChooser.addObject("Blue 3", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.BLUE3, AutonEncoderCameraToPeg.Control.NOTHING));
		autoChooser.addObject("Red 1 and Charge", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.RED1, AutonEncoderCameraToPeg.Control.CHARGE));
		autoChooser.addObject("Red 3 and Charge", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.RED3, AutonEncoderCameraToPeg.Control.CHARGE));
		autoChooser.addObject("Blue 1 and Charge", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.BLUE1, AutonEncoderCameraToPeg.Control.CHARGE));
		autoChooser.addObject("Blue 3 and Charge", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.BLUE3, AutonEncoderCameraToPeg.Control.CHARGE));
		autoChooser.addObject("Red 3 and Shoot", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.RED3, AutonEncoderCameraToPeg.Control.SHOOT));
		autoChooser.addObject("Blue 1 and Shoot", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.BLUE1, AutonEncoderCameraToPeg.Control.SHOOT));
		autoChooser.addObject("Do Nothing", new AutonEncoderCameraToPeg(
			AutonEncoderCameraToPeg.Position.DONOTHING, AutonEncoderCameraToPeg.Control.NOTHING)); // Do
		// nothing
		// auton
		// chooser

		SmartDashboard.putData("Auto", autoChooser);
	}

	public void createDriverSelect() {
		driverSelect = new SendableChooser<String>();

		driverSelect.addDefault("Tank", "Tank");
		driverSelect.addObject("Cheesy", "Cheesy");

		SmartDashboard.putData("Driver Selection", driverSelect);
	}

	public void createLegacyChooser() {
		legacyChooser = new SendableChooser<Command>();

		legacyChooser.addDefault("Center No Camera",
			new AutonEncoderToPeg(AutonEncoderToPeg.Position.RED2));
		legacyChooser.addObject("Blue 1 No Camera",
			new AutonEncoderToPeg(AutonEncoderToPeg.Position.BLUE1));
		legacyChooser.addObject("Blue 3 No Camera",
			new AutonEncoderToPeg(AutonEncoderToPeg.Position.BLUE3));
		legacyChooser.addObject("Red 1 No Camera",
			new AutonEncoderToPeg(AutonEncoderToPeg.Position.RED1));
		legacyChooser.addObject("Red 3 No Camera",
			new AutonEncoderToPeg(AutonEncoderToPeg.Position.RED3));
		legacyChooser.addObject("Blue 1 Shoot No Camera",
			new AutonEncoderToPegShoot(AutonEncoderToPegShoot.Position.BLUE1));
		legacyChooser.addObject("Red 3 Shoot No Camera",
			new AutonEncoderToPegShoot(AutonEncoderToPegShoot.Position.RED3));
		legacyChooser.addObject("Blue 1 Charge No Camera",
			new AutonEncoderToPegCharge(AutonEncoderToPegCharge.Position.BLUE1));
		legacyChooser.addObject("Blue 3 Charge No Camera",
			new AutonEncoderToPegCharge(AutonEncoderToPegCharge.Position.BLUE3));
		legacyChooser.addObject("Red 1 Charge No Camera",
			new AutonEncoderToPegCharge(AutonEncoderToPegCharge.Position.RED1));
		legacyChooser.addObject("Red 3 Charge No Camera",
			new AutonEncoderToPegCharge(AutonEncoderToPegCharge.Position.RED3));

		SmartDashboard.putData("Auto Legacy", legacyChooser);
	}

	@Override
	public void teleopInit() {
		SmartDashboard.putBoolean("captureImage", false);

		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		// RobotMap.compressor.start();
		// createTestButtons();

		// Scheduler.getInstance().add(aim); // TODO add other commands
		CameraServer.getInstance().startAutomaticCapture();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		// poseEstimator.updatePose();
		dumpSmartDashboardValues();
		Scheduler.getInstance().run();
	}

	public void createTestButtons() {
		/** These buttons are obsolete */
		// SmartDashboard.putData("MoveTimeForward", new DriveStraightTrapezoid(true,
		// SmartDashboard.getNumber("aMax", 0),
		// SmartDashboard.getNumber("Duration", 0),
		// DriveStraightTrapezoid.Direction.FORWARD));
		// SmartDashboard.putData("MoveTimeAntiforward",
		// new DriveStraightTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
		// SmartDashboard.getNumber("Duration", 0),
		// DriveStraightTrapezoid.Direction.ANTIFORWARD));
		// SmartDashboard.putData("DiffTimeClockWise",
		// new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
		// SmartDashboard.getNumber("Duration", 0),
		// SmartDashboard.getNumber("DiffPercent", 0),
		// DriveDiffTrapezoid.DiffDirection.CLOCKWISE));
		// SmartDashboard.putData("DiffTimeAntiClockWise",
		// new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
		// SmartDashboard.getNumber("Duration", 0),
		// SmartDashboard.getNumber("DiffPercent", 0),
		// DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE));
		// SmartDashboard.putData("DiffTimeClockWiseBack",
		// new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
		// SmartDashboard.getNumber("Duration", 0),
		// SmartDashboard.getNumber("DiffPercent", 0),
		// DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK));
		// SmartDashboard.putData("DiffTimeAntiClockWiseBack",
		// new DriveDiffTrapezoid(true, SmartDashboard.getNumber("aMax", 0),
		// SmartDashboard.getNumber("Duration", 0),
		// SmartDashboard.getNumber("DiffPercent", 0),
		// DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISEBACK));
		// SmartDashboard.putData("SetMotionControllerConstants", new
		// SetMotionControllerConstants());
		//
		// SmartDashboard.putData("MotionEncoder", new DriveStraightByEncoder(true, 0,
		// 0, 0));
		// SmartDashboard.putData("TurnByEncoder", new DriveTurnByEncoder(true, 0, 0,
		// 0));
		// SmartDashboard.putData("TurnBySpline", new DriveSplineByEncoder(true, 0, 0,
		// 0, 0));

		SmartDashboard.putData("testShooterPower", new TestShooterPower());
	}

	@Override
	public void testInit() {
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

	private void dumpSmartDashboardValues() {
		drivetrain.dumpSmartdashboardValues();
		poseEstimator.dumpSmartdashboardValues();
		shooter.dumpValuesToSmartDashboard();
		climber.dumpSmartdashboardValues();
	}
}
