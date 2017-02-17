package org.usfirst.frc2974.Robot2017.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CSVWriter {

	/**
	 * A HashMap that hold all instance of the CSVClass
	 */
	private static final Map<String, CSVWriter> instances = new HashMap<>(1);

	/*
	 * Static block that is called when the class is first used. This block add
	 * to the shutdown sequence the act of classing all the open FileChannels
	 */
	static {
		ThreadRuntimeExecutionManager.addRuntimeShutdownHook(() -> {
			RobotLoggerManager.setFileHandlerInstance(Mode.LOGGING, ThreadRuntimeExecutionManager.DEFAULT_LOGGER_NAME,
					RobotLoggerLevel.INFORMATION).info("Closing all FileChannel instances.");
			closeAll();
		});
	}

	/**
	 * Looks through all the non null instances of CSVWriter in the instance
	 * hashMap and the individually closes the FileChannel in each CSVWriter
	 * instance.
	 */
	public static synchronized void closeAll() {
		instances.values().stream().filter(Objects::nonNull).forEach(CSVWriter::close);
		instances.clear();
	}

	/**
	 * Returns the CSVWriter instance that is defined by the instance name.
	 * Looks through the instance HashMap and checks if the instance exists. If
	 * the instance does not exists then the instance is created and added to
	 * the HashMap
	 *
	 * @param instanceName
	 *            Name of the instance
	 * @return The instance of the CSVWriter
	 */
	public static synchronized CSVWriter getInstance(final String instanceName) {
		if (!instances.containsKey(instanceName))
			instances.put(instanceName, new CSVWriter(instanceName));

		return instances.get(instanceName);
	}

	/**
	 * A new File writer in the java 8 nio package which improves the
	 * performance of writing to a file >250% faster and more efficiently
	 * (apparently). Faster than the BufferedWriter
	 */
	private FileChannel fileWriter;

	/**
	 * Creates the directory if it does not exist already and the .csv file for
	 * the specific instance. Initialises the FileChannel to target the file
	 * wanted.
	 *
	 * @param instanceName
	 *            Sets the name fo the file to be the same as its instance name.
	 */
	private CSVWriter(final String instanceName) {
		final Time time = new Time(System.currentTimeMillis());

		final String fileDirectory = "/media/sda1/CSVLogs/" + String.format("%tY-%tm-%td", time, time, time) + '/';
		final File file = new File(fileDirectory);

		if (file.exists() || file.mkdirs()) {
			final String fileName = String.format("%s%s%tHh%tM", instanceName, "_Query_", time, time) + ".csv";

			final File logFile = new File(fileDirectory + fileName);

			try {
				if (logFile.exists() || logFile.createNewFile())
					try (FileOutputStream out = new FileOutputStream(logFile, true)) {
						fileWriter = out.getChannel();
					}
			} catch (final IOException e) {
				RobotLoggerManager
						.setFileHandlerInstance(Mode.LOGGING, "robot.logging.CSVWriter", RobotLoggerLevel.CRITICAL)
						.severe("Could not create file " + fileName + ". Error: " + e);
			}
		}
	}

	/**
	 * Closes the FileChannel safely. If an error occurs a logging statement is
	 * sent.
	 */
	private synchronized void close() {
		try {
			fileWriter.close();
		} catch (final IOException e) {
			RobotLoggerManager.setFileHandlerInstance(Mode.LOGGING, "robot.logging.CSVWriter")
					.severe("Could not close FileChannel. Error " + e);
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "CSVWriter{" + "fileWriter=" + fileWriter + '}';
	}

	/**
	 * Writes to the file that the FileChannel was channeled to. However first
	 * checks that the FileChannel is not null and the array is not null either.
	 *
	 * @param array
	 *            An array that can contain anything (Ever object is converted
	 *            into a string by using the toString method)
	 * @param <T>
	 *            Anything can be passed inside
	 */
	@SafeVarargs
	public final synchronized <T> void write(final T... array) {
		if (fileWriter != null)
			if (array != null)
				try {
					for (final T e : array)
						fileWriter.write(ByteBuffer.wrap((String.valueOf(e) + ',').getBytes()));
					fileWriter.write(ByteBuffer.wrap("\r\n".getBytes()));
				} catch (final IOException e1) {
					RobotLoggerManager
							.setFileHandlerInstance(Mode.LOGGING, "robot.logging.CSVWriter", RobotLoggerLevel.CRITICAL)
							.severe("Could not write to file. Will skip line and continue. Error: " + e1);
					e1.printStackTrace();

				}
			else
				RobotLoggerManager
						.setFileHandlerInstance(Mode.LOGGING, "robot.logging.CSVWriter", RobotLoggerLevel.CRITICAL)
						.warning("Will not write contents inputted, as value is null");
		else
			RobotLoggerManager
					.setFileHandlerInstance(Mode.LOGGING, "robot.logging.CSVWriter", RobotLoggerLevel.CRITICAL)
					.severe("The FileChannel fileWriter to write to is null, error in initialization possible");
	}
}
