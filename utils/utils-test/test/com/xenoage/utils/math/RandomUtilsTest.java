package com.xenoage.utils.math;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link RandomUtils}.
 *
 * @author Andreas Wenger
 */
public class RandomUtilsTest {

	@Test public void randomIntTest() {
		int min = 5;
		int max = 8;
		boolean foundMin = false;
		boolean foundMax = false;
		for (int i = 0; i < 100000 && false == (foundMin && foundMax); i++) {
			int rand = RandomUtils.randomInt(min, max);
			foundMin |= (rand == min);
			foundMax |= (rand == max);
			if (rand < min || rand > max)
				fail("got " + rand);
		}
		if (!foundMin || !foundMax)
			fail("did not get min or max");
	}

}
