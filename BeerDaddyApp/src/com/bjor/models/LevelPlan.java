package com.bjor.models;

import java.util.HashSet;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * LevelPlan implements IDrinkingPlan. LevelPlan's purpose is to keep
 * BAC within specific buzz.</br>
 * 
 * 	&nbsp &nbsp &nbsp	|- - - -_-_-_- - 			</br>
 *  bac 				| &nbsp &nbsp &nbsp	 __/ 	&nbsp &nbsp	<- the buzz </br>
 * 	&nbsp &nbsp &nbsp	|- -/- - - - - - 			</br>
 * 	&nbsp &nbsp &nbsp	| /							</br>
 * 	&nbsp &nbsp &nbsp	|/____________________		</br>
 *  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp time
 * 
 * @author Palli Gunnarsson
 *
 */

/**
 * @author Notandi
 *
 */
/**
 * @author Notandi
 *
 */
public class LevelPlan implements IPlan {
	
	/**
	 * Series containing data for graph
	 */
	private XYSeries bacSeries, drinkSeries;
	/**
	 * Dataset containing bac and drinkSeries
	 */
	private XYMultipleSeriesDataset dataset;
	/**
	 * minutes for plan
	 */
	private int minutesDuration;
	/**
	 * input, gender from user
	 */
	private Gender gender;
	/**
	 * input, weight from user
	 */
	private float weight;
	/**
	 * input, buzzlevel from user (1-3)
	 */
	private Level level;
	/**
	 * type of drink in the plan
	 */
	private Drink planDrinkType;
	/**
	 * shared preferences
	 */
	private SharedPreferences sharedPref;
	/**
	 * contains timeunits when drinks are added
	 */
	private HashSet<Integer> drinkSet;
	
	/**
	 * timeunits between drinks in lower and upper parts
	 */
	public int timeUnitsBetweenDrinksLower, timeUnitsBetweenDrinksUpper;
	
	/**
	 * Ratio of standard drinks to drinks
	 */
	private double stdDrinkToDrinkRatio;
	/**
	 * last BAC value of a drink before a limit
	 */
	private double lastBacToLimit;
	/**
	 * last bac value of a drink befor lower&upper limit
	 */
	private double lastBacToLowerLimit, lastBacToUpperLimit;
	/**
	 * last timeunit of a drink before limit
	 */
	private int lastTimeUnitToLimit;
	/**
	 * time units between drinks
	 */
	private int timeUnitsBetweenDrinks;
	
	/**
	 * slope of graph bac vs hours
	 */
	public double slopeToLimit;
	/**
	 * time unit when graphing of a line starts
	 */
	private int timeUnitStart;
	/**
	 * last time unit of line, used to determine slope
	 */
	private int timeUnitEnd;
	
	/**
	 * number of drinks in plan
	 */
	private double nrOfDrinks;
	
	/**
	 * Creates an instance of LevelPlan
	 * 
	 * @param minutesDuration int > 0
	 * @param level string either "1", "2" or "3"
	 * @param context Context of caller
	 */
	public LevelPlan (int minutesDuration, Level level, Drink planDrinkType, Context context)
	{
		this(minutesDuration, level, planDrinkType);
		sharedPref = context.getSharedPreferences(Global.PREFS_NAME, 0);
		this.gender = new Gender( sharedPref.getString("gender", "Male") );
		this.weight = Float.parseFloat( sharedPref.getString("weight", "0") );
		Initialize();
	}

	/**
	 * Creates an instance of LevelPlan for unitTests
	 * 
	 * @param minutesDuration
	 * @param gender
	 * @param weight
	 * @param level
	 */
	public LevelPlan(int minutesDuration, Gender gender, float weight, Level level) {
		this(minutesDuration, level, new Drink("Beer", Global.ML_PER_STD_BEER, Global.STD_BEER_ALC_PERC));
		this.gender = gender;
		this.weight = weight;
		Initialize();
	}
	
	/**
	 * 
	 * Hidden part of the LevelPlan builder
	 * 
	 * @param minutesDuration
	 * @param level
	 * @param planDrinkType
	 */
	private LevelPlan(int minutesDuration, Level level, Drink planDrinkType) {
		this.minutesDuration = minutesDuration;
		this.level = level;
		this.planDrinkType = planDrinkType;
	}
	
	/**
	 * Initializes the graph components and the standard drink to drink ratio
	 */
	private void Initialize() {
		dataset = new XYMultipleSeriesDataset();
	    bacSeries = new XYSeries("BAC per time");
	    drinkSeries = new XYSeries("Open drink");
	    drinkSet = new HashSet<Integer>();
	    
	    drinkSeries.add(0.0, 0.0);
	    drinkSet.add(0);
	    
		// [g alcohl/drink / g alcohol/stdDrink] = [stdDrink/drink]
		stdDrinkToDrinkRatio = planDrinkType.SD();
	}

	/* (non-Javadoc)
	 * @see com.bjor.models.IDrinkingPlan#getDataSet()
	 */
	@Override
	
	public XYMultipleSeriesDataset getDataSet()
	{
		

		
		double timeToLower = minutesDuration * level.ratioToLower;	//minutes for ascend to lower limit of BAC
	
		setUnits(0.0, timeToLower, 0.0, level.lowerBAC);
		createSeries(0.0, level.lowerBAC);
		updateLowerVariables();
		
		setUnits(lastTimeUnitToLimit*Global.GRAPH_RES, minutesDuration, lastBacToLowerLimit, level.upperBAC );
		createSeries(lastBacToLowerLimit, level.upperBAC);
	    
		updateUpperVariables();
		dataset.addSeries(bacSeries);
	    dataset.addSeries(drinkSeries);
	    
	    return dataset;		
	}
	

	
	/**
	 * 
	 * Resets global variables for reusing.
	 * 
	 * @param timeStart
	 * @param timeEnd
	 * @param bacStart
	 * @param bacEnd
	 */
	private void setUnits(double timeStart, double timeEnd, double bacStart, double bacEnd)
	{
		double timeLimit = timeEnd - timeStart;
		double bacLimit = bacEnd - bacStart;
		
		slopeToLimit = bacLimit / (timeLimit/Global.MIN_PER_HOUR);
		
		timeUnitStart = (int)(timeStart/Global.GRAPH_RES);
		timeUnitEnd = (int)(timeEnd/Global.GRAPH_RES);
		int timeUnitLimit = timeUnitEnd - timeUnitStart;
		// [min / (num*drinks*0.5)] = [2*min/(num*drinks)]
		
		setDrinks(timeLimit, bacLimit);
	
		timeUnitsBetweenDrinks = (int)(timeUnitLimit / nrOfDrinks);
		//[min*2]
		double timeUnitFromLastDrink = timeUnitLimit % timeUnitsBetweenDrinks;
		//[min*2]
		lastTimeUnitToLimit = (int)(timeUnitEnd - timeUnitFromLastDrink);
	}
	
	/**
	 * Resets the number of drinks.
	 * 
	 * @param timeLimit
	 * @param bacLimit
	 */
	private void setDrinks(double timeLimit, double bacLimit)
	{
		// [num * stdDrinks]
		double nrOfStdDrinks = Widmark.SD(gender, bacLimit, weight, timeLimit);
		//[num * stdDrinks/ (stdDrinks/drinks)] = [num * drinks]
		nrOfDrinks = nrOfStdDrinks / stdDrinkToDrinkRatio;		
	}
	
	/**
	 * Creates the data series used to make the graph
	 * 
	 * @param bacStart
	 * @param bacEnd
	 */
	private void createSeries(double bacStart, double bacEnd)
	{

	    // breytti úr timeLimit - (int)timeUnitFromLastDrink, breytti úr < í <=
	    for(int i = timeUnitStart ; i <= lastTimeUnitToLimit; i++)
	    {
	    	double x = i * Global.GRAPH_RES / Global.MIN_PER_HOUR;
	    	double y = bacStart + (x - timeUnitStart*Global.GRAPH_RES/Global.MIN_PER_HOUR) * slopeToLimit;
	    	
	    	bacSeries.add( x, y );
	    	
	    	if ( (i - timeUnitStart) % timeUnitsBetweenDrinks == 0 && (i != timeUnitStart))
	    	{
	    		drinkSeries.add(x, y);
	    		drinkSet.add(i);
	    	}
	    }
	    
	    //bætti þessu við [min*2 *0.5 */ min/klst  * BAC/klst] = [BAC]
	    lastBacToLimit = bacSeries.getY(bacSeries.getItemCount() - 1);
	    
	}
	
	/**
	 * Updates the data series that create the graph when a drink is added.
	 * 
	 * @param bacStart
	 * @param bacEnd
	 * @param ratioOfDrinkAfterAdd
	 */
	private void updateSeries(double bacStart, double bacEnd, double ratioOfDrinkAfterAdd)
	{	
 	
	    // breytti úr timeLimit - (int)timeUnitFromLastDrink, breytti úr < í <=
	    for(int i = timeUnitStart ; i <= lastTimeUnitToLimit; i++)
	    {
	    	double x = i * Global.GRAPH_RES / Global.MIN_PER_HOUR;
	    	double y = bacStart + (x - timeUnitStart*Global.GRAPH_RES/Global.MIN_PER_HOUR) * slopeToLimit;
	    	
	    	bacSeries.add( x, y );
	    	if (timeUnitStart != i)
		    {
		    	if ( ratioOfDrinkAfterAdd != 0.0 &&
		    			(i - timeUnitStart) % (int)(timeUnitsBetweenDrinks*ratioOfDrinkAfterAdd) == 0)
		    	{
		    		drinkSeries.add(x,y);
		    		drinkSet.add(i);
		    		ratioOfDrinkAfterAdd = 0.0;
		    	}
		    	else if ( (i - timeUnitStart) % timeUnitsBetweenDrinks == 0)
		    	{
		    		drinkSeries.add(x, y);
		    		drinkSet.add(i);
		    	}
		    }
	    }
	    
	    //bætti þessu við [min*2 *0.5 */ min/klst  * BAC/klst] = [BAC]
	    lastBacToLimit = bacSeries.getY(bacSeries.getItemCount() - 1);
		
	}
	
	/**
	 * Updates global variables that are used in the upper part of graph
	 */
	private void updateUpperVariables()
	{
		this.lastBacToUpperLimit = lastBacToLimit;
		this.timeUnitsBetweenDrinksUpper = timeUnitsBetweenDrinks;
	}
	
	/**
	 * Updates global variables that are used in the lower part of graph
	 */
	private void updateLowerVariables()
	{
		this.lastBacToLowerLimit = lastBacToLimit;
		this.timeUnitsBetweenDrinksLower = timeUnitsBetweenDrinks;
	}
	    
	    	
	
	/* (non-Javadoc)
	 * @see com.bjor.models.IPlan#addDrink(int, com.bjor.models.Drink)
	 */
	@Override
	public XYMultipleSeriesDataset addDrink(int timeStepsSinceInit, Drink addedDrink)
	{
		
		//vantar að láta ratio föllin bara taka drykki úr venjulega planinu. Dno how.
		
		
		double standardDrinksAdded = addedDrink.SD();
		double bacIncr = Widmark.BAC(gender, standardDrinksAdded, weight);
		//double bacIncr = 0.806*1.2/(gender.BW*weight)*alcoholInDrink/Global.G_ALC_PER_STD_DRINK;
		double bacPostAdd = bacSeries.getY(timeStepsSinceInit) + bacIncr;
		
		if (level.upperBAC <= bacPostAdd) return dataset;
		
		int index = removeOld(timeStepsSinceInit);
		
		double hourAdd = timeStepsSinceInit*Global.GRAPH_RES/Global.MIN_PER_HOUR;
		double hourAfterAdd = (timeStepsSinceInit + 1)*Global.GRAPH_RES/Global.MIN_PER_HOUR;
		
		bacSeries.add(hourAfterAdd, bacPostAdd);
		drinkSeries.add(hourAdd, bacSeries.getY(timeStepsSinceInit));
		drinkSet.add(timeStepsSinceInit);
		
		if (lastBacToLowerLimit <= bacPostAdd) addDrinkUpper(timeStepsSinceInit, bacPostAdd, index);
		else if (bacPostAdd <= level.upperBAC) addDrinkLower(timeStepsSinceInit, bacPostAdd, index);
		return dataset;
	}
	
	//Virkar ekki því hann tekur lastLimitToLevel úr efra þótt svo við viljum nota neðra.
	/**
	 * Gives the updated dataseries if the added drink is added in the upper part of the graph
	 * 
	 * @param timeStepsSinceInit
	 * @param bacPostAdd
	 * @param index
	 */
	private void addDrinkUpper(int timeStepsSinceInit, double bacPostAdd, int index)
	{

		
		double ratioOfDrinkAfterAdd = ratioOfDrinkUpper(timeStepsSinceInit, index);
		//Byrja að bæta við gögnum.
		
		setUnits(timeStepsSinceInit*Global.GRAPH_RES, minutesDuration,bacPostAdd,level.upperBAC);
		
		updateSeries(bacPostAdd, level.upperBAC, ratioOfDrinkAfterAdd);
		
		updateUpperVariables();
	}	
	
	/**
	 * 
	 * Calculates the ratio left in drink when a new drink is added in the upper part.
	 * @param timeStepsSinceInit
	 * @param index
	 * @return
	 */
	private double ratioOfDrinkUpper(int timeStepsSinceInit, int index)
	{
		double timeUnitLastDrink = drinkSeries.getX(index)*Global.MIN_PER_HOUR/Global.GRAPH_RES;
		double ratioOfDrinkBeforeAdd = (timeStepsSinceInit - timeUnitLastDrink)/timeUnitsBetweenDrinksUpper;
		return (1 - ratioOfDrinkBeforeAdd);
		
	}
			
	/**
	 * Gives the updated dataseries if the added drink is added in the lower part of the graph
	 * 
	 * @param timeStepsSinceInit
	 * @param bacPostAdd
	 * @param index
	 */
	private void addDrinkLower(int timeStepsSinceInit, double bacPostAdd, int index)
	{

		
		double ratioOfDrinkAfterAdd = ratioOfDrinkLower(timeStepsSinceInit, index);
		
		setUnits(timeStepsSinceInit*Global.GRAPH_RES, minutesDuration*level.ratioToLower,
				bacPostAdd, level.lowerBAC);
		updateSeries(bacPostAdd, level.lowerBAC, ratioOfDrinkAfterAdd);
		
		updateLowerVariables();
		
	
		setUnits(lastTimeUnitToLimit*Global.GRAPH_RES, minutesDuration, lastBacToLowerLimit,
				level.upperBAC );
		updateSeries(lastBacToLimit, level.upperBAC, 0.0);
		
		updateUpperVariables();
	
	}
		
	/**
	 * Calculates the ratio left in drink when a new drink is added in the upper part.
	 * 
	 * @param timeStepsSinceInit
	 * @param index
	 * @return
	 */
	public double ratioOfDrinkLower(int timeStepsSinceInit, int index)
	{
		double timeUnitLastDrink = drinkSeries.getX(index)*Global.MIN_PER_HOUR/Global.GRAPH_RES;
		double ratioOfDrinkBeforeAdd = (timeStepsSinceInit - timeUnitLastDrink)/timeUnitsBetweenDrinksLower;
		return (1 - ratioOfDrinkBeforeAdd);		
	}
	
	
	
	
	/**
	 * Removes the old variables from the dataSeries 
	 * @param timeStepsSinceInit
	 * @return
	 */
	private int removeOld(int timeStepsSinceInit)
	{
		// Removea gögn úr báðum XYSeries og datasettinu.
		int i = timeStepsSinceInit;
		int itemCount = bacSeries.getItemCount();
		for (int k = i + 1; k < itemCount; k++) 
		{
			bacSeries.remove(i+1);
		}
		
		
		//Finn lægsta gildi í datasettinu á bilinu timeStepsSinceInit og minutesDuration/GRAP_RES,
		//deleta svo gildum frá því úr drinkSeries
		
		// Error if drink is added on timeUnit 0.
		// If we opened a drink at the same time we added another we want to make sure we dont delete that drink.
		// But we have to adjust to the fact that use (i + 1) inside the while loop
		while (!drinkSet.contains(i)) i--;
		
		
		double key = i*Global.GRAPH_RES/Global.MIN_PER_HOUR;
		int index = drinkSeries.getIndexForKey(key);
		
		
		for (int k = drinkSeries.getItemCount() - 1; index < k; k--) drinkSeries.remove(k);
		
		drinkSet.clear();
		for (int j = 0; j < drinkSeries.getItemCount(); j++)
		{
			drinkSet.add((int)(drinkSeries.getX(j)*Global.MIN_PER_HOUR/Global.GRAPH_RES));
		}
		return index;
	}
	
	
}
