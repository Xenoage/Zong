package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import static com.xenoage.utils.pdlib.IVector.ivec;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.pdlib.IVector;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;


/**
 * Test cases for a {@link FillPageVerticalFrameFillingStrategy}.
 * 
 * @author Andreas Wenger
 */
public class FillPageVerticalFrameFillingStrategyTest
{
	
	/**
	 * Check if the systems were positioned correctly in a frame
	 * with some systems.
	 */
	@Test public void computeFrameArrangementTest()
	{
		//create a simple frame for testing
		float usableHeight = 400;
		int stavesCount = 2;
		float staffHeight = 10;
		float staffDistance = 30;
		float offset1 = 0;
		float offset2 = 100;
		float offset3 = 200;
		SystemArrangement system1 = createSystem(
			stavesCount, staffHeight, staffDistance, offset1);
		SystemArrangement system2 = createSystem(
			stavesCount, staffHeight, staffDistance, offset2);
		SystemArrangement system3 = createSystem(
			stavesCount, staffHeight, staffDistance, offset3);
		
		FrameArrangement frame = new FrameArrangement(
			ivec(system1, system2, system3).close(), new Size2f(10, usableHeight));
		
		//apply strategy
		FillPageVerticalFrameFillingStrategy strategy = FillPageVerticalFrameFillingStrategy.getInstance();
		frame = strategy.computeFrameArrangement(frame, null);
		
		//compare values
		//remaining space is usable height - offset3 - (height of last system)
		float remainingSpace = usableHeight - offset3 - system3.getHeight();
		//the last two systems are moved down, each remainingSpace/2
		float additionalSpace = remainingSpace / 2;
		//compare new offsets
		assertEquals(offset2 + 1 * additionalSpace, frame.getSystems().get(1).getOffsetY(), Delta.DELTA_FLOAT);
		assertEquals(offset3 + 2 * additionalSpace, frame.getSystems().get(2).getOffsetY(), Delta.DELTA_FLOAT);
		
	}
	
	
	/**
	 * Creates and returns a simple {@link SystemArrangement} using the
	 * given values.
	 */
	public static SystemArrangement createSystem(int stavesCount,
		float staffHeight, float staffDistance,	float offsetY)
	{
		IVector<Float> staffHeights = ivec(stavesCount);
		for (int i = 0; i < stavesCount; i++)
			staffHeights.add(staffHeight);
		IVector<Float> staffDistances = ivec(stavesCount - 1);
		for (int i = 0; i < stavesCount - 1; i++)
			staffDistances.add(staffDistance);
		return new SystemArrangement(-1, -1,
			new IVector<ColumnSpacing>().close(), 0, 0, 0, staffHeights, staffDistances, offsetY);
	}

}
