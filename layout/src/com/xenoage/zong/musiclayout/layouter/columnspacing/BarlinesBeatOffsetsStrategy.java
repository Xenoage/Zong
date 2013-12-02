package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Tuple2.t;

import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineRepeat;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.musiclayout.BeatOffset;

/**
 * This strategy creates {@link BeatOffset}s for the barlines
 * and the notes of the given measure column, based on the
 * given {@link BeatOffset}s (that were created without
 * respect to barlines).
 * 
 * @author Andreas Wenger
 */
public class BarlinesBeatOffsetsStrategy {

	//additional 1 IS when using a repeat sign
	public static final float REPEAT_SPACE = 1;
	//2 IS after a mid-measure barline
	public static final float MID_BARLINE_SPACE = 2;


	/**
	 * Computes and returns updated {@link BeatOffset}s. The first component are
	 * the note offsets, the second one the barline offsets.
	 */
	public Tuple2<IList<BeatOffset>, IList<BeatOffset>> computeBeatOffsets(
		List<BeatOffset> baseOffsets, ColumnHeader columnHeader, float interlineSpace) {

		CList<BeatOffset> retNotes = clist(baseOffsets);
		CList<BeatOffset> retBarlines = clist();
		//start barline
		retBarlines.add(new BeatOffset(Fraction._0, 0));
		Barline startBarline = columnHeader.getStartBarline();
		if (startBarline != null && startBarline.getRepeat() == BarlineRepeat.Forward) {
			//forward repeat: move all beats REPEAT_SPACE IS backward
			float move = REPEAT_SPACE * interlineSpace;
			for (int i = 0; i < retNotes.size(); i++) {
				BeatOffset oldOffset = retNotes.get(i);
				retNotes.set(i, new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
			}
		}
		//mid-measure barlines: add MID_BARLINE_SPACE IS for each
		for (BeatE<Barline> midBarline : columnHeader.getMiddleBarlines()) {
			//get beat of barline, find it in the note offsets and move the following ones
			Fraction beat = midBarline.beat;
			int i = 0;
			float move = 0;
			for (; i < retNotes.size(); i++) {
				if (retNotes.get(i).getBeat().compareTo(beat) >= 0) {
					BarlineRepeat repeat = midBarline.element.getRepeat();
					if (repeat == BarlineRepeat.Backward) {
						//backward repeat: additional space before barline
						move += REPEAT_SPACE * interlineSpace;
						BeatOffset oldOffset = retNotes.get(i);
						retBarlines.add(new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
					}
					else if (repeat == BarlineRepeat.Forward) {
						//forward repeat: additional space after barline
						BeatOffset oldOffset = retNotes.get(i);
						retBarlines.add(new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
						move += REPEAT_SPACE * interlineSpace;
					}
					else if (repeat == BarlineRepeat.Both) {
						//forward and backward repeat: additional space before and after barline
						move += REPEAT_SPACE * interlineSpace;
						BeatOffset oldOffset = retNotes.get(i);
						retBarlines.add(new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
						move += REPEAT_SPACE * interlineSpace;
					}
					else {
						retBarlines.add(retNotes.get(i));
					}
					move += MID_BARLINE_SPACE * interlineSpace;
					break;
				}
			}
			for (; i < retNotes.size(); i++) {
				//move following notes
				BeatOffset oldOffset = retNotes.get(i);
				retNotes.set(i, new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
			}
		}
		//end barline
		BeatOffset lastOffset = retNotes.get(retNotes.size() - 1);
		Barline endBarline = columnHeader.getEndBarline();
		if (endBarline != null && endBarline.getRepeat() == BarlineRepeat.Backward) {
			//backward repeat: additional space before end barline
			float move = REPEAT_SPACE * interlineSpace;
			retBarlines.add(new BeatOffset(lastOffset.getBeat(), lastOffset.getOffsetMm() + move));
		}
		else {
			retBarlines.add(lastOffset);
		}
		//return result
		return t(retNotes.close(), retBarlines.close());
	}

}
