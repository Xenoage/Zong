package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static org.junit.Assert.*;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.position.BMP;

import org.junit.Test;


/**
 * Test cases for a {@link StaffMarks} class.
 *
 * @author Andreas Wenger
 */
public class StaffMarksTest
{
  
	StaffMarks sm = new StaffMarks(0, 0, 0, pvec(
		new MeasureMarks(0, 0, 0, 25, pvec(new BeatOffset(fr(0, 4), 5), new BeatOffset(fr(1, 4), 20))),
		new MeasureMarks(1, 35, 35, 55, pvec(new BeatOffset(fr(0, 4), 40), new BeatOffset(fr(1, 4), 50))),
		new MeasureMarks(2, 55, 55, 75, pvec(new BeatOffset(fr(0, 4), 60), new BeatOffset(fr(3, 4), 70)))));
  
  
  /**
   * Tests the getScorePositionAt method.
   */
  @Test public void getScorePositionAt()
  {
    BMP bmp;
    MeasureMarks[] mm = sm.getMeasureMarks().toArray(new MeasureMarks[0]);
    MeasureMarks lastMm = mm[mm.length - 1];
    //coordinate before first measure must return null
    assertNull(sm.getMPAt(mm[0].getStartMm() - 1));
    //coordinate before first beat in measure 0 must return first beat
    bmp = sm.getMPAt((mm[0].getStartMm() + mm[0].getBeatOffsets().getFirst().getOffsetMm()) / 2);
    assertEquals(0, bmp.measure);
    assertEquals(mm[0].getBeatOffsets().getFirst().getBeat(), bmp.beat);
    //coordinate after last measure must return null
    assertNull(sm.getMPAt(lastMm.getEndMm() + 1));
    //coordinate after last beat in last measure must return last beat
    bmp = sm.getMPAt((lastMm.getBeatOffsets().getLast().getOffsetMm() + lastMm.getStartMm()) / 2);
    assertEquals(mm.length - 1, bmp.measure);
    assertEquals(lastMm.getBeatOffsets().getLast().getBeat(), bmp.beat);
    //coordinate at i-th x-position must return i-th beat
    for (int iMeasure = 0; iMeasure < mm.length; iMeasure++)
    {
    	PVector<BeatOffset> bm = mm[iMeasure].getBeatOffsets();
	    for (int iBeat = 0; iBeat < bm.size(); iBeat++)
	    {
	      bmp = sm.getMPAt(bm.get(iBeat).getOffsetMm());
	      assertEquals(iMeasure, bmp.measure);
	      assertEquals(bm.get(iBeat).getBeat(), bmp.beat);
	    }
    }
    //coordinate between beat 0 and 3 in measure 2 must return beat 3
    bmp = sm.getMPAt((mm[2].getBeatOffsets().get(0).getOffsetMm() +
    	mm[2].getBeatOffsets().get(1).getOffsetMm()) / 2);
    assertEquals(2, bmp.measure);
    assertEquals(mm[2].getBeatOffsets().get(1).getBeat(), bmp.beat);
  }
  
  
  /**
   * Tests the getXMmAt method.
   */
  @Test public void getXMmAt()
  {
  	MeasureMarks[] mm = sm.getMeasureMarks().toArray(new MeasureMarks[0]);
  	MeasureMarks lastMm = mm[mm.length - 1];
  	//beat before first measure and after last measure must return null
  	assertNull(sm.getXMmAt(0 - 1, Fraction._0));
  	assertNull(sm.getXMmAt(mm.length, Fraction._0));
    //beat before first beat in measure 0 must return first beat
  	assertEquals(mm[0].getBeatOffsets().getFirst().getOffsetMm(),
  		sm.getXMmAt(0, mm[0].getBeatOffsets().getFirst().getBeat().sub(fr(1, 4))), Delta.DELTA_FLOAT);
    //beat after last beat in last measure must return last beat
  	assertEquals(lastMm.getBeatOffsets().getLast().getOffsetMm(),
  		sm.getXMmAt(mm.length - 1, lastMm.getBeatOffsets().getLast().getBeat().add(fr(1, 4))), Delta.DELTA_FLOAT);
    //i-th beat must return coordinate at i-th x-position
  	for (int iMeasure = 0; iMeasure < mm.length; iMeasure++)
    {
    	PVector<BeatOffset> bm = mm[iMeasure].getBeatOffsets();
	    for (int iBeat = 0; iBeat < bm.size(); iBeat++)
	    {
	      assertEquals(bm.get(iBeat).getOffsetMm(),
	      	sm.getXMmAt(iMeasure, bm.get(iBeat).getBeat()), Delta.DELTA_FLOAT);
	    }
    }
  }
  

}
