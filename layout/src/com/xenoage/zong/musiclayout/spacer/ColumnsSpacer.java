package com.xenoage.zong.musiclayout.spacer;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.spacer.measure.ColumnSpacer.columnSpacer;

import java.util.List;

import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;

/**
 * Computes the optimal {@link ColumnSpacing}s for a score,
 * unbounded by frames or systems constraints, and without
 * {@link LeadingSpacing}s.
 * 
 * @author Andreas Wenger
 */
public class ColumnsSpacer {
	
	public static final ColumnsSpacer columnsSpacer = new ColumnsSpacer();
	
	private static final boolean noLeading = false;
	
	
	public List<ColumnSpacing> compute(Notations notations, Context context) {
		context.saveMp();
		List<ColumnSpacing> ret = alist();
		for (int iMeasure : range(context.score.getMeasuresCount())) {
			context.mp = MP.atMeasure(iMeasure);
			ret.add(columnSpacer.compute(context, noLeading, notations));
		}
		context.restoreMp();
		return ret;
	}

}
