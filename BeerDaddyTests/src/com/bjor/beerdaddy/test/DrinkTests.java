package com.bjor.beerdaddy.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.bjor.models.Drink;

public class DrinkTests extends TestCase {

	private Drink beer;
	
	protected void setUp() throws Exception {
		super.setUp();
		int ml500 = 500;
		beer = new Drink("Tuborg Christmas Brew", ml500, 0.05);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		beer = null;
	}

	@SmallTest
	public void testABW() throws Exception {
		Assert.assertEquals(19.834, beer.ABW(), 0.001);
	}
	
	@SmallTest
	public void testSD() throws Exception {
		Assert.assertEquals(1.9834, beer.SD(), 0.001);
	}
}
