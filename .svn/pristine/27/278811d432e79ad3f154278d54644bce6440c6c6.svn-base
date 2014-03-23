package com.bjor.models;

import java.util.HashSet;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Implements IDrinkingPlan.
 * <p>
 * Gives very simple linear plan.
 * 
 * @author Arnar P&#225ll Birgisson
 * @author Palli Gunnarsson
 *
 */

public class LinearPlan implements IPlan {
	
	/** Dataset of points (BAC, t) to be plotted on a graph  */
	private XYSeries bacSeries;
	/** Dataset of points, denoting drinks */
	private XYSeries drinkSeries;
	/** Dataset containing all the dataset series */
	private XYMultipleSeriesDataset dataset;
	/** Duration of drinking plan in minutes */
	private int minutesDuration;
	/** Gender of user */
	private Gender gender;
	/** Weight of user */
	private float weight;
	/** Number of drinks in a drinking plan	 */
	private int numberOfDrinks;
	/** An instance of Shared Preferences */
	private SharedPreferences sharedPref;
	/** HashSet used to store indices of drinks */
	private HashSet<Integer> drinkSet;
	/** Type of drink for initial plan */
	private Drink planDrinkType;

	/**
	 * Creates an instance LinearDrinkingPlan
	 * 
	 * @param minutesDuration
	 * @param numberOfDrinks
	 * @param drinkAlcPerc
	 * @param context
	 */
	public LinearPlan (int minutesDuration, int numberOfDrinks, Drink planDrinkType, Context context)
	{
		this(minutesDuration, numberOfDrinks, planDrinkType);
		sharedPref = context.getSharedPreferences(Global.PREFS_NAME, 0);
		this.gender = new Gender( sharedPref.getString("gender", "Male") );
		this.weight = Float.parseFloat( sharedPref.getString("weight", "0") );
	}
	
	/**
	 * Ctor for purpose of unit tests
	 * 
	 * @param minutesDuration
	 * @param gender
	 * @param weight
	 * @param numberOfDrinks
	 * @param drinkAlcPerc
	 */
	public LinearPlan(int minutesDuration, Gender gender, float weight, int numberOfDrinks, Drink planDrinkType) {
		this(minutesDuration, numberOfDrinks, planDrinkType);
		this.gender = gender;
		this.weight = weight;
	}
	
	/**
	 * Private ctor
	 * @param minutesDuration
	 * @param numberOfDrinks
	 */
	private LinearPlan(int minutesDuration, int numberOfDrinks, Drink planDrinkType)
	{
		this.minutesDuration = minutesDuration;
		this.numberOfDrinks = numberOfDrinks;
		this.planDrinkType = planDrinkType;
	}


	/* (non-Javadoc)
	 * @see com.bjor.models.IDrinkingPlan#getDataSet()
	 */
	@Override
	public XYMultipleSeriesDataset getDataSet()
	{
		double numberOfStdDrinks = planDrinkType.SD() * numberOfDrinks;
		double stdDrinksPerTimeUnit = numberOfStdDrinks * Global.GRAPH_RES / minutesDuration;
		
	    dataset = new XYMultipleSeriesDataset();
	    bacSeries = new XYSeries("BAC per time");
	    drinkSeries = new XYSeries("Open drink");
    	drinkSet = new HashSet<Integer>();

    	int timeUnitsBetweenDrinks;
	    if (numberOfDrinks == 0)
	    	timeUnitsBetweenDrinks = Integer.MAX_VALUE;
	    else
	    	timeUnitsBetweenDrinks = (int)(minutesDuration / (numberOfDrinks * Global.GRAPH_RES));
	    
	    for(int i = 0; i < minutesDuration / Global.GRAPH_RES; i++)
	    {
	    	double x = i * Global.GRAPH_RES / Global.MIN_PER_HOUR;
	    	double bac = 0.806 * stdDrinksPerTimeUnit * i * 1.2 / (gender.BW * weight) - (gender.MR * i * Global.GRAPH_RES / Global.MIN_PER_HOUR);
	    	if (bac > 0)
	    		bacSeries.add( x, bac );
	    	else
	    		bacSeries.add( x, 0 );
	    	
	    	if ( numberOfDrinks != 0 && i % timeUnitsBetweenDrinks == 0 ) {
	    		drinkSeries.add(x, bac);
	    		drinkSet.add(i);
	    	}
	    }
	    dataset.addSeries(bacSeries);
	    dataset.addSeries(drinkSeries);
	    
	    return dataset;
	}

	@Override
	public XYMultipleSeriesDataset addDrink(int timeStepsSinceInit, Drink addedDrink) {
		// Calculate increase in BAC with one drink
    	double bacIncr = 0.806 * addedDrink.SD() * 1.2 / (gender.BW * weight);
    	double bacDecrPerTimeUnit = gender.MR * Global.GRAPH_RES / Global.MIN_PER_HOUR;
    	    
    	drinkSeries.add(bacSeries.getX(timeStepsSinceInit), bacSeries.getY(timeStepsSinceInit) + bacIncr);
    	boolean firstRound = true;
    	int k = 1;
    	for(int i = timeStepsSinceInit; i < minutesDuration / Global.GRAPH_RES; i++)
    	{
    		double updBacY = bacSeries.getY(i);
    		if (updBacY <= 0) {
    			updBacY -= bacDecrPerTimeUnit * k;
    			k++;
    		}
			updBacY += bacIncr;
			if (updBacY < 0)
				updBacY = 0;
    		double updBacX = bacSeries.getX(i);
    		bacSeries.remove(i);
    		bacSeries.add(i, updBacX, updBacY);
    		
    		if(firstRound)
    			firstRound = false;
    		else if (drinkSet.contains(i)) {
    			int index = drinkSeries.getIndexForKey(updBacX);
				drinkSeries.remove(index);
				drinkSeries.add(index, updBacX, updBacY);
    		}
    	}
    	return dataset;
	}
}
