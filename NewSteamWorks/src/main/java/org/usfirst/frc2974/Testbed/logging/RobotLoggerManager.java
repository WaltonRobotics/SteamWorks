package org.usfirst.frc2974.Testbed.logging;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RobotLoggerManager {
	/**
	 * A HashMap that holds the FileHandler for each Mode
	 */
	private static final Map<Mode, FileHandler> fileHandlers = new EnumMap<>(Mode.class);

	/**
	 * Boolean meant to determine whether or not when the logger is changed it
	 * should remove all previous Handlers. default true.
	 */
	private static boolean removeExistingHandlers = true;

	/*
	 * Static block that is called when the class is first used. This block add
	 * to the shutdown sequence the act of classing all the open FileChannels
	 */
	static {
		ThreadRuntimeExecutionManager.addRuntimeShutdownHook(() -> {
			setFileHandlerInstance(Mode.LOGGING, ThreadRuntimeExecutionManager.DEFAULT_LOGGER_NAME,
					RobotLoggerLevel.INFORMATION)
							.info("Closing, flushing and removing all File Handlers in the RobotLoggerManager class");

			closeHandlers();
		});
	}

	/**
	 * Closes and flushes all Handlers form the handlers array except the
	 * specified FileHandler
	 *
	 * @param fileHandler
	 *            FileHandler to exempt
	 * @param handlers
	 *            Array of Handlers to close
	 */
	private static synchronized void closeHandler(final FileHandler fileHandler, final Handler... handlers) {
		for (final Handler handler : handlers)
			if (!handler.equals(fileHandler)) {
				handler.flush();
				handler.close();
			}
	}

	/**
	 * Closes and flushes all Handlers form the fileHandlers
	 */
	public static synchronized void closeHandlers() {
		for (final Handler handler : fileHandlers.values()) {
			if(handler != null) {
			handler.flush();
			handler.close();
			}
		}
		
		fileHandlers.clear();		
	}

	/**
	 * Closes and flushes all the Handlers from a specific Logger
	 *
	 * @param logger
	 *            Logger which the Handlers to close
	 */
	private static synchronized void closeHandlers(final Logger logger) {
		final Handler[] handlers = logger.getHandlers();

		for (final Handler handler : handlers) {
			handler.flush();
			handler.close();
		}
	}

	/**
	 * Creates and returns an instance of a FileHandler with all the Default
	 * predefined settings.
	 *
	 * @param mode
	 *            The mode in which the fileHandle will be in (is used to name
	 *            the file)
	 * @return A FileHandler with a file pattern and a SimpleFormatter
	 */
	private static synchronized FileHandler createFileHandler(final Mode mode) {
		final Time time = new Time(System.currentTimeMillis());

		final String directory = "/media/sda1/Logs/" + String.format("%tY-%tm-%td", time, time, time) + '/';

		final File fileDirectory = new File(directory);

		if (fileDirectory.mkdirs() || fileDirectory.exists()) {
			final String filePath = fileDirectory.getAbsolutePath() + File.separator + mode.name() + '_'
					+ String.format("%tHh%tM", time, time) + ".log";

			try {
				final FileHandler fileHandler = new FileHandler(filePath, true);

				final SimpleFormatter fileFormat = createFormat();
				fileHandler.setFormatter(fileFormat);

				return fileHandler;
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Method that returns a SimpleFormatter with the format by which the input
	 * from the Logger that contains the Handler that, has this SimpleFormatter
	 * object will be formatted to when the Handler redirect the input to the
	 * predetermined output file.
	 *
	 * @return SimpleFormatter class instance with the abstract method format
	 *         initialised and returns the format to output to file using the
	 *         FileHandler instance
	 */
	private static synchronized SimpleFormatter createFormat() {
		return new SimpleFormatter() {
			@Override
			public synchronized String format(final LogRecord record) {
				// This is the way the message imputed by the Logger will be
				// output to a file as. If the SimpleFormatter is added to the
				// Handler.
				// The format is: LoggerLevel Time(year-month-day
				// hours(0-23):minutes:seconds) LoggerName (new line)
				// SourceClass::SourceMethod - Message
				return String.format("%-9s %tY-%<tm-%<td %<tH:%<tM:%<tS Logger: %s %s::%s " + "- %s\r\n",
						'[' + record.getLevel().getName() + ']', new Time(record.getMillis()), record.getLoggerName(),
						record.getSourceClassName(), record.getSourceMethodName(), record.getMessage());
			}
		};
	}

	/**
	 * Returns true if the specified Logger contains the specific Handler in its
	 * array for the getHandler()
	 *
	 * @param logger
	 *            Logger to get the Handlers from
	 * @param handler
	 *            Handler to check
	 * @return True if the Logger contains handler. False otherwise
	 */
	private static synchronized boolean loggerContainsHandler(final Logger logger, final Handler handler) {
		final Handler[] handlers = logger.getHandlers();

		for (final Handler handler1 : handlers)
			if (handler1.equals(handler))
				return true;

		return false;
	}

	/**
	 * Closes and removes the all Handlers form the specified Logger except the
	 * specified FileHandler
	 *
	 * @param logger
	 *            The Logger to remove and close the Handlers from
	 * @param fileHandler
	 *            The FileHandler to exempt
	 */
	private static synchronized void removeAndCloseHandlers(final Logger logger, final FileHandler fileHandler) {
		final Handler[] handlers = logger.getHandlers();

		closeHandler(fileHandler, handlers);
		removeHandlers(logger, fileHandler);
	}

	/**
	 * Removes the all Handlers form the specified Logger except the specified
	 * FileHandler
	 *
	 * @param logger
	 *            The Logger to remove and close the Handlers from
	 * @param handler
	 *            The FileHandler to exempt
	 */
	private static synchronized void removeHandlers(final Logger logger, final FileHandler handler) {
		final Handler[] handlers = logger.getHandlers();

		for (final Handler handler1 : handlers)
			if (!handler1.equals(handler))
				logger.removeHandler(handler1);
	}

	public static void setRobotMode(Mode mode)
	{
		robotMode = mode;
	}
	
	private static Mode robotMode = Mode.AUTONOMOUS;
	private static Level robotLevel = RobotLoggerLevel.COMPETITION;
	public void setRobotLevel(Level level)
	{
		robotLevel = level;
	}
	
	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final Logger logger) {
		return setFileHandlerInstance(robotMode, logger, robotLevel);
	}
	
	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final String logger) {
		return setFileHandlerInstance(robotMode, Logger.getLogger(logger));
	}
	
	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final Logger logger, Level level) {
		return setFileHandlerInstance(robotMode, logger, level);
	}
	
	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final String logger, Level level) {
		return setFileHandlerInstance(robotMode, Logger.getLogger(logger), level);
	}
	
	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final Mode mode, final Logger logger) {
		return setFileHandlerInstance(mode, logger, logger.getLevel());
	}

	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @param level
	 *            What level the logger should be changed to
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final Mode mode, final Logger logger, final Level level) {
		FileHandler fileHandler = fileHandlers.get(mode);

		if (fileHandler == null)
			fileHandlers.put(mode, fileHandler = createFileHandler(mode));

		if (fileHandler != null) {
			logger.setLevel(level);

			if (removeExistingHandlers)
				removeAndCloseHandlers(logger, fileHandler);

			if (!loggerContainsHandler(logger, fileHandler))
				logger.addHandler(fileHandler);
		}
		return logger;
	}

	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final Mode mode, final String logger) {
		return setFileHandlerInstance(mode, Logger.getLogger(logger));
	}

	/**
	 * Sets the Logger with the FileHandler that fits its mode.
	 *
	 * @param mode
	 *            Mode to be
	 * @param logger
	 *            Which Logger to change
	 * @param level
	 *            What level the logger should be changed to
	 * @return Returns the Logger
	 */
	public static synchronized Logger setFileHandlerInstance(final Mode mode, final String logger, final Level level) {
		return setFileHandlerInstance(mode, Logger.getLogger(logger), level);
	}

	/**
	 * Sets the value of removeExistingHandlers to be the value that
	 * removeExistingHandlers is.
	 *
	 * @param removeExistingHandlers
	 *            New value that removeExistingHandlers will be
	 */
	public static synchronized void setShouldRemoveHandlers(final boolean removeExistingHandlers) {
		RobotLoggerManager.removeExistingHandlers = removeExistingHandlers;
	}

}
