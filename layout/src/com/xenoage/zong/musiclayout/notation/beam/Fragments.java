package com.xenoage.zong.musiclayout.notation.beam;

import java.util.ArrayList;

/**
 * Line fragments of a single secondary beam line (16th line, or 32th line, or ...).
 * The line consists of continuous parts and hooks.
 * 
 * @author Andreas Wenger
 */
public class Fragments
	extends ArrayList<Fragment> {
	
	/**
	 * Creates line fragments initialized to {@link Fragment#None} for
	 * the given number of beamed stems.
	 */
	public Fragments(int stemsCount) {
		super(stemsCount);
		for (int i = 0; i < stemsCount; i++)
			add(Fragment.None);
	}
	
}
