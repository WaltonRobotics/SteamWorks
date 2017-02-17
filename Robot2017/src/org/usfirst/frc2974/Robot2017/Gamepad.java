package org.usfirst.frc2974.Robot2017;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 * Wrapper class for the game pad
 * 
 */

public class Gamepad extends Joystick

{

	/**
	 * 
	 * @param port
	 *            the port of the controller
	 * 
	 */

	public Gamepad(int port)

	{

		super(port);

	}

	/**
	 * 
	 * forward is -1 and backward is 1 //TODO check this
	 * 
	 * @return the left thumb stick y value between -1 and 1
	 * 
	 */

	public double getLeftY()

	{

		return getRawAxis(1);

	}

	/**
	 * 
	 * left is 1 right is -1 //TODO check this
	 * 
	 * @return the left thumb stick x value between -1 and 1
	 * 
	 */

	public double getLeftX()

	{

		return getRawAxis(0);

	}

	/**
	 * 
	 * left is 1 right is -1 //TODO check this
	 * 
	 * @return the right thumb stick x value between -1 and 1
	 * 
	 */

	public double getRightX()

	{

		return getRawAxis(4);

	}

	/**
	 * 
	 * forward is -1 and backward is 1 //TODO check this
	 * 
	 * @return the right thumb stick y value between -1 and 1
	 * 
	 */

	public double getRightY()

	{

		return getRawAxis(3);

		// return getRawAxis(5);

	}

	/**
	 * 
	 * 0 is not pressed and 1 is completely pressed
	 * 
	 * @return the left trigger value between 0 and 1
	 * 
	 */

	public double getLeftTrigger()

	{

		return getRawAxis(2);

	}

	/**
	 * 
	 * 0 is not pressed and 1 is completely pressed
	 * 
	 * @return the right trigger value between 0 and 1
	 * 
	 */

	public double getRightTrigger()

	{

		return getRawAxis(3);

	}

	/**
	 * 
	 * @param index
	 *            the index of the button
	 * 
	 *            A( 0 ), B( 1 ), X( 2 ), Y( 3 ), L( 4 ), R( 5 ), BACK( 6 ),
	 *            START( 7 );
	 * 
	 * @return true if button pressed false if not pressed
	 * 
	 */

	public boolean getButton(int index)

	{

		return getRawButton(index);

	}

	/**
	 * 
	 * @param b
	 *            the button to get
	 * 
	 *            A,B,X,Y,L,R,START, or BACK
	 * 
	 * @return
	 * 
	 */

	public boolean getButton(Button b)

	{

		return b.getPressed(this);

	}

	public boolean getPOVButton(int angle)

	{

		return getPOV() == angle;

	}

	/**
	 * 
	 * @param p
	 *            the POV to get based on compass directions
	 * 
	 *            N,S,E,W,NE,NW,SE,SW, or CENTER
	 * 
	 * @return true if the POV button is pressed false if not
	 * 
	 */

	public boolean getPOVButton(POV p)

	{

		return p.getPressed(this);

	}

	/**
	 * 
	 * d-pad buttons enum
	 * 
	 */

	public enum POV

	{

		N(0), S(180), E(90), W(270), NE(45), SE(135), NW(315), SW(225), CENTER(0);

		private int angle;

		private POV(int angle)

		{

			this.angle = angle;
			;

		}

		boolean getPressed(Gamepad g)

		{

			return g.getPOV() == angle;

		}

	}

	/**
	 * 
	 * non d-pad buttons enum
	 * 
	 */

	public enum Button

	{

		A(1), B(2), X(3), Y(4), L(5), R(6), BACK(7), START(8);

		private int index;

		private Button(int index)

		{

			this.index = index;

		}

		boolean getPressed(Gamepad g)

		{

			return g.getRawButton(index);

		}

	}

}