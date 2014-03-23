package com.bjor.models;
/**
 * Class that abstracts alcoholic beverage
 * 
 * @author Arnar P&#225ll Birgisson
 * @since 6/11/2013
 */
public class Drink {
	/** mesured in grams */
	private double _alcoholByWeight;
	/** name of the drink */
	private String _name;
	/** Ration between alcohol volume and weight */
	private final double ABW_PER_ABV = 0.79336;
	
	/** 
	 * Ctor for Drink
	 * @param name
	 * @param volumeMl > 0.0
	 * @param alcoholByVolumePerc >= 0.0
	 * @throws IllegalArgumentException
	 */
	public Drink(String name, int volumeMl, double alcoholByVolumePerc)
		throws IllegalArgumentException
	{
		if (!(volumeMl >= 0.0))
			throw new IllegalArgumentException("Parameter volumeMl should be > 0");
		else if (!(alcoholByVolumePerc >= 0))
			throw new IllegalArgumentException("Parameter alcoholByVolumePerc should be > 0");
		
		this._alcoholByWeight = ABW_PER_ABV * alcoholByVolumePerc * volumeMl;
		this._name = name;
	}
	
	/**
	 * @return This drink total alcohol mesured in grams
	 */
	public double ABW() {
		return _alcoholByWeight;
	}
	
	/**
	 * @return How many standard drinks this drinks equals where standard
	 * 			standard drink is 10g alcohol
	 */
	public double SD() {
		return _alcoholByWeight / Global.G_ALC_PER_STD_DRINK;
	}
	
	/**
	 * @return the name of this drink
	 */
	public String getName() {
		return _name;
	}
}
