package com.xenoage.zong.musiclayout.spacer.frame.fill;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.spacer.frame.fill.StretchSystems.stretchSystems;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.StavesSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Tests for {@link StretchSystems}.
 * 
 * @author Andreas Wenger
 */
public class StretchSystemsTest {
	
	private StretchSystems testee = stretchSystems;

	
	@Test public void computeTest() {
		//create a simple frame for testing
		float usableHeight = 400;
		int stavesCount = 2;
		float staffHeight = 10;
		float staffDistance = 30;
		float offset1 = 0;
		float offset2 = 100;
		float offset3 = 200;
		SystemSpacing system1 = createSystem(stavesCount, staffHeight, staffDistance, offset1);
		SystemSpacing system2 = createSystem(stavesCount, staffHeight, staffDistance, offset2);
		SystemSpacing system3 = createSystem(stavesCount, staffHeight, staffDistance, offset3);

		FrameSpacing frame = new FrameSpacing(ilist(system1, system2, system3),
			new Size2f(10, usableHeight));

		//apply strategy
		testee.compute(frame, null);

		//compare values
		//remaining space is usable height - offset3 - (height of last system)
		float remainingSpace = usableHeight - offset3 - system3.getHeightMm();
		//the last two systems are moved down, each remainingSpace/2
		float additionalSpace = remainingSpace / 2;
		//compare new offsets
		assertEquals(offset2 + 1 * additionalSpace, frame.getSystems().get(1).offsetYMm, df);
		assertEquals(offset3 + 2 * additionalSpace, frame.getSystems().get(2).offsetYMm, df);
	}

	/**
	 * Creates and returns a simple {@link SystemSpacing} using the
	 * given values.
	 */
	public static SystemSpacing createSystem(int stavesCount, float staffHeight,
		float staffDistance, float offsetY) {
		float is = staffHeight / 5;
		List<Staff> staves = alist(stavesCount);
		for (int i = 0; i < stavesCount; i++)
			staves.add(new Staff(alist(), 5, is));
		float[] staffDistances = new float[stavesCount - 1];
		for (int i : range(stavesCount - 1))
			staffDistances[i] = staffDistance;
		return new SystemSpacing(CollectionUtils.<ColumnSpacing>alist(), 0, 0, 0,
			new StavesSpacing(staves, staffDistances, is), offsetY);
	}

}
