package org.usfirst.frc2974.Testbed.logging;

/**
 * Possible modes that the robot is. This enum is used for the logging.
 */
enum Mode {
	TELEOP, AUTONOMOUS, TEST, DISABLED, LOGGING

	/*
	 * There is no need to assign a modeName variable to the enum if the
	 * modeName is the exact same as the enum name. You can just use
	 * someMode.name to return the mode name however it return the EXACT name
	 * meaning capitalization is included
	 */
}
