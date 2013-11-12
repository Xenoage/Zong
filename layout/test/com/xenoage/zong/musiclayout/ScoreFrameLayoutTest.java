package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StampingMock;
import com.xenoage.zong.musiclayout.stampings.Stamping.Level;


/**
 * Test cases for a ScoreFrameLayout.
 *
 * @author Andreas Wenger
 */
public class ScoreFrameLayoutTest
{
  

  @Test public void getLayoutElementAt()
  {
    
    /* 0    5   10   15    20    | 0
     *                             
     *       ***********           2
     *  +----*   [0]   *     
     *  |    *         *~~~~~~~    4
     *  |[1] ***********      |
     *  +--------+       [2]  |    6
     *     |                  |
     *     ~~~~~~~~~~~~~~~~~~~~    8
     */
  	Stamping[] stampings = new Stamping[3];
    
    StampingMock s1 = new StampingMock(Level.Music, new Rectangle2f(6, 2, 10, 3));
    stampings[0] = s1;
    
    StampingMock s2 = new StampingMock(Level.Staff, new Rectangle2f(1, 3, 9, 3));
    stampings[1] = s2;
    
    StampingMock s3 = new StampingMock(Level.EmptySpace, new Rectangle2f(4, 4, 19, 4));
    stampings[2] = s3;
    
    ScoreFrameLayout layout = new ScoreFrameLayout(null,
    	new PVector<StaffStamping>(), pvec(stampings),
    	new PVector<ContinuedElement>());
    
    //no hit (but empty space)
    assertTrue(isNot(layout.getStampingAt(new Point2f(0, 0)), s1, s2, s3));
    assertTrue(isNot(layout.getStampingAt(new Point2f(3, 7)), s1, s2, s3));
    assertTrue(isNot(layout.getStampingAt(new Point2f(17, 3)), s1, s2, s3));
    
    //single hit
    assertEquals(s1, layout.getStampingAt(new Point2f(10, 2)));
    assertEquals(s2, layout.getStampingAt(new Point2f(3, 5)));
    assertEquals(s3, layout.getStampingAt(new Point2f(22, 8)));
    
    //intersection hit
    assertEquals(s1, layout.getStampingAt(new Point2f(15, 4)));
    assertEquals(s2, layout.getStampingAt(new Point2f(5, 5)));
    assertEquals(s1, layout.getStampingAt(new Point2f(8, 4)));
    
  }
  
  
  private boolean isNot(Object object, Object this1, Object this2, Object this3)
  {
    return (object != this1) && (object != this2) && (object != this3);
  }
  
}
