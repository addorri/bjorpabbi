package com.bjor.beerdaddy.test;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.bjor.models.*;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

public class LinearPlanTests extends AndroidTestCase {
	
	private IPlan linearDrinkingPlan;
	private int twoHours = 2 * 60;
	private Gender male = new Gender("Male");
	private float oneHundredKg = 100f;
	private int eightDrinks = 8;
	private int zeroDrinks = 0;
	private static final float stdBeerAlcPerc = 0.05f;
	private static final Drink stdBeer = new Drink("StdBeer", 500, stdBeerAlcPerc);
	
	public LinearPlanTests() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		linearDrinkingPlan = null;
	}
	
	@SmallTest
	public void testGetDataSetReturnsCorrectNoOfDrinks1() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, zeroDrinks,
				new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		XYSeries drinkTime = dataset.getSeriesAt(1);
		Assert.assertEquals(0, drinkTime.getItemCount());
	}
	
	@SmallTest
	public void testGetDataSetReturnsCorrectNoOfDrinks2() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, eightDrinks,
				new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		XYSeries drinkTime = dataset.getSeriesAt(1);
		Assert.assertEquals(8, drinkTime.getItemCount());
		Assert.assertTrue(drinkTime.getX(0) == 0.0);
		Assert.assertTrue(drinkTime.getX(1) == 0.25);
		Assert.assertTrue(drinkTime.getX(2) == 0.50);
		Assert.assertTrue(drinkTime.getX(3) == 0.75);
		Assert.assertTrue(drinkTime.getX(4) == 1.0);
		Assert.assertTrue(drinkTime.getX(5) == 1.25);
		Assert.assertTrue(drinkTime.getX(6) == 1.50);
		Assert.assertTrue(drinkTime.getX(7) == 1.75);
	}
	
	@SmallTest
	public void testGetDataSetRetCorrectBACForBeer() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, eightDrinks, 
				new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertEquals(0.0, bac.getY(bac.getIndexForKey(0)));
		Assert.assertEquals(0.2305992386, bac.getY(bac.getItemCount()-1), 0.01);
	}
	
	@SmallTest
	public void testGetDataSetRetCorrectBACForBrandy() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, eightDrinks, 
				new Drink("Brandy", 100, 0.38));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertEquals(0.0, bac.getY(bac.getIndexForKey(0)));
		Assert.assertEquals(0.3706, bac.getY(bac.getItemCount()-1), 0.01);
	}
	
	@SmallTest
	public void testBACisNeverNegative() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, zeroDrinks,
				new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertFalse(bac.getY(bac.getItemCount()-1) < 0); 
	}
	
	@SmallTest
	public void testAddMethodAddsDrink1() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, eightDrinks,
				new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		dataset = linearDrinkingPlan.addDrink(100, stdBeer);
		XYSeries drinkTime = dataset.getSeriesAt(1);
		Assert.assertEquals(9, drinkTime.getItemCount());
	}
	
	@SmallTest
	public void testAddMethodAddsDrink2() throws Throwable {
		linearDrinkingPlan = new LinearPlan(twoHours, male, oneHundredKg, zeroDrinks,
				new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		XYMultipleSeriesDataset dataset = linearDrinkingPlan.getDataSet();
		dataset = linearDrinkingPlan.addDrink(1, stdBeer);
		XYSeries drinkTime = dataset.getSeriesAt(1);
		Assert.assertEquals(1, drinkTime.getItemCount());
	}
}