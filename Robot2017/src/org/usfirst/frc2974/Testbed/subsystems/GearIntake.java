package org.usfirst.frc2974.Testbed.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.GearIntakeControl;

/**
 *
 */
public class GearIntake extends Subsystem {

	private Solenoid piston;

	private DigitalInput gearSensor;

	public GearIntake() {
		piston = RobotMap.flapCylinder;
		this.gearSensor = RobotMap.gearSensor;

	}

	public static void declarePrefs(boolean reset) {
		Preferences pref = Preferences.getInstance();
		if (reset || !pref.containsKey("gearintake.hasSensor")) {
			pref.putBoolean("gearintake.hasSensor", false);
		}
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new GearIntakeControl());
	}

	public void setPiston(boolean deployed) {
		piston.set(deployed);
	}

	public boolean hasGear() {

		if (Preferences.getInstance().getBoolean("gearintake.hasSensor", false)) {
			return !gearSensor.get();
		}
		return false;
	}
}
