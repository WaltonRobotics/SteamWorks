package org.usfirst.frc2974.Testbed.subsystems;

import org.usfirst.frc2974.Testbed.RobotMap;
import org.usfirst.frc2974.Testbed.commands.HopperControl;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hopper extends Subsystem {

  private Solenoid piston;

  public Hopper() {
    piston = RobotMap.hopperRelease;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new HopperControl());
  }

  public void setPistonDown(boolean down) {
    // FIXME: Disabled for Ashville as solenoid removed
    // piston.set(down);
  }
}
