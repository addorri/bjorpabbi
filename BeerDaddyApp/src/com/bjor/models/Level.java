package com.bjor.models;

/**
 * Abstracts various constants depending on level.
 * 
 * @author P&#225ll Gunnarsson
 * @since 15/10/2013
 */

public class Level {
	/** Lower Blood Alcohol Level */
	public double lowerBAC;
	/** Upper Blood Alcohol Level */
	public double upperBAC;
	/** Ratio that determines how fast a level is reached */
	public double ratioToLower;
	
	/**
	 * Creates an instance of Level
	 * 
	 * @param i is int 0 < i < 4
	 * @throws IllegalArgumentException
	 */
	public Level(int i) throws IllegalArgumentException
	{
		if (i == 1)
		{
			this.lowerBAC = 0.05;
			this.upperBAC = 0.1;
			this.ratioToLower = 1.0/4.0;
		}
		else if (i == 2)
		{
			this.lowerBAC = 0.1;
			this.upperBAC = 0.15;
			this.ratioToLower = 1.0/2.0;
		}
		else if (i == 3)
		{
			this.lowerBAC = 0.15;
			this.upperBAC = 0.2;
			this.ratioToLower = 3.0/4.0;
		}
		else
			throw new IllegalArgumentException();
	}
	
	/**
	 * Method that sets the buzz level
	 * @param lower >= 0
	 * @param upper > 0
	 * @throws IllegalArgumentException
	 */
	public void setBAC(double lower, double upper) throws IllegalArgumentException
	{
		if (lower < 0 || upper <= 0)
			throw new IllegalArgumentException();
		this.lowerBAC = lower;
		this.upperBAC = upper;
	}
}
