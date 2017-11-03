package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.RobotMap;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonShoot extends Command {

  private double timeAtInit;
  private final double timeAtStop;

  public AutonShoot(double timeAtStop) {
    timeAtInit = Timer.getFPGATimestamp();
    this.timeAtStop = timeAtStop;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // FIXME: Add logic to shoot
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (Timer.getFPGATimestamp() - timeAtInit) >= timeAtStop;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    RobotMap.flywheelMotor.set(0);
    RobotMap.indexer.set(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
