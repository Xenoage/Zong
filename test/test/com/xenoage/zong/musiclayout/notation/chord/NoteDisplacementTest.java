package com.xenoage.zong.musiclayout.notation.chord;

/**
 * Tests for {@link NoteDisplacement}.
 * 
 * @author Andreas Wenger
 */
public class NoteDisplacementTest {
	
	public static NoteDisplacement note(int lp) {
		return new NoteDisplacement(lp, 0, NoteSuspension.None);
	}
	
	public static NoteDisplacement note(int lp, float offsetIs) {
		return new NoteDisplacement(lp, offsetIs, NoteSuspension.None);
	}
	
	public static NoteDisplacement note(int lp, float offsetIs, NoteSuspension suspension) {
		return new NoteDisplacement(lp, offsetIs, suspension);
	}

}
