package com.xenoage.zong.core.util;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.unknown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;
import com.xenoage.zong.io.selection.Cursor;

/**
 * Tests for {@link MeasureElementIterator}.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementIteratorTest {
	
	
	@Test public void test() {
		MeasureElementIterator it = new MeasureElementIterator(createTestScore());
		for (int staff : new int[]{1, 3}) {
			for (int measure : new int[]{1, 3}) {
				for (int element : range(2)) {
					assertTrue(it.hasNext());
					assertEquals("staff " + staff + ", measure " + measure + ", element " + element,
						((Words) it.next()).getText().toString());
					assertEquals(atBeat(staff, measure, unknown, fr(element + 1, 4)), it.getMp());
				}
			} 
		}
		assertFalse(it.hasNext());
	}
	
	/**
	 * Test score with 4 staves and 4 measures. In staves 1 and 3, the measures 1 and 3 have each
	 * three words with text "staff x, measure y, element z" on beats 1/4 and 2/4
	 */
	public static Score createTestScore() {
		Score score = new Score();
		Cursor cursor = new Cursor(score, MP.mp0, true);
		for (int staff : new int[]{1, 3}) {
			for (int measure : new int[]{1, 3}) {
				cursor.setMP(atElement(staff, measure, 0, 0));
				for (int element : range(2)) {
					cursor.write(new Rest(fr(1, 4)));
					cursor.write((MeasureElement) new Words(new UnformattedText(
						"staff " + staff + ", measure " + measure + ", element " + element)));
				}
			}
		}
		return score;
	}

}
