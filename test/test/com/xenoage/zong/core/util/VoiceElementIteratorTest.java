package com.xenoage.zong.core.util;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.mp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.selection.Cursor;

/**
 * Tests for {@link VoiceElementIterator}.
 * 
 * @author Andreas Wenger
 */
public class VoiceElementIteratorTest {
	
	
	@Test public void test() {
		VoiceElementIterator it = new VoiceElementIterator(createTestScore());
		for (int staff : range(4)) {
			for (int measure : range(4)) {
				if ((staff == 1 || staff == 3) && (measure == 1 || measure == 3)) {
					//expect two voices with each 4 quarter rests
					for (int voice : range(2)) {
						for (int element : range(4)) {
							assertTrue(it.hasNext());
							VoiceElement e = it.next();
							assertEquals(mp(staff, measure, voice, fr(element, 4), element), it.getMp());
							assertEquals(fr(1, 4), e.getDuration());
						}
					}
				}
				else {
					//expect a full rest
					assertTrue(it.hasNext());
					VoiceElement e = it.next();
					assertEquals(mp(staff, measure, 0, _0, 0), it.getMp());
					assertEquals(fr(1), e.getDuration());
				}
			} 
		}
		assertFalse(it.hasNext());
	}
	
	/**
	 * Test score with 4 staves and 4 measures. In staves 1 and 3, the measures 1 and 3 have each
	 * two voices with 4 quarter rests. The other measures have a single voice and
	 * a full rest.
	 */
	public static Score createTestScore() {
		Score score = new Score();
		Cursor cursor = new Cursor(score, MP.mp0, true);
		for (int staff : range(4)) {
			for (int measure : range(4)) {
				if ((staff == 1 || staff == 3) && (measure == 1 || measure == 3)) {
					//2 voices with each 4 quarter notes
					for (int voice : range(2)) {
						cursor.setMp(atElement(staff, measure, voice, 0));
						for (int i = 0; i < 4; i++)
							cursor.write(new Rest(fr(1, 4)));
					}
				}
				else {
					//full rest
					cursor.setMp(atElement(staff, measure, 0, 0));
					cursor.write(new Rest(fr(1)));
				}
			}
		}
		return score;
	}

}
