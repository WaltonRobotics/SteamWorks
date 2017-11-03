package org.usfirst.frc2974.Testbed.autoncommands;

import org.usfirst.frc2974.Testbed.controllers.Pose;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AimAuton extends Command {

  public Pose goal;
  Pose orientation;
  public boolean isDashboard;

  public AimAuton(boolean isDashboard, Pose goal) {
    this.goal = goal;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if (isDashboard) {
      // goal = new Pose(SmartDashboard.getNumber("goalPosLeft", 0),
      // SmartDashboard.getNumber("goalPosRight", 0),
      // SmartDashboard.getNumber("goalX", 0), SmartDashboard.getNumber("goalY", 0),
      // SmartDashboard.getNumber("goalAngle", 0));
    }

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
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {}
}
