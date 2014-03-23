package com.bjor.models;

import com.bjor.models.Gender;

/**
 * Class that abstract Blood alcohol calculations
 * @author Arnar P&#225ll Birgisson
 * @since 7/11/2013
 */
public class Widmark {
	
	private static final double BODY_WATER_CONST = 0.806;
	private static final double GRAMS_TO_SWEDISH = 1.2;
	
	/**
	 * Method to calculate blood alcohol content by using Widmark's formula
	 * @param gender
	 * @param SD >= 0
	 * @param weight > 0
	 * @return BAC (Blood alcohol contents)
	 * @throws IllegalArgumentsException
	 */
	public static double BAC(Gender gender, double SD, float weight) throws IllegalArgumentException 
	{
		return BAC(gender, SD, weight, 0.0);
	}
	
	/**
	 * Method to calculate blood alcohol content after drinking period in hours
	 * by using Widmark's formula
	 * @param gender
	 * @param SD >= 0, is number of standard drinks
	 * @param weight > 0 is weight in Kg
	 * @param DP >= 0, is drinking period in hours
	 * @return BAC (Blood alchohol contents)
	 * @throws IllegalArgumentException
	 */
	public static double BAC(Gender gender, double SD, float weight, double DP) throws IllegalArgumentException
	{
		if (SD < 0 || weight <= 0)
			throw new IllegalArgumentException("SD should be >= 0 and weight should be > 0");
		return (BODY_WATER_CONST * SD * GRAMS_TO_SWEDISH) / (gender.BW * weight) - (gender.MR * DP);
	}
	
	public static double SD(Gender gender, double BAC, float weight, double timeLimit)throws IllegalArgumentException 
	{
		if (BAC < 0 || weight <= 0)
			throw new IllegalArgumentException("SD should be >= 0 and weight should be > 0");
		return (BAC + (gender.MR * timeLimit / Global.MIN_PER_HOUR ) )
				 * gender.BW * weight / (BODY_WATER_CONST * GRAMS_TO_SWEDISH );
	}
}
