package org.usfirst.frc2974.Testbed.auton;

import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid.DiffDirection;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonDiffRunnable extends CommandGroup {

  public DriveDiffTrapezoid.DiffDirection diffDirection;
  public DriveDiffTrapezoid.DiffDirection antiDiffDirection;
  public Position position;
  public boolean doesShoot;

  public Command toGoalCommand;

  public enum Position {
    LEFT, RIGHT, CENTER
  }

  public AutonDiffRunnable(Position position, boolean doesShoot) {
    this.position = position;
    this.doesShoot = doesShoot;
    switch (position) {

      case CENTER:
        addSequential(new DriveStraightTrapezoid(false, 0.7, 1.1,
            DriveStraightTrapezoid.Direction.ANTIFORWARD));
        addSequential(new DriveDiffTrapezoid(false, 0.5, 0.9, 0.2,
            DriveDiffTrapezoid.DiffDirection.CLOCKWISE));
        addSequential(new DriveStraightTrapezoid(false, 0.7, 1.3,
            DriveStraightTrapezoid.Direction.ANTIFORWARD));// check
        break;

      case LEFT:
        directionAuton(DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISEBACK,
            DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE, new DriveStraightTrapezoid(false, 0.7,
                1.2, DriveStraightTrapezoid.Direction.ANTIFORWARD));
        break;

      case RIGHT:
        directionAuton(DriveDiffTrapezoid.DiffDirection.CLOCKWISEBACK,
            DriveDiffTrapezoid.DiffDirection.CLOCKWISE, new DriveDiffTrapezoid(false, 0.7, 1.8,
                0.65, DriveDiffTrapezoid.DiffDirection.CLOCKWISE));
        break;
    }
    if (doesShoot) {
      // addSequential(new Shoot()); TODO add shooting
    }
  }

  private void directionAuton(DiffDirection diffDirection, DiffDirection antiDiffDirection,
      Command toGoalCommand) {
    addSequential(new DriveDiffTrapezoid(false, 0.7, 1.8, 0.65, diffDirection));// move
                                                                                // onto
                                                                                // peg
    addSequential(new DriveDiffTrapezoid(false, 0.7, 1.2, 0.2, antiDiffDirection));// reverse
                                                                                   // off
                                                                                   // peg
    addSequential(toGoalCommand);
  }
}
