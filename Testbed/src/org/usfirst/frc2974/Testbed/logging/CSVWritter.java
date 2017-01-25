package org.usfirst.frc2974.Testbed.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class CSVWritter {
	private final static HashMap<String, CSVWritter> instances = new HashMap<>();

	private File fileToWrite;
	private Logger logger;

	public static CSVWritter getInstance(String instanceName) {
		if (!instances.containsKey(instanceName))
			instances.put(instanceName, new CSVWritter(instanceName));

		return instances.get(instanceName);
	}

	private CSVWritter(String instanceName) {
		RobotLogging.createFile(Mode.LOGGING, RobotLogging.DEBUGGING);
		logger = Logger.getLogger("robot.logging");

		Time time = new Time(System.currentTimeMillis());

		String directory = "/media/sda1/CSVLogs/" + String.format("%tY-%<tm-%<td", time) + '/';

		File fileDirectory = new File(directory);

		if (fileDirectory.mkdirs() || fileDirectory.exists()) {
			String filePath = fileDirectory.getAbsolutePath() + File.separator
					+ String.format("%s%s%tHh%<tM", instanceName, "_Query_", time) + ".csv";

			fileDirectory = new File(filePath);

			try {
				if ((fileDirectory.createNewFile() || fileDirectory.exists()) && fileDirectory.canWrite()) {
					fileToWrite = fileDirectory;
				}
			} catch (IOException e) {
				logger.severe("Could not create the " + instanceName + " .CSV File.");
				e.printStackTrace();
			}
		}
	}

	// TODO make an Stack Array list meant to change between files
	public <T> void write(T... toWrite) {
		if (fileToWrite != null) {
			if (toWrite != null) {
				try {
					try (BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileToWrite, true)))) {

						StringJoiner stringJoiner = new StringJoiner(",");

						for (T e : toWrite) {
							stringJoiner.add(String.valueOf(e));
						}

						writer.write(stringJoiner.toString() + "\r\n");
					}
				} catch (FileNotFoundException e) {
					logger.warning("Could not create the BufferedWritter " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e1) {
					logger.warning("Could not write to file " + e1.getMessage());
					e1.printStackTrace();
				}
			} else {
				logger.warning("Will not write contents inputted, as value is null");
			}
		} else {
			logger.severe("The file to write to is null, error in initialization possible");
		}
	}
}
