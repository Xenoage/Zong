package com.xenoage.zong.musiclayout.spacer.measure;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.musiclayout.spacer.beat.BarlinesBeatOffsetter.barlinesBeatOffsetter;
import static com.xenoage.zong.musiclayout.spacer.beat.VoicesBeatOffsetter.voicesBeatOffsetter;
import static com.xenoage.zong.musiclayout.spacer.measure.LeadingSpacer.leadingSpacer;
import static com.xenoage.zong.musiclayout.spacer.measure.MeasureElementsSpacer.measureElementsSpacer;
import static com.xenoage.zong.musiclayout.spacer.voice.AlignedVoicesSpacer.alignedVoicesSpacer;
import static com.xenoage.zong.musiclayout.spacer.voice.SingleVoiceSpacer.singleVoiceSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.VoiceSpacingsByStaff;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.spacer.beat.BarlinesBeatOffsetter;
import com.xenoage.zong.musiclayout.spacing.BeatOffset;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureElementsSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;

/**
 * Computes a {@link ColumnSpacing} from a measure column.
 * 
 * @author Andreas Wenger
 */
public class ColumnSpacer
	implements ScoreLayouterStrategy {
	
	public static final ColumnSpacer columnSpacer = new ColumnSpacer();


	/**
	 * Computes a {@link ColumnSpacing} from a measure column.
	 * @param context         the current context, with the current {@link MP} and precomputed
	 *                        element {@link Notation}s
	 * @param createLeading   true, if a leading spacing has to be created, otherwise false
	 * @param notations       the precomputed notations of the measure and voice elements
	 */
	public ColumnSpacing compute(Context context, boolean createLeading, Notations notations) {
		context.saveMp();
		
		int measureIndex = context.mp.measure;
		Column column = context.score.getColumn(measureIndex);
		ColumnHeader columnHeader = context.score.getHeader().getColumnHeader(measureIndex);

		//compute the optimal spacings for each voice separately
		List<List<VoiceSpacing>> voiceSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			List<VoiceSpacing> vss = alist();
			Measure measure = column.get(iStaff);
			for (Voice voice : measure.getVoices()) {
				context.mp = MP.atVoice(iStaff, measureIndex, measure.getVoices().indexOf(voice));
				VoiceSpacing vs = singleVoiceSpacer.compute(context, notations);
				vss.add(vs);
			}
			voiceSpacingsByStaff.add(vss);
		}

		//compute the measure elements (like inner clefs) and accordingly updated voice spacings
		ArrayList<MeasureElementsSpacing> optimalMeasureElementsSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			context.mp = MP.atMeasure(iStaff, measureIndex);
			MeasureElementsSpacing measureSpacing = measureElementsSpacer.compute(
				context, createLeading, voiceSpacingsByStaff.get(iStaff), notations);
			optimalMeasureElementsSpacingsByStaff.add(measureSpacing);
		}

		//compute the common beat offsets of this measure column
		Fraction measureBeats = context.score.getMeasureBeats(measureIndex);
		VoiceSpacingsByStaff voiceSpacings = new VoiceSpacingsByStaff(voiceSpacingsByStaff);
		List<BeatOffset> beatOffsets = voicesBeatOffsetter.compute(voiceSpacings.getAll(),
			measureBeats, context.settings.offsetBeatsMinimal);

		//recompute beat offsets with respect to barlines
		BarlinesBeatOffsetter.Result offsets = barlinesBeatOffsetter.compute(
			beatOffsets, columnHeader, context.score.getMaxInterlineSpace());
		beatOffsets = offsets.voiceElementOffsets;
		List<BeatOffset> barlineOffsets = offsets.barlineOffsets;

		//compute the spacings for the whole column, so that equal beats are aligned
		ArrayList<MeasureElementsSpacing> alignedMeasureElementsSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			Measure measure = column.get(iStaff);
			//voice spacings
			for (int iVoice : range(measure.getVoices()))
				alignedVoicesSpacer.compute(voiceSpacings.get(iStaff, iVoice), beatOffsets);
			//measure elements, based on the aligned voice spacings
			context.mp = atMeasure(iStaff, measureIndex);
			alignedMeasureElementsSpacingsByStaff.add(measureElementsSpacer.compute(
				context, createLeading, voiceSpacings.getStaff(iStaff), notations));
		}
		
		//compute spacings for each staff
		List<MeasureSpacing> measureSpacings = alist(column.size());
		for (int iStaff : range(column)) {
			//create leading spacing, if needed
			LeadingSpacing leadingSpacing = null;
			if (createLeading) {
				context.mp = atBeat(iStaff, measureIndex, 0, Fraction._0);
				leadingSpacing = leadingSpacer.compute(context);
			}
			//create measure spacing
			float interlineSpace = context.score.getInterlineSpace(iStaff);
			measureSpacings.add(new MeasureSpacing(atMeasure(iStaff, measureIndex), interlineSpace,
				voiceSpacings.getStaff(iStaff), alignedMeasureElementsSpacingsByStaff.get(iStaff), leadingSpacing));
		}
		
		context.restoreMp();
		return new ColumnSpacing(measureIndex, measureSpacings, beatOffsets, barlineOffsets);
	}

}
