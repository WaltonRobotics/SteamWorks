package org.usfirst.frc2974.Testbed.auton;

import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveStraightTrapezoid;
import org.usfirst.frc2974.Testbed.autoncommands.TurnTrapazoid;
import org.usfirst.frc2974.Testbed.autoncommands.DriveDiffTrapezoid.DiffDirection;
import org.usfirst.frc2974.Testbed.autoncommands.TurnTrapazoid.Direction;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonRunnable extends CommandGroup {

	public TurnTrapazoid.Direction turnDirection;
	public TurnTrapazoid.Direction antiTurnDirection;
	public DriveDiffTrapezoid.DiffDirection diffDirection;

	public double timeToGoal;
	public double turnTimeToGoal;

	public enum Position {
		LEFT, RIGHT, CENTER
	}

	public AutonRunnable(Position position, boolean doesShoot) {

		if (position == Position.CENTER) {
			addSequential(new DriveStraightTrapezoid(false, 0.7, 1.1, DriveStraightTrapezoid.Direction.FORWARD));
			addSequential(new DriveStraightTrapezoid(false, 0.5, 0.7, DriveStraightTrapezoid.Direction.ANTIFORWARD));
			addSequential(new TurnTrapazoid(false, 0.4, 0.25, TurnTrapazoid.Direction.CLOCKWISE));
			addSequential(new DriveStraightTrapezoid(false, 0.4, 2, DriveStraightTrapezoid.Direction.FORWARD));
		} else {
			if (position == Position.LEFT) {
				turnDirection = TurnTrapazoid.Direction.ANTICLOCKWISE;
				antiTurnDirection = TurnTrapazoid.Direction.CLOCKWISE;
				diffDirection = DriveDiffTrapezoid.DiffDirection.CLOCKWISE;
				timeToGoal = 2;
				turnTimeToGoal = .25;
			} else {
				turnDirection = TurnTrapazoid.Direction.CLOCKWISE;
				antiTurnDirection = TurnTrapazoid.Direction.ANTICLOCKWISE;
				diffDirection = DriveDiffTrapezoid.DiffDirection.ANTICLOCKWISE;
				timeToGoal = .8;
				turnTimeToGoal = .8;
			}
			addSequential(new DriveStraightTrapezoid(false, 1, 0.1, DriveStraightTrapezoid.Direction.FORWARD));// move
																												// forward
																												// enough
																												// to
																												// turn
			addSequential(new TurnTrapazoid(false, 0.4, 0.16, turnDirection));// turn
																				// opposite
																				// angle
																				// of
																				// peg
			addSequential(new DriveStraightTrapezoid(false, 0.4, 0.5, DriveStraightTrapezoid.Direction.FORWARD));// move
																													// halfway
																													// to
																													// peg
			addSequential(new TurnTrapazoid(false, 0.4, 0.16, antiTurnDirection));// turn
																					// to
																					// peg
			addSequential(new DriveStraightTrapezoid(false, 0.4, 0.5, DriveStraightTrapezoid.Direction.FORWARD));// move
																													// to
																													// peg
			addSequential(new DriveStraightTrapezoid(false, 0.4, 0.5, DriveStraightTrapezoid.Direction.ANTIFORWARD));// reverse
																														// off
																														// peg
			addSequential(new TurnTrapazoid(false, 0.4, turnTimeToGoal, turnDirection));// turn
																						// to
																						// goal
			addSequential(new DriveStraightTrapezoid(false, 0.4, timeToGoal, DriveStraightTrapezoid.Direction.FORWARD));// move
																														// to
																														// goal
			if (doesShoot) {
				// addSequential(new Shoot()); TODO add shooting
			}
		}
	}
}
