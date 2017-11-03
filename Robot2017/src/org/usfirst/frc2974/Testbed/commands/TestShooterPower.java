package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TestShooterPower extends Command {

  public TestShooterPower() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.shooter);

    // SmartDashboard.putNumber("testPower", .1);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    System.out.println("Setting power = " + SmartDashboard.getNumber("testPower", 0.1));
    Robot.shooter.setPowerMode(SmartDashboard.getNumber("testPower", 0.1));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {}

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.shooter.endPowerMode();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
