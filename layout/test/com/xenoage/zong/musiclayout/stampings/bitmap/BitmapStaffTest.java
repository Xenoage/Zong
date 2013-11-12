package com.xenoage.zong.musiclayout.stampings.bitmap;

import static org.junit.Assert.*;

import com.xenoage.utils.graphics.Units;
import com.xenoage.utils.math.Delta;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;

import org.junit.Test;


/**
 * Test cases for a {@link BitmapStaff}.
 *
 * @author Andreas Wenger
 */
public class BitmapStaffTest
{

  @Test public void test8Pixel1()
  {
    //1mm -> 1px, 5 lines, interline: 2 mm. Displayed staff must be 8px high.
    float scaling = Units.pxToMm(1, 1);
    BitmapStaff ss = new BitmapStaff(5, 2, 0.1f, scaling);
    assertEquals(Units.pxToMm(8, scaling), ss.heightMm, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(0, scaling), ss.yOffsetMm, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(1, scaling), ss.heightScaling, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(2, scaling), ss.interlineSpaceMm, Delta.DELTA_FLOAT);
  }
  
  
  @Test public void test16Pixel()
  {
    //1mm -> 2px, 5 lines, interline: 1.8 mm. Displayed staff must be 16px high.
    float scaling = Units.pxToMm(2, 1);
    BitmapStaff ss = new BitmapStaff(5, 1.8f, 0.1f, scaling);
    assertEquals(Units.pxToMm(16, scaling), ss.heightMm, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(-1, scaling), ss.yOffsetMm, Delta.DELTA_FLOAT);
    assertEquals(16 / (2 * 4 * 1.8f), ss.heightScaling, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(4, scaling), ss.interlineSpaceMm, Delta.DELTA_FLOAT);
  }
  
  
  @Test public void test12Pixel()
  {
    //1mm -> 1px, 5 lines, interline: 3.4 mm. Displayed staff must be 12px high.
    float scaling = Units.pxToMm(1, 1);
    BitmapStaff ss = new BitmapStaff(5, 3.4f, 0.1f, scaling);
    assertEquals(Units.pxToMm(12, scaling), ss.heightMm, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(1, scaling), ss.yOffsetMm, Delta.DELTA_FLOAT);
    assertEquals(12 / (4 * 3.4f), ss.heightScaling, Delta.DELTA_FLOAT_ROUGH);
    assertEquals(Units.pxToMm(3, scaling), ss.interlineSpaceMm, Delta.DELTA_FLOAT);
  }
  
  
  @Test public void test13Pixel()
  {
    //1mm -> 1px, 10 lines, interline: 1.4 mm. Displayed simplified staff must be (9*1.4)px high.
    float scaling = Units.pxToMm(1, 1);
    BitmapStaff ss = new BitmapStaff(10, 1.4f, 0.1f, scaling);
    assertTrue(ss.isSimplifiedStaff);
    assertEquals(Units.pxToMm(9*1.4f, scaling), ss.heightMm, Delta.DELTA_FLOAT_ROUGH);
    assertEquals(Units.pxToMm(0, scaling), ss.yOffsetMm, Delta.DELTA_FLOAT);
    assertEquals(1, ss.heightScaling, Delta.DELTA_FLOAT);
    assertTrue(ss.interlineSpaceMm < 2); //simplified staff
  }
  
  
  @Test public void test2Pixel()
  {
    //1mm -> 1px, 5 lines, interline: 0.55 mm. Displayed simplified staff must be (4*0.55)px high.
    float scaling = Units.pxToMm(1, 1);
    BitmapStaff ss = new BitmapStaff(5, 0.55f, 0.1f, scaling);
    assertTrue(ss.isSimplifiedStaff);
    assertEquals(Units.pxToMm(4*0.55f, scaling), ss.heightMm, Delta.DELTA_FLOAT);
    assertEquals(Units.pxToMm(0, scaling), ss.yOffsetMm, Delta.DELTA_FLOAT);
    assertEquals(1, ss.heightScaling, Delta.DELTA_FLOAT);
    assertTrue(ss.interlineSpaceMm < 2); //simplified staff
  }
  
}