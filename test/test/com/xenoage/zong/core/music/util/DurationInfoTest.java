package com.xenoage.zong.core.music.util;

import org.junit.Test;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.Duration.*;
import static org.junit.Assert.assertEquals;


/**
 * Test cases for a {@link Duration} class.
 *
 * @author Andreas Wenger
 */
public class DurationInfoTest
{
  
  @Test public void getFlagsCountTest()
  {
    //simple notes
    assertEquals(0, INSTANCE.getFlagsCount(Companion.fr(1, 4)));
    assertEquals(1, INSTANCE.getFlagsCount(Companion.fr(1, 8)));
    assertEquals(2, INSTANCE.getFlagsCount(Companion.fr(1, 16)));
    assertEquals(3, INSTANCE.getFlagsCount(Companion.fr(1, 32)));
    assertEquals(4, INSTANCE.getFlagsCount(Companion.fr(1, 64)));
    assertEquals(5, INSTANCE.getFlagsCount(Companion.fr(1, 128)));
    assertEquals(3, INSTANCE.getFlagsCount(Companion.fr(2, 64)));
    //single dotted quarter note
    assertEquals(0, INSTANCE.getFlagsCount(Companion.fr(3, 8)));
    //single dotted eighth note
    assertEquals(1, INSTANCE.getFlagsCount(Companion.fr(3, 16)));
    //double dotted quarter note
    assertEquals(0, INSTANCE.getFlagsCount(Companion.fr(7, 16)));
    //single dotted 32nd note
    assertEquals(3, INSTANCE.getFlagsCount(Companion.fr(3, 64)));
    //double dotted 32nd note
    assertEquals(3, INSTANCE.getFlagsCount(Companion.fr(7, 128)));
    //whole note
    assertEquals(0, INSTANCE.getFlagsCount(Companion.fr(1, 1)));
  }
  
  
  @Test public void getDurationTest()
  {
  	//no dots
  	assertEquals(Companion.fr(1, 2), INSTANCE.getDuration(Companion.fr(1, 2), 0));
  	assertEquals(Companion.fr(1, 4), INSTANCE.getDuration(Companion.fr(1, 4), 0));
    //one dot
    assertEquals(Companion.fr(3, 4), INSTANCE.getDuration(Companion.fr(1, 2), 1));
    assertEquals(Companion.fr(3, 8), INSTANCE.getDuration(Companion.fr(1, 4), 1));
    //two dots
    assertEquals(Companion.fr(7, 8), INSTANCE.getDuration(Companion.fr(1, 2), 2));
    assertEquals(Companion.fr(7, 16), INSTANCE.getDuration(Companion.fr(1, 4), 2));
  }

  
  @Test public void getDotsTest()
  {
  	//no dots
  	assertEquals(0, INSTANCE.getDots(Companion.fr(1, 1)));
  	assertEquals(0, INSTANCE.getDots(Companion.fr(1, 2)));
  	assertEquals(0, INSTANCE.getDots(Companion.fr(1, 4)));
    assertEquals(0, INSTANCE.getDots(Companion.fr(1, 8)));
    assertEquals(0, INSTANCE.getDots(Companion.fr(1, 16)));
    assertEquals(0, INSTANCE.getDots(Companion.fr(1, 32)));
    //one dot
    assertEquals(1, INSTANCE.getDots(Companion.fr(1, 1).add(Companion.fr(1, 2))));
    assertEquals(1, INSTANCE.getDots(Companion.fr(1, 2).add(Companion.fr(1, 4))));
    assertEquals(1, INSTANCE.getDots(Companion.fr(1, 4).add(Companion.fr(1, 8))));
    assertEquals(1, INSTANCE.getDots(Companion.fr(1, 8).add(Companion.fr(1, 16))));
    assertEquals(1, INSTANCE.getDots(Companion.fr(1, 16).add(Companion.fr(1, 32))));
    assertEquals(1, INSTANCE.getDots(Companion.fr(1, 32).add(Companion.fr(1, 64))));
    //two dots
    assertEquals(2, INSTANCE.getDots(Companion.fr(1, 1).add(Companion.fr(1, 2)).add(Companion.fr(1, 4))));
    assertEquals(2, INSTANCE.getDots(Companion.fr(1, 2).add(Companion.fr(1, 4)).add(Companion.fr(1, 8))));
    assertEquals(2, INSTANCE.getDots(Companion.fr(1, 4).add(Companion.fr(1, 8)).add(Companion.fr(1, 16))));
    assertEquals(2, INSTANCE.getDots(Companion.fr(1, 8).add(Companion.fr(1, 16)).add(Companion.fr(1, 32))));
    assertEquals(2, INSTANCE.getDots(Companion.fr(1, 16).add(Companion.fr(1, 32)).add(Companion.fr(1, 64))));
    assertEquals(2, INSTANCE.getDots(Companion.fr(1, 32).add(Companion.fr(1, 64)).add(Companion.fr(1, 128))));
  }
  
  
  @Test public void getBaseDurationTest()
  {
  	//no dots
  	assertEquals(Companion.fr(1, 2), INSTANCE.getBaseDuration(Companion.fr(1, 2)));
  	assertEquals(Companion.fr(1, 4), INSTANCE.getBaseDuration(Companion.fr(1, 4)));
    //one dot
    assertEquals(Companion.fr(1, 2), INSTANCE.getBaseDuration(Companion.fr(3, 4)));
    assertEquals(Companion.fr(1, 4), INSTANCE.getBaseDuration(Companion.fr(3, 8)));
    //two dots
    assertEquals(Companion.fr(1, 2), INSTANCE.getBaseDuration(Companion.fr(7, 8)));
    assertEquals(Companion.fr(1, 4), INSTANCE.getBaseDuration(Companion.fr(7, 16)));
  }
  

}
