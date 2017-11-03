package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class SetMotionControllerConstants extends Command {

  private boolean isDone = false;

  public SetMotionControllerConstants() {
    System.out.println("Constructing");
    isDone = false;
  }

  @Override
  protected void initialize() {
    System.out.println("Setting constants!");
    Robot.drivetrain.setConstants();

    isDone = true;

  }

  @Override
  protected void execute() {}

  @Override
  protected boolean isFinished() {

    return isDone;

  }

  @Override
  protected void end() {

  }

  @Override
  protected void interrupted() {
    end();

  }

}
