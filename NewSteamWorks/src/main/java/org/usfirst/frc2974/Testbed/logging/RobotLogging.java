// package org.usfirst.frc2974.Testbed.logging;
//
// import java.io.File;
// import java.io.IOException;
// import java.sql.Time;
// import java.time.Instant;
// import java.util.logging.*;
//
// @Deprecated
// public final class RobotLogging {
// // Level.FINEST includes FINEST FINER FINE INFO WARNING SEVERE
// public static final Level COMPETITION = Level.FINEST;
// // Level.WARNING includes WARNING SEVERE
// public static final Level DEBUGGING = Level.WARNING;
// // Level.INFO includes INFO WARNING SEVERE
// public static final Level INFORMATION = Level.INFO;
// // Level.SEVERE includes SEVERE
// public static final Level CRITICAL = Level.SEVERE;
// // A Handler meant to re-path the input from the Logger
// private static FileHandler fileHandler;
//
// /*
// * This method initialises the global parent logger called "robot" with the
// * specific FileHandler and SimpleFormatter.
// *
// * Logger Level: - Competition: Level.FINEST - Debugging: Level.WARNING -
// * Information: Level.INFO - Critical: Level.SEVERE -
// */
// static synchronized void createFile(final Mode mode, final Level level) {
// // TODO See if using LogManager.reset() is better for resetting the
// // Loggers. For all named loggers, the reset operation removes and
// // closes all Handlers and (except for the root logger, here I believe
// // it would be the "robot" logger is the root) sets the level to null.
// // The root logger's level is set to Level.INFO.
// // LogManager.reset();
//
// // Initialises if has never yet been initialised the global parent/root
// // logger for all the robot subsystems loggers to use.
// Logger globalLogger = Logger.getLogger("robot");
// globalLogger.setLevel(level);
//
// // This checks if there is already a Handler object that exists. If
// // there already exists a Handler object then it is removed from the
// // Logger
// // and is then closed, flushing (exporting all its data) the FileHandler
// // in the same time.
// if (fileHandler != null) {
// globalLogger.removeHandler(fileHandler);
// fileHandler.close();
// }
//
// // This file path is to uncommented when expected to run on the robot
// Time time = new Time(System.currentTimeMillis());
//
// String directory = "/media/sda1/Logs/" + String.format("%tY-%<tm-%<td", time)
// + '/';
//
// File fileDirectory = new File(directory);
//
// // Checks if the directory exists or not if not tries to create it. If
// // the directory is still not created then the rest of the program is
// // skipped.
// if (fileDirectory.mkdirs() || fileDirectory.exists()) {
// // Name of the test file which will be created in the Project Path.
// // This file path is meant to commented when running on the robot.
// // String filePath = "Log.log";
//
// String filePath = fileDirectory.getAbsolutePath()
// /*
// * TODO: Must figure out if it is better to use
// * fileDirectory.getPath(), fileDirectory.getAbsolutePath()
// * or directory. Using the methods from the File class might
// * be better because it automatically formats the path to
// * the correctly with the correct path separator
// */ + File.separator + mode.name() + '_' + String.format("%tHh%<tM", time) +
// ".log";
//
// try {
// fileHandler = new FileHandler(filePath, true);
//
// // Must be put inside because if there is an error then
// // currentHandler will be null so adding the formatter will just
// // make it worst
// SimpleFormatter fileFormat = createFormat();
// // SimpleFormatter fileFormat = new SimpleFormatter();
// fileHandler.setFormatter(fileFormat);
//
// globalLogger.addHandler(fileHandler);
//
// // TODO Must find a way to make it so that if there is an error
// // and we cannot create fileHandler then there is an alternate
// // way of saving the output
// // TODO This is where you might want to continue code
// } catch (SecurityException | IOException e) {
// e.printStackTrace();
// }
// }
// }
//
// /**
// * Method that returns a SimpleFormatter with the format by which the input
// * from the Logger that contains the Handler that, has this SimpleFormatter
// * object will be formatted to when the Handler redirect the input to the
// * predetermined output file.
// *
// * @return SimpleFormatter class instance with the abstract method format
// * initialised and returns the format to output to file using the
// * FileHandler instance
// */
// private static synchronized SimpleFormatter createFormat() {
//
// return new SimpleFormatter() {
// @Override
// public synchronized String format(LogRecord record) {
// // This is the way the message imputed by the Logger will be
// // output to a file as. If the SimpleFormatter is added to the
// // Handler.
// // The format is: LoggerLevel Time(year-month-day
// // hours(0-23):minutes:seconds) LoggerName (new line)
// // SourceClass::SourceMethod - Message
// // TODO: Ask Tim if he feels that this format is
// // sufficient/good. If not Tim must tell Marius his design so
// // that Marius can change it.
// return String.format("%-9s %tY-%<tm-%<td %<tH:%<tM:%<tS Logger: %s %s::%s " +
// "- %s\r\n",
// '[' + record.getLevel().getName() + ']', new Time(record.getMillis()),
// record.getLoggerName(),
// record.getSourceClassName(), record.getSourceMethodName(),
//
// record.getMessage());
// }
// };
// }
// }
