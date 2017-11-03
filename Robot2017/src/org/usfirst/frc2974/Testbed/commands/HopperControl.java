package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HopperControl extends Command {

  public HopperControl() {
    requires(Robot.hopper);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.oi.hopperControl.get()) {// ||Robot.oi.hopperAltControl.get()) {
      Robot.hopper.setPistonDown(true);
    } else {
      Robot.hopper.setPistonDown(false);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
