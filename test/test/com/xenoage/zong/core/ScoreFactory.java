package com.xenoage.zong.core;

import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.music.Part;


/**
 * Helper class for quickly creating scores for testing purposes.
 * 
 * @author Andreas Wenger
 */
public class ScoreFactory {
	
	
	/**
	 * Creates a score with a single staff and 1 empty measure.
	 */
	public static Score create1Staff() {
		Score score = new Score();
		new PartAdd(score, new Part("", null, 1, null), 0, null).execute();
		new MeasureAdd(score, 1).execute();
		return score;
	}
	
	
	/**
	 * Creates a score with a single staff and 4 empty measures.
	 */
	public static Score create1Staff4Measures() {
		Score score = create1Staff();
		new MeasureAdd(score, 3).execute();
		return score;
	}

}
