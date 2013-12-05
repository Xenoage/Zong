package com.xenoage.zong.core.music.util;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.DurationInfo.getBaseDuration;
import static com.xenoage.zong.core.music.util.DurationInfo.getDots;
import static com.xenoage.zong.core.music.util.DurationInfo.getDuration;
import static com.xenoage.zong.core.music.util.DurationInfo.getFlagsCount;
import static org.junit.Assert.*;

import org.junit.Test;

import com.xenoage.zong.core.music.util.DurationInfo;


/**
 * Test cases for a {@link DurationInfo} class.
 *
 * @author Andreas Wenger
 */
public class DurationInfoTest
{
  
  @Test public void getFlagsCountTest()
  {
    //simple notes
    assertEquals(0, getFlagsCount(fr(1, 4)));
    assertEquals(1, getFlagsCount(fr(1, 8)));
    assertEquals(2, getFlagsCount(fr(1, 16)));
    assertEquals(3, getFlagsCount(fr(1, 32)));
    assertEquals(4, getFlagsCount(fr(1, 64)));
    assertEquals(5, getFlagsCount(fr(1, 128)));
    assertEquals(3, getFlagsCount(fr(2, 64)));
    //single dotted quarter note
    assertEquals(0, getFlagsCount(fr(3, 8)));
    //single dotted eighth note
    assertEquals(1, getFlagsCount(fr(3, 16)));
    //double dotted quarter note
    assertEquals(0, getFlagsCount(fr(7, 16))); 
    //whole note
    assertEquals(0, getFlagsCount(fr(1, 1)));
  }
  
  
  @Test public void getDurationTest()
  {
  	//no dots
  	assertEquals(fr(1, 2), getDuration(fr(1, 2), 0));
  	assertEquals(fr(1, 4), getDuration(fr(1, 4), 0));
    //one dot
    assertEquals(fr(3, 4), getDuration(fr(1, 2), 1));
    assertEquals(fr(3, 8), getDuration(fr(1, 4), 1));
    //two dots
    assertEquals(fr(7, 8), getDuration(fr(1, 2), 2));
    assertEquals(fr(7, 16), getDuration(fr(1, 4), 2));
  }

  
  @Test public void getDotsTest()
  {
  	//no dots
  	assertEquals(0, getDots(fr(1, 1)));
  	assertEquals(0, getDots(fr(1, 2)));
  	assertEquals(0, getDots(fr(1, 4)));
    assertEquals(0, getDots(fr(1, 8)));
    assertEquals(0, getDots(fr(1, 16)));
    assertEquals(0, getDots(fr(1, 32)));
    //one dot
    assertEquals(1, getDots(fr(1, 1).add(fr(1, 2))));
    assertEquals(1, getDots(fr(1, 2).add(fr(1, 4))));
    assertEquals(1, getDots(fr(1, 4).add(fr(1, 8))));
    assertEquals(1, getDots(fr(1, 8).add(fr(1, 16))));
    assertEquals(1, getDots(fr(1, 16).add(fr(1, 32))));
    assertEquals(1, getDots(fr(1, 32).add(fr(1, 64))));
    //two dots
    assertEquals(2, getDots(fr(1, 1).add(fr(1, 2)).add(fr(1, 4))));
    assertEquals(2, getDots(fr(1, 2).add(fr(1, 4)).add(fr(1, 8))));
    assertEquals(2, getDots(fr(1, 4).add(fr(1, 8)).add(fr(1, 16))));
    assertEquals(2, getDots(fr(1, 8).add(fr(1, 16)).add(fr(1, 32))));
    assertEquals(2, getDots(fr(1, 16).add(fr(1, 32)).add(fr(1, 64))));
    assertEquals(2, getDots(fr(1, 32).add(fr(1, 64)).add(fr(1, 128))));
  }
  
  
  @Test public void getBaseDurationTest()
  {
  	//no dots
  	assertEquals(fr(1, 2), getBaseDuration(fr(1, 2)));
  	assertEquals(fr(1, 4), getBaseDuration(fr(1, 4)));
    //one dot
    assertEquals(fr(1, 2), getBaseDuration(fr(3, 4)));
    assertEquals(fr(1, 4), getBaseDuration(fr(3, 8)));
    //two dots
    assertEquals(fr(1, 2), getBaseDuration(fr(7, 8)));
    assertEquals(fr(1, 4), getBaseDuration(fr(7, 16)));
  }
  

}
