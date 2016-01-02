package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.utils.math.Fraction;

/**
 * Simple {@link ElementSpacing} for testing purposes.
 * 
 * @author Andreas Wenger
 */
public class TestSpacing
	extends ElementSpacing {

	public TestSpacing(Fraction beat, float xIs) {
		super(beat, xIs);
	}
	
	public static TestSpacing spacing(Fraction beat, float xIs) {
		return new TestSpacing(beat, xIs);
	}

}
