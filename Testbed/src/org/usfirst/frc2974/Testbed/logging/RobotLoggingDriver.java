package org.usfirst.frc2974.Testbed.logging;

import java.util.logging.Logger;

public class RobotLoggingDriver {

    public static void main(String[] args) {

        test();
    }

    private static void test() {
        // Base test with parent Logger robot with AUTONOMOUS logging.Mode.
        RobotLogging.createFile(Mode.AUTONOMOUS, RobotLogging.COMPETITION);
        Logger log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests if when you use a child of the parent robot Logger it still
        // logs to the same file.
        log = Logger.getLogger("robot.test");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Test the DEBUGGING Level
        RobotLogging.createFile(Mode.AUTONOMOUS, RobotLogging.DEBUGGING);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the INFO Level
        RobotLogging.createFile(Mode.AUTONOMOUS, RobotLogging.INFORMATION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");
        // Tests the SEVERE Level
        RobotLogging.createFile(Mode.AUTONOMOUS, RobotLogging.CRITICAL);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Base test with parent Logger robot with DISABLED logging.Mode.
        RobotLogging.createFile(Mode.DISABLED, RobotLogging.COMPETITION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests if when you use a child of the parent robot Logger it still
        // logs to the same file.
        log = Logger.getLogger("robot.test");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Test the DEBUGGING Level
        RobotLogging.createFile(Mode.DISABLED, RobotLogging.DEBUGGING);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the INFO Level
        RobotLogging.createFile(Mode.DISABLED, RobotLogging.INFORMATION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the SEVERE Level
        RobotLogging.createFile(Mode.DISABLED, RobotLogging.CRITICAL);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Base test with parent Logger robot with TELEOP logging.Mode.
        RobotLogging.createFile(Mode.TELEOP, RobotLogging.COMPETITION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests if when you use a child of the parent robot Logger it still
        // logs to the same file.
        log = Logger.getLogger("robot.test");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Test the DEBUGGING Level
        RobotLogging.createFile(Mode.TELEOP, RobotLogging.DEBUGGING);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the INFO Level
        RobotLogging.createFile(Mode.TELEOP, RobotLogging.INFORMATION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the SEVERE Level
        RobotLogging.createFile(Mode.TELEOP, RobotLogging.CRITICAL);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Base test with parent Logger robot with TEST logging.Mode.
        RobotLogging.createFile(Mode.TEST, RobotLogging.COMPETITION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests if when you use a child of the parent robot Logger it still
        // logs to the same file.
        log = Logger.getLogger("robot.test");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Test the DEBUGGING Level
        RobotLogging.createFile(Mode.TEST, RobotLogging.DEBUGGING);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the INFO Level
        RobotLogging.createFile(Mode.TEST, RobotLogging.INFORMATION);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");

        // Tests the SEVERE Level
        RobotLogging.createFile(Mode.TEST, RobotLogging.CRITICAL);
        log = Logger.getLogger("robot");

        log.finest("finest");
        log.finer("finer");
        log.fine("fine");
        log.info("info");
        log.warning("warning");
        log.severe("severe");
    }
}
