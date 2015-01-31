package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musiclayout.notator.chord.accidentals.Strategy;


/**
 * Tests for {@link Strategy}.
 * 
 * @author Andreas Wenger
 */
public class StrategyTest
	extends TestData {
	
	@Test public void getAccNoteIndexTest() {
		//C#5
		//must have an accidental
		List<Pitch> pitches = alist(pi(0, 1, 5));
		assertEquals(0, Strategy.getAccNoteIndex(pitches, 0, contextC));
		//E4, G#4, C#5 (no accidental at bottom note)
		//must have accidentals on note 1 and 2
		pitches = alist(pi(2, 0, 4), pi(4, 1, 4), pi(0, 1, 5));
		assertEquals(1, Strategy.getAccNoteIndex(pitches, 0, contextC));
		assertEquals(2, Strategy.getAccNoteIndex(pitches, 2, contextC));
	}

}
