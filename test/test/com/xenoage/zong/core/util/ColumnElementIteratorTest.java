package com.xenoage.zong.core.util;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.barline.Barline.barline;
import static com.xenoage.zong.core.music.barline.Barline.barlineRegular;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightLight;
import static com.xenoage.zong.core.music.barline.BarlineStyle.Regular;
import static com.xenoage.zong.core.music.time.TimeType.time_4_4;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mp;
import static com.xenoage.zong.core.position.MP.unknown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.xenoage.zong.core.music.time.TimeSignature;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.selection.Cursor;

/**
 * Tests for {@link ColumnElementIterator}.
 * 
 * @author Andreas Wenger
 */
public class ColumnElementIteratorTest {
	
	
	@Test public void test() {
		ColumnElementIterator it = new ColumnElementIterator(createTestScore());
		for (int measure : new int[]{1, 3}) {
			//middle barline is reported first (known beat)
			assertTrue(it.hasNext());
			ColumnElement e = it.next();
			assertEquals(atBeat(unknown, measure, unknown, fr(1, 4)), it.getMp());
			assertEquals(Regular, ((Barline) e).getStyle());
			//time signature
			assertTrue(it.hasNext());
			e = it.next();
			assertEquals(atMeasure(unknown, measure), it.getMp());
			assertEquals(time_4_4, ((TimeSignature) e).getType());
			//end barline
			assertTrue(it.hasNext());
			e = it.next();
			assertEquals(atMeasure(unknown, measure), it.getMp());
			assertEquals(LightLight, ((Barline) e).getStyle());
		} 
		assertFalse(it.hasNext());
	}
	
	/**
	 * Test score with 4 measures. The measures 1 and 3 have each
	 * a time signature, a middle barline at 1/4 and an end barline.
	 */
	public static Score createTestScore() {
		Score score = new Score();
		Cursor cursor = new Cursor(score, MP.mp0, true);
		for (int measure : new int[]{1, 3}) {
			cursor.setMp(mp(unknown, measure, 0, _0, 0));
			cursor.write(new TimeSignature(time_4_4));
			cursor.setMp(mp(unknown, measure, unknown, fr(1, 4), 0));
			cursor.write(barlineRegular());
			ColumnHeader column = score.getColumnHeader(measure);
			column.setEndBarline(barline(LightLight));
		}
		return score;
	}

}
