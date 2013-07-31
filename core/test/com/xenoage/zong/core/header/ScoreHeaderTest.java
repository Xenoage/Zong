package com.xenoage.zong.core.header;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;


/**
 * Tests for {@link ScoreHeader}.
 * 
 * @author Andreas Wenger
 */
public class ScoreHeaderTest {

	@Test public void getTimeAtOrBeforeTest() {
		//create a little test score, where there is no time signature
		//in the first measure, but a 3/4 time in the second one
		//and a 4/4 in the fourth one
		Score score = ScoreFactory.create1Staff4Measures();
		ScoreHeader header = score.getHeader();
		Time time1 = new Time(TimeType.time_3_4);
		header.getColumnHeader(1).setTime(time1);
		Time time2 = new Time(TimeType.time_4_4);
		header.getColumnHeader(3).setTime(time2);
		//test method
		assertEquals(Time.implicitSenzaMisura, header.getTimeAtOrBefore(0));
		assertEquals(time1, header.getTimeAtOrBefore(1));
		assertEquals(time1, header.getTimeAtOrBefore(2));
		assertEquals(time2, header.getTimeAtOrBefore(3));
	}

}
