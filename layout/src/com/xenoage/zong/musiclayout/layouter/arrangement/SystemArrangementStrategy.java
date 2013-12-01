package com.xenoage.zong.musiclayout.layouter.arrangement;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.zong.core.position.MP.atStaff;

import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.layout.SystemBreak;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterContext;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.columnspacing.ColumnSpacingStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.VoiceSpacingsByStaff;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * The {@link SystemArrangementStrategy} arranges
 * a list of measure columns into a {@link SystemArrangement}.
 * 
 * The systems will not be stretched to their full possible width.
 * This can be done by another strategy.
 * 
 * This strategy also regards forced and prohibited system breaks
 * and custom system margins (left, right) and staff margins.
 * 
 * @author Andreas Wenger
 */
public class SystemArrangementStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	private final ColumnSpacingStrategy measureColumnSpacingStrategy;


	/**
	 * Creates a new {@link SystemArrangementStrategy}.
	 */
	public SystemArrangementStrategy(ColumnSpacingStrategy measureColumnSpacingStrategy) {
		this.measureColumnSpacingStrategy = measureColumnSpacingStrategy;
	}

	/**
	 * Arranges an optimum number of measures in a system, beginning at the given measure,
	 * and returns this system together with a list of created notations for leading spacings.
	 * If there is not enough space left on the current frame to create a system, null is returned.
	 * @param startMeasure   the index of the measure to start with
	 * @param usableSize     the usable size within the score frame
	 * @param offsetY        the vertical offset of the system in mm
	 * @param systemIndex    the global system index (over all frames!)
	 * @param measureColumnSpacings  a list of all measure column spacings without
	 *                       leading spacings
	 * @param notations      the already computed notations. this cache is modified when
	 *                       a leading spacing is created, because the notations needed
	 *                       for the leading spacing are added.
	 * @param lc             the context of the layouter
	 */
	public Tuple2<SystemArrangement, NotationsCache> computeSystemArrangement(int startMeasure,
		Size2f usableSize, float offsetY, int systemIndex, List<ColumnSpacing> measureColumnSpacings,
		NotationsCache notations, ScoreLayouterContext lc) {

		//test if there is enough height for the system
		Score score = lc.getScore();
		ScoreFormat scoreFormat = score.getFormat();
		ScoreHeader scoreHeader = score.getHeader();

		//compute staff heights
		float[] staffHeights = new float[score.getStavesCount()];
		float totalStavesHeights = 0;
		for (int iStaff : range(score.getStavesCount())) {
			Staff staff = score.getStaff(iStaff);
			float staffHeight = (staff.getLinesCount() - 1) * score.getInterlineSpace(atStaff(iStaff));
			staffHeights[iStaff] = staffHeight;
			totalStavesHeights += staffHeight;
		}

		//compute staff distances
		float[] staffDistances = new float[score.getStavesCount() - 1];
		float totalStavesDistances = 0;
		for (int iStaff : range(1, staffHeights.length - 1)) {
			StaffLayout staffLayout = scoreHeader.getStaffLayout(systemIndex, iStaff);
			float staffDistance = 0;
			if (staffLayout != null) {
				//use custom staff distance
				staffDistance = staffLayout.getDistance();
			}
			else {
				//use default staff distance
				staffDistance = scoreFormat.getStaffLayoutNotNull(iStaff).getDistance();
			}
			staffDistances[iStaff - 1] = staffDistance;
			totalStavesDistances += staffDistance;
		}

		//enough space?
		if (offsetY + totalStavesHeights + totalStavesDistances > usableSize.height) {
			//not enough space
			return null;
		}

		//compute the usable width for the system
		float systemLeftMargin = getLeftMargin(systemIndex, scoreFormat, scoreHeader);
		float systemRightMargin = getRightMargin(systemIndex, scoreFormat, scoreHeader);
		float usableWidth = usableSize.width - systemLeftMargin - systemRightMargin;

		//append system measure-by-measure, until there is no space any more
		//or until there are no measures left
		int measuresCount = score.getMeasuresCount();
		CList<ColumnSpacing> systemMCSs = clist();
		float usedWidth = 0;
		int currentMeasure;
		NotationsCache retLeadingNotations = new NotationsCache();
		while (startMeasure + systemMCSs.size() < measuresCount) {
			currentMeasure = startMeasure + systemMCSs.size();

			//decide if to add a leading spacing to the current measure or not
			ColumnSpacing currentMCS;
			NotationsCache leadingNotations = null; //add leading notations to given cache only if
																							//measure column with leading spacing is really used
			if (systemMCSs.size() == 0) {
				//first measure within this system: add leading elements (clef, time sig.)
				Tuple3<ColumnSpacing, VoiceSpacingsByStaff, NotationsCache> mcsData = measureColumnSpacingStrategy
					.computeColumnSpacing(currentMeasure, true /* leading! */, notations, lc);
				currentMCS = mcsData.get1();
				leadingNotations = mcsData.get3();
			}
			else {
				//otherwise: use the optimal spacing
				currentMCS = measureColumnSpacings.get(currentMeasure);
			}

			//try to add this measure to the current system. if there is no space left for
			//it, don't use it, and we are finished.
			if (!canAppend(currentMCS, currentMeasure, usableWidth, usedWidth, scoreHeader,
				systemMCSs.size() == 0)) {
				break;
			}
			else {
				usedWidth += currentMCS.getWidth();
				systemMCSs.add(currentMCS);
				if (leadingNotations != null)
					retLeadingNotations.merge(leadingNotations);
			}
		}
		systemMCSs.close();

		//we are finished
		if (systemMCSs.size() == 0) {
			return null; //not enough space for the system on this area
		}
		else {
			SystemArrangement ret = new SystemArrangement(startMeasure, startMeasure + systemMCSs.size() -
				1, systemMCSs, systemLeftMargin, systemRightMargin, usedWidth, staffHeights,
				staffDistances, offsetY);
			return t(ret, retLeadingNotations);
		}

	}

	/**
	 * Returns true, if the given measure can be appended to the currently computed system,
	 * otherwise false.
	 * It is not tested if there is enough vertical space, this must be done before.
	 */
	public boolean canAppend(ColumnSpacing measure, int measureIndex, float usableWidth,
		float usedWidth, ScoreHeader scoreHeader, boolean firstMeasureInSystem) {

		//if a line break is forced, do it (but one measure is always allowed)
		Break br = scoreHeader.getColumnHeader(measureIndex).getMeasureBreak();
		if (br != null && br.getSystemBreak() == SystemBreak.NewSystem && !firstMeasureInSystem) {
			return false;
		}

		//if a line break is prohibited, force the measure to be within this system
		boolean force = (br != null && br.getSystemBreak() == SystemBreak.NoNewSystem);

		//enough horizontal space?
		float remainingWidth = usableWidth - usedWidth;
		if (remainingWidth < measure.getWidth() && !force)
			return false;

		//ok, append the measure
		return true;
	}

	/**
	 * Returns the left margin of the given system (global index) in mm.
	 */
	private float getLeftMargin(int systemIndex, ScoreFormat scoreFormat, ScoreHeader scoreHeader) {
		SystemLayout systemLayout = scoreHeader.getSystemLayout(systemIndex);
		if (systemLayout != null) {
			//use custom system margin
			return systemLayout.getMarginLeft();
		}
		else {
			//use default system margin
			return scoreFormat.getSystemLayout().getMarginLeft();
		}
	}

	/**
	 * Returns the right margin of the given system (global index) in mm.
	 */
	private float getRightMargin(int systemIndex, ScoreFormat scoreFormat, ScoreHeader scoreHeader) {
		SystemLayout systemLayout = scoreHeader.getSystemLayout(systemIndex);
		if (systemLayout != null) {
			//use custom system margin
			return systemLayout.getMarginRight();
		}
		else {
			//use default system margin
			return scoreFormat.getSystemLayout().getMarginRight();
		}
	}

}
