package org.usfirst.frc.team2974.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static Talon left;
	public static Talon right;
	public static Talon indexer;
	public static Talon intake;
	public static Spark climber;

	public static CANTalon flywheelMotor;

	public static Encoder encoderLeft;
	public static Encoder encoderRight;
	public static Encoder encoderFlyWheel;

	public static Compressor compressor;
	public static Solenoid pneumaticsShifter;
	public static Solenoid hopperRelease;

	public static DigitalInput laser;

	public static void init() {
		left = new Talon(0);
		right = new Talon(1);
		indexer = new Talon(2);
		intake = new Talon(3);
		flywheelMotor = new CANTalon(1);

		compressor = new Compressor();
		pneumaticsShifter = new Solenoid(0);
		hopperRelease = new Solenoid(1);

		encoderRight = new Encoder(new DigitalInput(0), new DigitalInput(1));
		encoderLeft = new Encoder(new DigitalInput(2), new DigitalInput(3));
		encoderFlyWheel = new Encoder(new DigitalInput(4), new DigitalInput(5));

		// laser = new DigitalInput();
	}
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
