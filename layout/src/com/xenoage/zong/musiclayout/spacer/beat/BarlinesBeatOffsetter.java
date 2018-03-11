package com.xenoage.zong.musiclayout.spacer.beat;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineRepeat;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.musiclayout.spacing.BeatOffset;

/**
 * Computes {@link BeatOffset}s for the barlines and the notes of the given measure column,
 * based on the given {@link BeatOffset}s (that were created without respect to barlines).
 * 
 * @author Andreas Wenger
 */
public class BarlinesBeatOffsetter {
	
	public static final BarlinesBeatOffsetter barlinesBeatOffsetter = new BarlinesBeatOffsetter();

	//additional 1 IS when using a repeat sign - TIDY: move into layout settings
	static final float repeatSpace = 1;
	//2 IS after a mid-measure barline - TIDY: move into layout settings
	static final float midBarlineSpace = 2;
	
	
	@AllArgsConstructor
	public class Result {
		public List<BeatOffset> voiceElementOffsets;
		public List<BeatOffset> barlineOffsets;
	}


	public Result compute(List<BeatOffset> baseOffsets, ColumnHeader columnHeader, float maxInterlineSpace) {

		ArrayList<BeatOffset> retNotes = alist(baseOffsets);
		ArrayList<BeatOffset> retBarlines = alist();
		
		//start barline
		retBarlines.add(new BeatOffset(Fraction.Companion.get_0(), 0));
		Barline startBarline = columnHeader.getStartBarline();
		if (startBarline != null && startBarline.getRepeat() == BarlineRepeat.Forward) {
			//forward repeat: move all beats REPEAT_SPACE IS backward
			float move = repeatSpace * maxInterlineSpace;
			for (int i = 0; i < retNotes.size(); i++) {
				BeatOffset oldOffset = retNotes.get(i);
				retNotes.set(i, new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
			}
		}
		
		//mid-measure barlines
		for (BeatE<Barline> midBarline : columnHeader.getMiddleBarlines()) {
			//get beat of barline, find it in the note offsets and move the following ones
			Fraction beat = midBarline.getBeat();
			int i = 0;
			float move = 0;
			for (; i < retNotes.size(); i++) {
				if (retNotes.get(i).getBeat().compareTo(beat) >= 0) {
					BarlineRepeat repeat = midBarline.getElement().getRepeat();
					if (repeat == BarlineRepeat.Backward) {
						//backward repeat: additional space before barline
						move += repeatSpace * maxInterlineSpace;
						BeatOffset oldOffset = retNotes.get(i);
						retBarlines.add(new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
					}
					else if (repeat == BarlineRepeat.Forward) {
						//forward repeat: additional space after barline
						BeatOffset oldOffset = retNotes.get(i);
						retBarlines.add(new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
						move += repeatSpace * maxInterlineSpace;
					}
					else if (repeat == BarlineRepeat.Both) {
						//forward and backward repeat: additional space before and after barline
						move += repeatSpace * maxInterlineSpace;
						BeatOffset oldOffset = retNotes.get(i);
						retBarlines.add(new BeatOffset(oldOffset.getBeat(), oldOffset.getOffsetMm() + move));
						move += repeatSpace * maxInterlineSpace;
					}
					else {
						retBarlines.add(retNotes.get(i));
					}
					move += midBarlineSpace * maxInterlineSpace;
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
			float move = repeatSpace * maxInterlineSpace;
			retBarlines.add(new BeatOffset(lastOffset.getBeat(), lastOffset.getOffsetMm() + move));
		}
		else {
			retBarlines.add(lastOffset);
		}
		
		//return result
		retBarlines.trimToSize();
		return new Result(retNotes, retBarlines);
	}

}
