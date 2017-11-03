package org.usfirst.frc2974.Testbed.autoncommands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Aim extends Command {

  public enum State {
    AIMING {
      @Override
      public void run(Aim aim) {
        if (/* aimed */false) {
          aim.state = AIMED;
        }
      }
    },
    AIMED {
      @Override
      public void run(Aim aim) {
        if (/* moved */false) {
          aim.state = AIMING;
        }
      }
    };

    public void run(Aim aim) {}
  }

  private State state;

  public Aim() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    // state = state.AIMING;
    state = State.AIMED; // FIXME
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    state.run(this);
  }

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

  public boolean aimed() {
    return state == State.AIMED;
  }
}
