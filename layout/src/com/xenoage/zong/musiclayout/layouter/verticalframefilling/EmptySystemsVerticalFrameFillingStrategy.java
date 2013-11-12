package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.pdlib.IVector.ivec;
import static com.xenoage.zong.core.position.MP.atStaff;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.pdlib.IVector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.io.score.ScoreController;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;


/**
 * This vertical frame filling strategy
 * adds empty systems to the current frame
 * until the page is filled and returns
 * the result.
 * 
 * @author Andreas Wenger
 */
public class EmptySystemsVerticalFrameFillingStrategy
	implements VerticalFrameFillingStrategy
{

	private static EmptySystemsVerticalFrameFillingStrategy instance = null;


	public static EmptySystemsVerticalFrameFillingStrategy getInstance()
	{
		if (instance == null)
			instance = new EmptySystemsVerticalFrameFillingStrategy();
		return instance;
	}


	private EmptySystemsVerticalFrameFillingStrategy()
	{
	}


	/**
	 * Fill frame with empty systems.
	 */
	@Override public FrameArrangement computeFrameArrangement(FrameArrangement frameArr, Score score)
	{
		Size2f usableSize = frameArr.getUsableSize();
		FrameArrangement ret = frameArr;

		//compute remaining space
		float remainingSpace = usableSize.height;
		float offsetY = 0;
		if (frameArr.getSystems().size() > 0) {
			SystemArrangement lastSystem = frameArr.getSystems().getLast();
			offsetY = lastSystem.getOffsetY() + lastSystem.getHeight();
			remainingSpace -= offsetY;
		}

		//compute height of an additional system
		SystemLayout defaultSystemLayout = score.format.systemLayout;
		float defaultSystemDistance = defaultSystemLayout.distance;
		float defaultMargin = defaultSystemLayout.marginLeft + defaultSystemLayout.marginRight;
		SystemArrangement newSystem = createEmptySystem(score, usableSize.width, 0);
		float newSystemHeight = defaultSystemDistance + newSystem.getHeight();

		//add as many additional empty staves as possible
		int newSystemsCount = (int) (remainingSpace / newSystemHeight);
		if (newSystemsCount > 0) {
			//otherwise add the empty systems
			IVector<SystemArrangement> newSystems = ivec();
			newSystems.addAll(frameArr.getSystems());
			for (int i = frameArr.getSystems().size() - 1; i < newSystemsCount; i++) {
				newSystems.add(createEmptySystem(score, usableSize.width - defaultMargin, offsetY +
					defaultSystemDistance));
				offsetY += newSystemHeight;
			}
			ret = new FrameArrangement(newSystems.close(), frameArr.getUsableSize());
		}

		return ret;
	}


	/**
	 * Creates an additional system for the given {@link Score} with
	 * the given width and y-offset in mm and returns it.
	 */
	private SystemArrangement createEmptySystem(Score score, float width, float offsetY)
	{
		IVector<Float> staffHeights = ivec(score.getStavesCount());
		IVector<Float> staffDistances = ivec(score.getStavesCount() - 1);
		//compute staff heights
		for (int iStaff : range(score.getStavesCount())) {
			Staff staff = score.getStaff(iStaff);
			staffHeights.add((staff.getLinesCount() - 1) *
				ScoreController.getInterlineSpace(score, atStaff(iStaff)));
		}
		//compute staff distances 
		for (int iStaff : range(1, score.stavesList.getStavesCount() - 1)) {
			staffDistances.add(score.format.getStaffLayoutNotNull(iStaff).distance);
		}
		//create and returns system
		SystemLayout defaultSystemLayout = score.format.systemLayout;
		return new SystemArrangement(-1, -1, new IVector<ColumnSpacing>().close(), defaultSystemLayout.marginLeft,
			defaultSystemLayout.marginRight, width, staffHeights, staffDistances, offsetY);
	}


}
