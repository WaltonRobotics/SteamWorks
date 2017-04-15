package org.usfirst.frc2974.Testbed;

import edu.wpi.first.wpilibj.Talon;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static final int CLIMBER_MOTOR_POWER_CHANNEL = 3;

	public static Talon left;
	public static Talon right;
	public static Talon indexer;
	public static Talon intake;
	public static Talon climber;

	public static Talon ballMover;

	public static CANTalon flywheelMotor;

	public static Encoder encoderLeft;
	public static Encoder encoderRight;
	// public static Encoder encoderFlyWheel;

	public static Compressor compressor;
	public static Solenoid pneumaticsShifter;
	public static Solenoid hopperRelease;

	public static Solenoid flapCylinder;

	public static DigitalInput gearSensor;

	public static PowerDistributionPanel powerPanel;

	public static void init() {
		left = new Talon(0);
		right = new Talon(1);
		indexer = new Talon(2);
		intake = new Talon(3);
		climber = new Talon(4);
		flywheelMotor = new CANTalon(1);

		ballMover = new Talon(5);

		compressor = new Compressor();
		pneumaticsShifter = new Solenoid(0);
		hopperRelease = new Solenoid(1);

		flapCylinder = new Solenoid(2);

		encoderRight = new Encoder(new DigitalInput(0), new DigitalInput(1));
		encoderLeft = new Encoder(new DigitalInput(2), new DigitalInput(3));
		// encoderFlyWheel = new Encoder(new DigitalInput(4), new
		// DigitalInput(5));

		gearSensor = new DigitalInput(4);

		powerPanel = new PowerDistributionPanel(0);
	}
}
