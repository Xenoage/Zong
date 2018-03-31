package com.xenoage.utils.collections;

import org.junit.Test;

import java.util.ArrayList;

import static com.xenoage.utils.collections.CList.clist;
import static org.junit.Assert.*;

/**
 * Tests for {@link CList}.
 * 
 * @author Andreas Wenger
 */
public class CListTest {

	/**
	 * Tests the branching feature of the {@link CList} class.
	 */
	@Test public void testBranch() {
		//test branching without modifications (very fast) - should be more than 100 times as fast
		long[] t = performBranchTest(false);
		assertTrue(t[0] / 100 > t[1]);
		//test branching with modifications (about as fast as normal copies)
		t = performBranchTest(true);
		double difference = Math.abs(1.0 * t[0] / t[1] - 1);
		double allowedDifference = 0.5; //0.05 always worked in Eclipse, but on build server this may vary
		assertTrue("difference is " + difference, difference < allowedDifference);
	}

	/**
	 * Measures the time for both a non-branching and a branching test.
	 * Returns the time in ms for the non-branching (index 0) and the branching (index 1) test.
	 * @param modify  true, if created list should be modified
	 */
	private long[] performBranchTest(boolean modify) {
		//branches must be (if not modified) much faster than normal copies
		int testsCount = 100000;
		//prepare data
		ArrayList<Integer> originalA = new ArrayList<>();
		for (int i = 0; i < 20000; i++)
			originalA.add(i);
		CList<Integer> originalI = new CList<>();
		originalI.addAll(originalA);
		originalI.close();
		//run test without branching
		CList<Integer> cl = new CList<>();
		long startTimeNoBranch = System.currentTimeMillis();
		for (int i = 0; i < testsCount; i++) {
			cl = clist(originalA);
			if (modify) {
				for (int j = 0; j < 10; j++)
					cl.set(j, j * 5);
			}
		}
		long durationNoBranch = System.currentTimeMillis() - startTimeNoBranch;
		//same test with branching
		cl = new CList<>();
		long startTimeBranch = System.currentTimeMillis();
		for (int i = 0; i < testsCount; i++) {
			cl = clist(originalI);
			if (modify) {
				for (int j = 0; j < 10; j++)
					cl.set(j, j * 5);
			}
		}
		long durationBranch = System.currentTimeMillis() - startTimeBranch;
		cl.set(0, cl.get(0)); //just that the compiler can not simplify something
		return new long[] { durationNoBranch, durationBranch };
	}

}
