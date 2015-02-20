package com.xenoage.zong.musiclayout.spacer.frame;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.musiclayout.spacer.system.SystemSpacer.systemSpacer;

import java.util.List;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Arranges a list of measure columns on a frame by filling it with systems.
 * 
 * The systems are not stretched or moved horizontally or vertically.
 * This can be done by a later layouting step.
 * 
 * This strategy also regards custom system distances.
 * 
 * @author Andreas Wenger
 */
public class FrameSpacer {
	
	public static final FrameSpacer frameSpacer = new FrameSpacer();


	/**
	 * Arranges an optimum number of systems in a frame, beginning at the given measure,
	 * if possible.
	 * @param context        the context of the layouter, with the {@link MP} set to the start measure
	 * @param startSystem    the index of the system where to start
	 * @param usableSizeMm   the usable size within the score frame in mm
	 * @param systemIndex    the global system index (over all frames)
	 * @param measureColumnSpacings  a list of all measure column spacings without leading spacings
	 * @param notations      the notations of the elements, needed when a column has to be recomputed
	 *                       because of a leading spacing
	 */
	public FrameSpacing compute(Context context, int startSystem,
		Size2f usableSizeMm, List<ColumnSpacing> columnSpacings, Notations notations) {
		
		context.saveMp();
		int startMeasure = context.mp.measure;
		
		Score score = context.score;
		ScoreFormat scoreFormat = score.getFormat();
		ScoreHeader scoreHeader = score.getHeader();

		int measuresCount = score.getMeasuresCount();
		int measureIndex = startMeasure;
		int systemIndex = startSystem;

		//top margin
		float offsetY = getTopSystemDistance(systemIndex, scoreFormat, scoreHeader);

		//append systems to the frame
		List<SystemSpacing> systems = alist();
		while (measureIndex < measuresCount) {
			//try to create system on this frame
			context.mp = atMeasure(measureIndex);
			SystemSpacing system = systemSpacer.compute(context, usableSizeMm, offsetY, systemIndex,
				columnSpacings, notations).orNull();

			//was there enough place for this system?
			if (system != null) {
				//yes, there is enough place. add system
				systems.add(system);
				//update offset and start measure index for next system
				//add height of this system
				offsetY += system.getHeightMm();
				//add system distance of the following system
				offsetY += getSystemDistance(systemIndex + 1, scoreFormat, scoreHeader);
				//increase indexes
				systemIndex++;
				measureIndex = system.getEndMeasureIndex() + 1;
			}
			else {
				break;
			}
		}

		context.restoreMp();
		return new FrameSpacing(systems, usableSizeMm);
	}

	/**
	 * Returns the top system distance (distance between top margin of the frame and
	 * first staff line of the first system within the frame) for the given
	 * system (global index) in mm.
	 */
	private float getTopSystemDistance(int systemIndex, ScoreFormat scoreFormat,
		ScoreHeader scoreHeader) {
		SystemLayout systemLayout = scoreHeader.getSystemLayout(systemIndex);
		if (systemLayout != null) {
			//use custom top system distance
			return systemLayout.getDistance();
		}
		else {
			//use default distance
			return scoreFormat.getTopSystemDistance();
		}
	}

	/**
	 * Returns the system distance (distance between last staff line of the previous
	 * system and first staff line of the following given system (global index) in mm.
	 */
	private float getSystemDistance(int systemIndex, ScoreFormat scoreFormat, ScoreHeader scoreHeader) {
		SystemLayout systemLayout = scoreHeader.getSystemLayout(systemIndex);
		if (systemLayout != null) {
			//use custom system distance
			return systemLayout.getDistance();
		}
		else {
			//use default system distance
			return scoreFormat.getSystemLayout().getDistance();
		}
	}

}
