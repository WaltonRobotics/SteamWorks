package org.usfirst.frc2974.Robot2017.logging;

public final class ThreadRuntimeExecutionManager {
	/**
	 * Default logger name for when things are executed at shutdown phase
	 */
	public static final String DEFAULT_LOGGER_NAME = "robot.logging.shutdown";

	/**
	 * Creates a new Thread for each Runnable instance and creates an new thread
	 * with it.
	 *
	 * @param runnable
	 *            Runnable instances
	 */
	public static synchronized void addRuntimeShutdownHook(final Runnable... runnable) {
		for (final Runnable runnable1 : runnable)
			Runtime.getRuntime().addShutdownHook(new Thread(runnable1));
	}
}
