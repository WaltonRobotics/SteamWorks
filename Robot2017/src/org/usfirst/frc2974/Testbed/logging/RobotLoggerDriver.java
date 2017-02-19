package org.usfirst.frc2974.Testbed.logging;

import java.util.logging.Logger;

public class RobotLoggerDriver {

	/**
	 * Test case output.
	 */
	private static final String SEVERE = "severe";
	/**
	 * Test case output.
	 */
	private static final String WARNING = "warning";
	/**
	 * Test case output.
	 */
	private static final String INFO = "info";
	/**
	 * Test case output.
	 */
	private static final String FINE = "fine";
	/**
	 * Test case output.
	 */
	private static final String FINER = "finer";
	/**
	 * Test case output.
	 */
	private static final String FINEST = "finest";
	/**
	 * Test case logger.
	 */
	private static final String ROBOT = "robot";
	/**
	 * Test case logger.
	 */
	private static final String ROBOT_TEST = "robot.test";
	/**
	 * Test case CSV File name.
	 */
	private static final String TEST = "Test";

	public static void test() {
		testCSVWriter();
		testRobotLogging();
	}

	/**
	 * Tests if the CSV class works
	 */
	private static void testCSVWriter() {
		for (int y = 0; y < 2; y++)
			for (int i = 0; i < 100; i++)
				CSVWriter.getInstance(TEST + y).write(TEST + y, i, i / 2.0);
	}

	/**
	 * Tests if the RobotLogger works in AUTONOMOUS Mode
	 */
	private static void testRobotLoggerAutonomous() {
		// Base test with parent Logger robot with AUTONOMOUS logging.Mode.
		Logger log = RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, Logger.getLogger(ROBOT),
				RobotLoggerLevel.COMPETITION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests if when you use a child of the parent robot Logger it still
		// logs to the same file.
		log = Logger.getLogger(ROBOT_TEST);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Test the DEBUGGING Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, Logger.getLogger(ROBOT),
				RobotLoggerLevel.DEBUGGING);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the INFO Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, Logger.getLogger(ROBOT),
				RobotLoggerLevel.INFORMATION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the SEVERE Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.AUTONOMOUS, Logger.getLogger(ROBOT),
				RobotLoggerLevel.CRITICAL);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);
	}

	/**
	 * Tests if the RobotLogger works in DISABLED Mode
	 */

	private static void testRobotLoggerDisabled() {
		// Base test with parent Logger robot with DISABLED logging.Mode.
		Logger log = RobotLoggerManager.setFileHandlerInstance(Mode.DISABLED, Logger.getLogger(ROBOT),
				RobotLoggerLevel.COMPETITION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests if when you use a child of the parent robot Logger it still
		// logs to the same file.
		log = Logger.getLogger(ROBOT_TEST);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Test the DEBUGGING Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.DISABLED, Logger.getLogger(ROBOT),
				RobotLoggerLevel.DEBUGGING);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the INFO Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.DISABLED, Logger.getLogger(ROBOT),
				RobotLoggerLevel.INFORMATION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the SEVERE Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.DISABLED, Logger.getLogger(ROBOT),
				RobotLoggerLevel.CRITICAL);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);
	}

	/**
	 * Tests if the RobotLogger works in LOGGING Mode
	 */
	private static void testRobotLoggerLogging() {
		// Base test with parent Logger robot with TEST logging.Mode.
		Logger log = RobotLoggerManager.setFileHandlerInstance(Mode.LOGGING, Logger.getLogger(ROBOT),
				RobotLoggerLevel.COMPETITION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests if when you use a child of the parent robot Logger it still
		// logs to the same file.
		log = Logger.getLogger(ROBOT_TEST);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Test the DEBUGGING Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.LOGGING, Logger.getLogger(ROBOT),
				RobotLoggerLevel.DEBUGGING);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the INFO Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.LOGGING, Logger.getLogger(ROBOT),
				RobotLoggerLevel.INFORMATION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the SEVERE Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.LOGGING, Logger.getLogger(ROBOT),
				RobotLoggerLevel.CRITICAL);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);
	}

	/**
	 * Tests if the RobotLogger works in TELEOP Mode
	 */
	private static void testRobotLoggerTeleop() {
		// Base test with parent Logger robot with TELEOP logging.Mode.
		Logger log = RobotLoggerManager.setFileHandlerInstance(Mode.TELEOP, Logger.getLogger(ROBOT),
				RobotLoggerLevel.COMPETITION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests if when you use a child of the parent robot Logger it still
		// logs to the same file.
		log = Logger.getLogger(ROBOT_TEST);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Test the DEBUGGING Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.TELEOP, Logger.getLogger(ROBOT),
				RobotLoggerLevel.DEBUGGING);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the INFO Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.TELEOP, Logger.getLogger(ROBOT),
				RobotLoggerLevel.INFORMATION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the SEVERE Level

		log = RobotLoggerManager.setFileHandlerInstance(Mode.TELEOP, Logger.getLogger(ROBOT),
				RobotLoggerLevel.CRITICAL);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);
	}

	/**
	 * Tests if the RobotLogger works in TEST Mode
	 */
	private static void testRobotLoggerTest() {
		// Base test with parent Logger robot with TEST logging.Mode.
		Logger log = RobotLoggerManager.setFileHandlerInstance(Mode.TEST, Logger.getLogger(ROBOT),
				RobotLoggerLevel.COMPETITION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests if when you use a child of the parent robot Logger it still
		// logs to the same file.
		log = Logger.getLogger(ROBOT_TEST);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Test the DEBUGGING Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.TEST, Logger.getLogger(ROBOT), RobotLoggerLevel.DEBUGGING);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the INFO Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.TEST, Logger.getLogger(ROBOT),
				RobotLoggerLevel.INFORMATION);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);

		// Tests the SEVERE Level
		log = RobotLoggerManager.setFileHandlerInstance(Mode.TEST, Logger.getLogger(ROBOT), RobotLoggerLevel.CRITICAL);

		log.finest(FINEST);
		log.finer(FINER);
		log.fine(FINE);
		log.info(INFO);
		log.warning(WARNING);
		log.severe(SEVERE);
	}

	/**
	 * Tests if the RobotLogger works
	 */
	private static void testRobotLogging() {
		testRobotLoggerAutonomous();
		testRobotLoggerDisabled();
		testRobotLoggerTeleop();
		testRobotLoggerTest();
		testRobotLoggerLogging();
	}
}
