package com.xenoage.zong.layout;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.layout.frames.TestFrame;


/**
 * Test cases for the {@link Layout} class.
 * 
 * @author Andreas Wenger
 */
public class LayoutTest
{
	
	TestFrame frame_1_1, frame_2_1, frame_2_2_1_1, frame_2_2_1_2, frame_2_2_2, frame_3_1;
	GroupFrame group_2_2_1, group_2_2;
	Page page_1, page_2, page_3;
	Layout layout;
	
	
	/**
	 * Creates a simple nested layout and checks the mappings.
	 */
	@Test public void testPlus()
	{
		createTestLayout1();
		HashMap<Frame, Page> pP = layout.parentPages;
		HashMap<Frame, GroupFrame> pF = layout.parentFrames;
		assertEquals(4, pP.size());
		assertEquals(4, pF.size());
		assertEquals(page_1, pP.get(frame_1_1));
		assertEquals(page_2, pP.get(frame_2_1));
		assertEquals(page_2, pP.get(group_2_2));
		assertEquals(page_3, pP.get(frame_3_1));
		assertEquals(group_2_2, pF.get(group_2_2_1));
		assertEquals(group_2_2_1, pF.get(frame_2_2_1_1));
		assertEquals(group_2_2_1, pF.get(frame_2_2_1_2));
		assertEquals(group_2_2, pF.get(frame_2_2_2));
	}
	
	
	@Test public void removeFrameTest1()
	{
		createTestLayout1();
		//remove group_2_2
		layout = layout.removeFrame(group_2_2, false);
		layout.updateCache();
		HashMap<Frame, Page> pP = layout.parentPages;
		HashMap<Frame, GroupFrame> pF = layout.parentFrames;
		assertEquals(3, pP.size());
		assertEquals(0, pF.size());
		assertEquals(page_1, pP.get(frame_1_1));
		assertEquals(layout.pages.get(1), pP.get(frame_2_1));
		assertEquals(page_3, pP.get(frame_3_1));
	}
	
	
	@Test public void removeFrameTest2()
	{
		createTestLayout1();
		//remove frame_2_2_1_1
		layout = layout.removeFrame(frame_2_2_1_1, false);
		layout.updateCache();
		HashMap<Frame, Page> pP = layout.parentPages;
		HashMap<Frame, GroupFrame> pF = layout.parentFrames;
		assertEquals(4, pP.size());
		assertEquals(3, pF.size());
		//parent frames over 2_2_2_1 have been changed
		page_2 = layout.pages.get(1);
		frame_2_1 = (TestFrame) page_2.frames.get(0);
		group_2_2 = (GroupFrame) page_2.frames.get(1);
		group_2_2_1 = (GroupFrame) group_2_2.children.get(0);
		frame_2_2_1_2 = (TestFrame) group_2_2_1.children.get(0);
		frame_2_2_2 = (TestFrame) group_2_2.children.get(1);
		//check mappings
		assertEquals(page_1, pP.get(frame_1_1));
		assertEquals(page_2, pP.get(frame_2_1));
		assertEquals(page_2, pP.get(group_2_2));
		assertEquals(page_3, pP.get(frame_3_1));
		assertEquals(group_2_2, pF.get(group_2_2_1));
		assertEquals(group_2_2_1, pF.get(frame_2_2_1_2));
		assertEquals(group_2_2, pF.get(frame_2_2_2));
	}
	
	
	private void createTestLayout1()
	{
		//layout:
		//[page 1 [frame 1_1]]
		//[page 2 [frame 2_1] [group 2_2 [group 2_2_1 [frame 2_2_1_1] [frame 2_2_1_2]] [frame 2_2_2]]]
		//[page 3 [frame 3_1]]
		frame_1_1 = new TestFrame();
		page_1 = new Page(PageFormat.defaultValue).plusFrame(frame_1_1);
		frame_2_1 = new TestFrame();
		frame_2_2_1_1 = new TestFrame();
		frame_2_2_1_2 = new TestFrame();
		group_2_2_1 = new GroupFrame(null).plusChildFrame(frame_2_2_1_1).plusChildFrame(frame_2_2_1_2);
		frame_2_2_2 = new TestFrame();
		group_2_2 = new GroupFrame(null).plusChildFrame(group_2_2_1).plusChildFrame(frame_2_2_2);
		page_2 = new Page(PageFormat.defaultValue).plusFrame(frame_2_1).plusFrame(group_2_2);
		frame_3_1 = new TestFrame();
		page_3 = new Page(PageFormat.defaultValue).plusFrame(frame_3_1);
		layout = new Layout(null).plusPage(page_1).plusPage(page_2).plusPage(page_3);
		layout.updateCache();
	}
	

}
