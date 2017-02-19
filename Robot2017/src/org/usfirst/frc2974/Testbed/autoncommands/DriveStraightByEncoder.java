package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.controllers.Motion;
import org.usfirst.frc2974.Testbed.controllers.MotionPathStraight;
import org.usfirst.frc2974.Testbed.controllers.MotionProvider;
import org.usfirst.frc2974.Testbed.controllers.Pose;
import org.usfirst.frc2974.Testbed.logging.RobotLoggerManager;
import org.usfirst.frc2974.Testbed.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightByEncoder extends Command {

	public final double SETTLE_TIME = 1;
	private Drivetrain driveTrain;
	private MotionPathStraight motion;
	private boolean isDashboard;
	public double distance;
	public double speed;
	public double acceleration;

	public DriveStraightByEncoder(boolean isDashboard, double distance, double speed, double acceleration) {
		requires(Robot.drivetrain);
		driveTrain = Robot.drivetrain;
		this.isDashboard = isDashboard;
		this.distance = distance;
		this.speed = speed;
		this.acceleration = acceleration;

		RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").info("Created DriveStraightByEncoder");
	}

	@Override
	protected void initialize() {
		if (isDashboard) {
			distance = SmartDashboard.getNumber("encoderDistance", 0);
			speed = SmartDashboard.getNumber("encoderSpeed", 0);
			acceleration = SmartDashboard.getNumber("encoderAccel", 0);
		}

		System.out.println(String.format("Distance=%f, Speed=%f, Accel=%f", distance, speed, acceleration));

		motion = new MotionPathStraight(distance, speed, acceleration);

		driveTrain.setControllerMotion(motion);
		System.out.println(motion.toString());
		System.out.println("Command starts: Controller enabled = " + driveTrain.getControllerStatus());
	}

	@Override
	protected void execute() {
		if (Timer.getFPGATimestamp() > motion.getFinalTime() + SETTLE_TIME) {
			driveTrain.cancelMotion();
			RobotLoggerManager.setFileHandlerInstance("robot.autoncommands").warning("Timed Out - motion stopped");

		}
	}

	@Override
	protected synchronized boolean isFinished() {
		return !driveTrain.getControllerStatus();
	}

	@Override
	protected void end() {
		System.out.print("Command ends: Controller enabled = " + driveTrain.getControllerStatus());
	}

	@Override
	protected void interrupted() {
		driveTrain.cancelMotion();
	}

	@Override
	public String toString() {
		return String.format("acceleration = %f,distance = %f,speed = %f", acceleration, distance, speed);
	}
}