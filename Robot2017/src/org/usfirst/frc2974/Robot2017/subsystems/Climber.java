package org.usfirst.frc2974.Robot2017.subsystems;

import org.usfirst.frc2974.Robot2017.RobotMap;
import org.usfirst.frc2974.Robot2017.commands.ClimberCommand;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {
	
	private static final double CLIMBER_ACTIVE = 1;
	private static final double CLIMBER_INACTIVE = 0;
	
	private Talon climberTalon;
	
	public Climber(){
		climberTalon = RobotMap.climber;
	}

    public void initDefaultCommand() {
        setDefaultCommand(new ClimberCommand());
    }
    
    public void startClimbing(){
    	climberTalon.set(CLIMBER_ACTIVE);
    }
    public void stopClimbing() {
    	climberTalon.set(CLIMBER_INACTIVE);
    }
}

