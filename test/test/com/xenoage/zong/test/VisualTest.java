package com.xenoage.zong.test;

import com.xenoage.zong.core.Score;

/**
 * Interface for tests that can be visually evaluated by a human
 * by showing the test score in a simple viewer.
 * 
 * @author Andreas Wenger
 */
public interface VisualTest {

	public Score getScore();
	
}
