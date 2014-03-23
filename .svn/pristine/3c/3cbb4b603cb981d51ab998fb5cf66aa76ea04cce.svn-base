package com.bjor.beerdaddy.test;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.bjor.models.*;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

public class LevelPlanTests extends AndroidTestCase {

	private LevelPlan levelPlan;
	private static final int twoHours = 2 * 60;
	private static final int fourHours = 2 * twoHours;
	private static final Gender male = new Gender("Male");
	private static final Gender female = new Gender("Female");
	private static final float oneHundredKg = 100f;
	private static final float sixtyKg = 60;
	private static final Level lowLevel = new Level(1);
	private static final Level midLevel = new Level(2);
	private static final Level highLevel = new Level(3);
	private static final float stdBeerAlcPerc = 0.05f;
	private static final Drink stdBeer = new Drink("StdBeer", 500, stdBeerAlcPerc);
	
	public LevelPlanTests() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		levelPlan = null;
	}
	
	@SmallTest
	public void testPlanHasDrinks() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, lowLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries drinkTime = dataset.getSeriesAt(1);
		Assert.assertTrue(drinkTime.getItemCount() > 0);
	}
	
	@SmallTest
	public void testPlanStartsAtZero() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, lowLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertEquals(0.0, bac.getY(bac.getIndexForKey(0)));
	}
	
	@SmallTest
	public void testPlanEndsInLowBuzz() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, lowLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertTrue(lowLevel.lowerBAC <= bac.getY(bac.getItemCount()-1));
		Assert.assertTrue(lowLevel.upperBAC >= bac.getY(bac.getItemCount()-1));
	}
	
	@SmallTest
	public void testPlanEndsInMidBuzz() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, midLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertTrue(midLevel.lowerBAC <= bac.getY(bac.getItemCount()-1));
		Assert.assertTrue(midLevel.upperBAC >= bac.getY(bac.getItemCount()-1));
	}
	
	@SmallTest
	public void testPlanEndsInHighBuzz() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertTrue(highLevel.lowerBAC <= bac.getY(bac.getItemCount()-1));
		Assert.assertTrue(highLevel.upperBAC >= bac.getY(bac.getItemCount()-1));
	}
	
	@SmallTest
	public void testFemaleNeedsFewerDrinksThanMale() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, highLevel);
		IPlan femalePlan = new LevelPlan(twoHours, female, sixtyKg, highLevel);
		XYMultipleSeriesDataset mDataset = levelPlan.getDataSet();
		XYSeries maleDrinks = mDataset.getSeriesAt(1);
		XYMultipleSeriesDataset fDataset = femalePlan.getDataSet();
		XYSeries femaleDrinks = fDataset.getSeriesAt(1);
		Assert.assertTrue(femaleDrinks.getItemCount() < maleDrinks.getItemCount());
	}
	
	@SmallTest
	public void testBACisNeverNegative() throws Throwable {
		levelPlan = new LevelPlan(twoHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries bac = dataset.getSeriesAt(0);
		for (int i = 0; i < bac.getItemCount(); i++)
		{
			Assert.assertFalse(bac.getY(i) < 0); 
		}
	}
	
	@SmallTest
	public void testAddDrinkSustainsBuzzLevel() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		dataset = levelPlan.addDrink(100, stdBeer);
		XYSeries bac = dataset.getSeriesAt(0);
		Assert.assertTrue(highLevel.lowerBAC <= bac.getY(bac.getItemCount()-1));
		Assert.assertTrue(highLevel.upperBAC >= bac.getY(bac.getItemCount()-1));
	}
	
	public void testAddDrinkBacCorrectAdd() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		dataset = levelPlan.addDrink(100, stdBeer);
		XYSeries bac = dataset.getSeriesAt(0);
		double standardDrinksAdded = stdBeer.SD();
		double bacIncr = Widmark.BAC(male, standardDrinksAdded, oneHundredKg);
		
		Assert.assertTrue(bac.getY(100) + bacIncr == bac.getY(101));
	}
	
	public void testAddDrinkAddsDrink() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		dataset = levelPlan.addDrink(20, stdBeer);
		XYSeries bac = dataset.getSeriesAt(0);
		XYSeries drink = dataset.getSeriesAt(1);
		double x = bac.getX(20);
		Assert.assertFalse(drink.getIndexForKey(x) == -1);
		Assert.assertTrue(drink.getY(drink.getIndexForKey(x)) == bac.getY(20));
		
	}
	
	public void testAddDrinkRemoveBac() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		dataset = levelPlan.addDrink(20, stdBeer);
		XYSeries newBac = dataset.getSeriesAt(0);
		Assert.assertTrue(newBac.getItemCount() == 21);
	}
	
	public void testAddDrinkRemoveDrinks() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		dataset = levelPlan.addDrink(2, stdBeer);
		XYSeries newDrink = dataset.getSeriesAt(1);
		Assert.assertTrue(newDrink.getItemCount() == 1);
	}
	
	public void testAddDrinkCorrectMethod() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		double timeUnitsBetweenDrinksLowerOld = levelPlan.timeUnitsBetweenDrinksLower;
		dataset = levelPlan.addDrink(20, stdBeer);
		double timeUnitsBetweenDrinksNew = levelPlan.timeUnitsBetweenDrinksLower;
		Assert.assertFalse(timeUnitsBetweenDrinksLowerOld == timeUnitsBetweenDrinksNew);
		
		timeUnitsBetweenDrinksLowerOld = levelPlan.timeUnitsBetweenDrinksLower;
		dataset = levelPlan.addDrink(115, stdBeer);
		timeUnitsBetweenDrinksNew = levelPlan.timeUnitsBetweenDrinksLower;
		Assert.assertTrue(timeUnitsBetweenDrinksLowerOld == timeUnitsBetweenDrinksNew);
		
		
	}
	public void testAddDrinkReplacesDrinks() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries oldDrinks = dataset.getSeriesAt(1);
		dataset = levelPlan.addDrink(0, stdBeer);
		XYSeries newDrinks = dataset.getSeriesAt(1);
		for (int i = 1; i < newDrinks.getItemCount() 
		&& i < oldDrinks.getItemCount(); i++)
		{
			Assert.assertFalse(oldDrinks.getY(i) == newDrinks.getY(i));
		}
	}
	
	public void testAddDrinkReplacesBac() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		XYMultipleSeriesDataset dataset = levelPlan.getDataSet();
		XYSeries oldBac = dataset.getSeriesAt(0);
		dataset = levelPlan.addDrink(100, stdBeer);
		XYSeries newBac = dataset.getSeriesAt(0);
		for (int i = 101; i < newBac.getItemCount() &&  i < oldBac.getItemCount(); i++)
		{
			Assert.assertFalse(oldBac.getY(i) == newBac.getY(i));		
		}
	}
	
	public void testAddDrinkResetValues() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		double timeUnitsBetweenDrinksOld = levelPlan.timeUnitsBetweenDrinksLower;
		double slopeOld = levelPlan.slopeToLimit;
		levelPlan.addDrink(50,stdBeer);
		double timeUnitsBetweenDrinksNew = levelPlan.timeUnitsBetweenDrinksLower;
		double slopeNew = levelPlan.slopeToLimit;
		Assert.assertTrue(timeUnitsBetweenDrinksOld < timeUnitsBetweenDrinksNew);
		Assert.assertTrue(slopeOld > slopeNew);
	}
	
	public void testAddDrinkRatioAfterAdd() throws Throwable {
		levelPlan = new LevelPlan(fourHours, male, oneHundredKg, highLevel);
		double timeUnitsBetweenDrinksLower = levelPlan.timeUnitsBetweenDrinksLower;
		double ratioOfDrinkAfter = levelPlan.ratioOfDrinkLower(5,0);
		double ratioCompare = (timeUnitsBetweenDrinksLower - 5)/timeUnitsBetweenDrinksLower;
		Assert.assertEquals(ratioOfDrinkAfter, ratioCompare);
	}
}
		
	
	
	

