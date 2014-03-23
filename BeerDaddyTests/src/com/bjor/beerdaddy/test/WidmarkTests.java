package com.bjor.beerdaddy.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.bjor.models.Widmark;
import com.bjor.models.Gender;

public class WidmarkTests extends TestCase {
	private final float kg80 = 80f;
	private final Gender male = new Gender("male");
	
	@SmallTest
	public void testBACgenderSDweight() throws Throwable {
		Assert.assertEquals(0.208448, Widmark.BAC(male, 10, kg80), 0.001);
	}
}
