package org.usfirst.frc2974.Testbed.commands;

import org.usfirst.frc2974.Testbed.Robot;
import org.usfirst.frc2974.Testbed.subsystems.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeCommand extends Command {
	private State state;
	private State saveState;
	public double dumpStart;
	public enum State {
		IntakeStop {
			@Override
			public void run(IntakeCommand intakeCommand) {
				if (Robot.oi.intake.get()) {
					intakeCommand.state = IntakeIn;
				} else if (Robot.oi.outtake.get()) {
					intakeCommand.state = IntakeOut;
				} else if (Robot.oi.dumptake.get()){
					intakeCommand.dumpStart = Timer.getFPGATimestamp();
					intakeCommand.saveState = intakeCommand.state;
					intakeCommand.state = IntakeDump;
				}
				Robot.intake.setIntake(Intake.INTAKE_STOP);
			}
		},
		IntakeIn {
			@Override
			public void run(IntakeCommand intakeCommand) {
				if (Robot.oi.outtake.get()) {
					intakeCommand.state = IntakeOut;
				} else if (Robot.oi.stoptake.get()) {
					intakeCommand.state = IntakeStop;
				}else if (Robot.oi.dumptake.get()){
					intakeCommand.dumpStart = Timer.getFPGATimestamp();
					intakeCommand.saveState = intakeCommand.state;
					intakeCommand.state = IntakeDump;
				}
				Robot.intake.setIntake(Intake.INTAKE_IN);
			}
		},
		IntakeOut {
			@Override
			public void run(IntakeCommand intakeCommand) {
				if (Robot.oi.intake.get()) {
					intakeCommand.state = IntakeIn;
				} else if (Robot.oi.stoptake.get()) {
					intakeCommand.state = IntakeStop;
				}else if (Robot.oi.dumptake.get()){
					intakeCommand.dumpStart = Timer.getFPGATimestamp();
					intakeCommand.saveState = intakeCommand.state;
					intakeCommand.state = IntakeDump;
				}
				Robot.intake.setIntake(Intake.INTAKE_OUT);
			}
		},
		IntakeDump {
			@Override
			public void run(IntakeCommand intakeCommand){
				Robot.intake.setIntake(Intake.INTAKE_OUT);
				if(Timer.getFPGATimestamp()-intakeCommand.dumpStart>0.5){
					intakeCommand.state = intakeCommand.saveState;
				}
			}
		};

		public void run(IntakeCommand intakeCommand) {
		}
	}

	public IntakeCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.intake);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = State.IntakeStop;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		state.run(this);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
