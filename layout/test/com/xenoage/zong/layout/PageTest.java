package com.xenoage.zong.layout;

import static com.xenoage.utils.math.geom.Point2f.p;
import static com.xenoage.utils.math.geom.Size2f.s;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.xenoage.utils.graphics.color.ColorInfo;
import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.PageMargins;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.FrameData;
import com.xenoage.zong.layout.frames.FP;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.background.ColorBackground;


/**
 * Test cases for the Page class.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class PageTest
{
  
  private Frame frm1, frm2a, frm2b, frm3;
  private GroupFrame frm2;
  
  
  
  /**
   * Tests the computeFramePosition-method
   * without rotation.
   */
	@Test public void computeFramePosition1()
  {
    Layout layout = createPageWithUnrotatedFrames();
    Page page = layout.pages.get(0);
    
    //Test 1
		Point2f p = new Point2f(113,122);
		FP fp = page.computeFramePosition(p, layout);
    assertNotNull(fp);
    assertEquals(frm2, fp.getFrame());
		assertEquals(23, fp.getPosition().x, Delta.DELTA_FLOAT);
		assertEquals(12, fp.getPosition().y, Delta.DELTA_FLOAT);
    
    //Test 2
    p = new Point2f(122,108);
    fp = page.computeFramePosition(p, layout);
    assertNotNull(fp);
    assertEquals(frm2a, fp.getFrame());
    assertEquals(2, fp.getPosition().x, Delta.DELTA_FLOAT);
    assertEquals(-2, fp.getPosition().y, Delta.DELTA_FLOAT);
    
    //Test 3
    p = new Point2f(86,88);
    fp = page.computeFramePosition(p, layout);
    assertNull(fp);
	}
  
  
  /**
   * Tests the computeFramePosition-method with rotation.
   * See PageTest.odg in this folder.
   */
  @Test public void computeFramePosition2()
  {
  	Layout layout = createPageWithRotatedFrames();
    Page page = layout.pages.get(0);
    FP fp;
    float delta = 1;
    
    //Test 1 (yellow)
    fp = page.computeFramePosition(p(162.1f,96.1f), layout);
    assertNull(fp);
    fp = page.computeFramePosition(p(163f,107.3f), layout);
    assertNull(fp);
    fp = page.computeFramePosition(p(163f,100.4f), layout);
    assertNotNull(fp);
    assertEquals(frm1, fp.getFrame());
    Point2f pExpected = p(27, -38);
    assertEquals(pExpected.x, fp.getPosition().x, delta);
    assertEquals(pExpected.y, fp.getPosition().y, delta);
    
    //Test 2 (green)
    fp = page.computeFramePosition(p(137,145), layout);
    assertEquals(frm1, fp.getFrame());
    fp = page.computeFramePosition(p(130,160), layout);
    assertEquals(frm1, fp.getFrame());
    fp = page.computeFramePosition(p(132,152.1f), layout);
    assertEquals(frm3, fp.getFrame());
    fp = page.computeFramePosition(p(137,152.1f), layout);
    assertEquals(frm3, fp.getFrame());
    
    //Test 3 (orange)
    fp = page.computeFramePosition(p(100,85), layout);
    assertEquals(frm2, fp.getFrame());
    fp = page.computeFramePosition(p(102,90), layout);
    assertEquals(frm2, fp.getFrame());
    fp = page.computeFramePosition(p(102,95), layout);
    assertEquals(frm2a, fp.getFrame());
    pExpected = p(-2, -4);
    assertEquals(pExpected.x, fp.getPosition().x, delta);
    assertEquals(pExpected.y, fp.getPosition().y, delta);
    fp = page.computeFramePosition(p(109,84), layout);
    assertEquals(frm1, fp.getFrame());
    fp = page.computeFramePosition(p(105,82.1f), layout);
    assertNull(fp);
    
    //Test 4 (red)
    fp = page.computeFramePosition(p(82,130), layout);
    assertEquals(frm2b, fp.getFrame());
    pExpected = p(-8, -9);
    assertEquals(pExpected.x, fp.getPosition().x, delta);
    assertEquals(pExpected.y, fp.getPosition().y, delta);
    fp = page.computeFramePosition(p(76,127.1f), layout);
    assertEquals(frm2, fp.getFrame());
    fp = page.computeFramePosition(p(75,135), layout);
    assertEquals(frm2, fp.getFrame());
    fp = page.computeFramePosition(p(75,139), layout);
    assertEquals(frm1, fp.getFrame());
    fp = page.computeFramePosition(p(75,144.1f), layout);
    assertEquals(frm3, fp.getFrame());
    fp = page.computeFramePosition(p(70,137.1f), layout);
    assertNull(fp);
  }
  
  
  /**
   * Creates a layout with a page with some unrotated frames for testing.
   */
  private Layout createPageWithUnrotatedFrames()
  {
  	Layout ret = new Layout(null);
  	
    PageFormat pf = new PageFormat(new Size2f(200, 200), new PageMargins(10,10,10,10));
    Page page = new Page(pf);
    
    //Frame 1
    frm1 = new ScoreFrame(new FrameData(p(120,120), s(60,80), 0, new ColorBackground(Color.blue)));
    page = page.plusFrame(frm1);

    //Frame 2
    frm2 = new GroupFrame(new FrameData(p(90,110), s(60,40), 0, new ColorBackground(Color.green)));
    
    //Childframe 2.a
    frm2a = new ScoreFrame(new FrameData(p(30,0), s(10,10), 0, new ColorBackground(Color.yellow)));
    frm2 = frm2.plusChildFrame(frm2a);
    
    //Childframe 2.b
    frm2b = new ScoreFrame(new FrameData(p(-10,-20), s(10,10), 0, new ColorBackground(Color.lightGray)));
    frm2 = frm2.plusChildFrame(frm2b);
    
    page = page.plusFrame(frm2);
    
    //Frame 3
    frm3 = new ScoreFrame(new FrameData(p(30,80), s(30,60), 0, new ColorBackground(Color.red)));
    page = page.plusFrame(frm3);
    
    ret = ret.plusPage(page);
    
    return ret;
  }
  
  
  /**
   * Creates a layout with a page with some rotated frames for testing.
   */
  private Layout createPageWithRotatedFrames()
  {
  	Layout ret = new Layout(null);
  	
    PageFormat pf = new PageFormat(new Size2f(200,200), new PageMargins(10,10,10,10));
    Page page = new Page(pf);
    
    //Frame 1
    frm1 = new ScoreFrame(new FrameData(p(120,120), s(60,80), -30, new ColorBackground(Color.blue)));
    page = page.plusFrame(frm1);

    //Frame 2
    frm2 = new GroupFrame(new FrameData(p(90,110), s(60,40), 70, new ColorBackground(Color.green)));
    
    //Childframe 2.a
    frm2a = new ScoreFrame(new FrameData(p(20,10), s(10,10), 0, new ColorBackground(Color.yellow)));
    frm2 = frm2.plusChildFrame(frm2a);
    
    //Childframe 2.b
    frm2b = new ScoreFrame(new FrameData(p(-10,-5), s(20,20), 70, new ColorBackground(Color.lightGray)));
    frm2 = frm2.plusChildFrame(frm2b);
    
    page = page.plusFrame(frm2);
    
    //Frame 3
    frm3 = new ScoreFrame(new FrameData(p(95,155), s(80,40), 30, new ColorBackground(Color.red)));
    page = page.plusFrame(frm3);
    
    ret = ret.plusPage(page);
    
    return ret;
  }
  
  
}
