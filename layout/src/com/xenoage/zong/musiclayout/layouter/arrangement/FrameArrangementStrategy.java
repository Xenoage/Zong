package com.xenoage.zong.musiclayout.layouter.arrangement;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.musiclayout.spacer.system.SystemSpacer.systemSpacer;

import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterContext;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.spacing.measure.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.system.SystemSpacing;

/**
 * A {@link FrameArrangementStrategy} arranges a list of
 * measure columns on a frame by filling it with systems.
 * 
 * The systems are not stretched or moved
 * horizontally or vertically. This can be
 * done by other strategies.
 * 
 * This strategy also regards custom system distances.
 * 
 * @author Andreas Wenger
 */
public class FrameArrangementStrategy
	implements ScoreLayouterStrategy {


	/**
	 * Arranges the given measure column spacings beginning at the
	 * given index to a {@link FrameArrangement}. Returns the frame arrangements
	 * and a list of created notations for the leading notations.
	 */
	public Tuple2<FrameArrangement, Notations> computeFrameArrangement(int startMeasure,
		int startSystem, Size2f usableSize, List<ColumnSpacing> columnSpacings,
		Notations notations, ScoreLayouterContext lc, Context context) {
		context.saveMp();
		
		Score score = lc.getScore();
		ScoreFormat scoreFormat = score.getFormat();
		ScoreHeader scoreHeader = score.getHeader();

		int measuresCount = score.getMeasuresCount();
		int measureIndex = startMeasure;
		int systemIndex = startSystem;

		//top margin
		float offsetY = getTopSystemDistance(systemIndex, scoreFormat, scoreHeader);

		//append systems to the frame
		CList<SystemSpacing> systems = clist();
		Notations retLeadingNotations = new Notations();
		while (measureIndex < measuresCount) {
			//try to create system on this frame
			context.mp = atMeasure(measureIndex);
			SystemSpacing system = systemSpacer.compute(context, usableSize, offsetY, systemIndex,
				columnSpacings, notations).orNull();

			//was there enough place for this system?
			if (system != null) {
				//yes, there is enough place. add system
				systems.add(system);
				//update offset and start measure index for next system
				//add height of this system
				offsetY += system.getHeight();
				//add system distance of the following system
				offsetY += getSystemDistance(systemIndex + 1, scoreFormat, scoreHeader);
				//increase indexes
				systemIndex++;
				measureIndex = system.endMeasureIndex + 1;
			}
			else {
				break;
			}
		}
		systems.close();

		context.restoreMp();
		return t(new FrameArrangement(systems, usableSize), retLeadingNotations);
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
