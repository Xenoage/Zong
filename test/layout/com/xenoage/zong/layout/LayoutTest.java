package com.xenoage.zong.layout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.layout.frames.TestFrame;

/**
 * Tests for {@link Layout}.
 * 
 * @author Andreas Wenger
 */
public class LayoutTest {

	TestFrame frame_1_1, frame_2_1, frame_2_2_1_1, frame_2_2_1_2, frame_2_2_2, frame_3_1;
	GroupFrame group_2_2_1, group_2_2;
	Page page_1, page_2, page_3;
	Layout layout;


	/**
	 * Creates a simple nested layout and checks the parent relationships.
	 */
	@Test public void testParents() {
		createTestLayout();
		assertEquals(3, layout.getPages().size());
		//direct parents
		assertEquals(page_1, frame_1_1.getParent());
		assertEquals(page_2, frame_2_1.getParent());
		assertEquals(page_2, group_2_2.getParent());
		assertEquals(page_3, frame_3_1.getParent());
		assertEquals(group_2_2, group_2_2_1.getParent());
		assertEquals(group_2_2_1, frame_2_2_1_1.getParent());
		assertEquals(group_2_2_1, frame_2_2_1_2.getParent());
		assertEquals(group_2_2, frame_2_2_2.getParent());
		//parent pages
		assertEquals(page_1, frame_1_1.getParentPage());
		assertEquals(page_2, frame_2_1.getParentPage());
		assertEquals(page_2, group_2_2.getParentPage());
		assertEquals(page_3, frame_3_1.getParentPage());
		assertEquals(page_2, group_2_2_1.getParentPage());
		assertEquals(page_2, frame_2_2_1_1.getParentPage());
		assertEquals(page_2, frame_2_2_1_2.getParentPage());
		assertEquals(page_2, frame_2_2_2.getParentPage());
	}

	@Test public void removeFrameTest1() {
		createTestLayout();
		//remove group_2_2
		page_2.removeFrame(group_2_2);
		assertFalse(page_2.getFrames().contains(group_2_2));
		assertNull(group_2_2.getParent());
		assertNull(group_2_2.getParentPage());
	}

	@Test public void removeFrameTest2() {
		createTestLayout();
		//remove frame_2_2_1_1
		group_2_2_1.removeChildFrame(frame_2_2_1_1);
		assertFalse(group_2_2_1.getChildren().contains(frame_2_2_1_1));
		assertNull(frame_2_2_1_1.getParent());
		assertNull(frame_2_2_1_1.getParentPage());
	}

	private void createTestLayout() {
		//layout:
		//[page 1 [frame 1_1]]
		//[page 2 [frame 2_1] [group 2_2 [group 2_2_1 [frame 2_2_1_1] [frame 2_2_1_2]] [frame 2_2_2]]]
		//[page 3 [frame 3_1]]
		layout = new Layout(null);
		//page 1
		page_1 = new Page(PageFormat.defaultValue);
		layout.addPage(page_1);
		frame_1_1 = new TestFrame();
		page_1.addFrame(frame_1_1);
		//page 2
		page_2 = new Page(PageFormat.defaultValue);
		layout.addPage(page_2);
		frame_2_1 = new TestFrame();
		page_2.addFrame(frame_2_1);
		group_2_2 = new GroupFrame();
		page_2.addFrame(group_2_2);
		group_2_2_1 = new GroupFrame();
		group_2_2.addChildFrame(group_2_2_1);
		frame_2_2_1_1 = new TestFrame();
		group_2_2_1.addChildFrame(frame_2_2_1_1);
		frame_2_2_1_2 = new TestFrame();
		group_2_2_1.addChildFrame(frame_2_2_1_2);
		frame_2_2_2 = new TestFrame();
		group_2_2.addChildFrame(frame_2_2_2);
		//page 3
		page_3 = new Page(PageFormat.defaultValue);
		layout.addPage(page_3);
		frame_3_1 = new TestFrame();
		page_3.addFrame(frame_3_1);
	}

}
