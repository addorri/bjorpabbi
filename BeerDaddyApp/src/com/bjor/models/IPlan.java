package com.bjor.models;

import org.achartengine.model.XYMultipleSeriesDataset;

/**
 * Interface for DrinkingPlan objects.
 * 
 * @author Arnar P&#225ll Birgisson
 * @since 14/10/2013
 * 
 */

public interface IPlan {

	/**
	 * Method that returns dataset to plot plan.
	 * 
	 * @return XYMultipleSeriesDataset
	 */
	public abstract XYMultipleSeriesDataset getDataSet();

	/**
	 * Method that adds a new drink, addedDrink, to this plan, at the timeStep
	 * specified in parameter, and returns an updated dataset.
	 * <br>
	 * This getDataSet method must be called before addDrink.
	 * 
	 * @param timeStepsSinceInit, addedDrink
	 * @return
	 */
	public abstract XYMultipleSeriesDataset addDrink(
			int timeStepsSinceInit, Drink addedDrink);

}