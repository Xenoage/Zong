package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.musiclayout.spacer.voice.SingleVoiceSpacer.singleVoiceSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterContext;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureElementsSpacings;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * A {@link ColumnSpacingStrategy}
 * computes a single {@link ColumnSpacing} from
 * the given measure column.
 * 
 * @author Andreas Wenger
 */
public class ColumnSpacingStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	private final MeasureElementsSpacingsStrategy measureElementsSpacingsStrategy;
	private final BeatOffsetsStrategy beatOffsetsStrategy;
	private final BarlinesBeatOffsetsStrategy barlinesBeatOffsetsStrategy;
	private final BeatOffsetBasedVoiceSpacingStrategy beatBasedVoiceSpacingStrategy;
	private final LeadingSpacingStrategy measureLeadingSpacingStrategy;


	/**
	 * Creates a new {@link ColumnSpacingStrategy}.
	 */
	public ColumnSpacingStrategy(
		MeasureElementsSpacingsStrategy measureElementsSpacingsStrategy,
		BeatOffsetsStrategy beatOffsetsStrategy,
		BarlinesBeatOffsetsStrategy barlinesBeatOffsetsStrategy,
		BeatOffsetBasedVoiceSpacingStrategy beatBasedVoiceSpacingStrategy,
		LeadingSpacingStrategy measureLeadingSpacingStrategy) {
		this.measureElementsSpacingsStrategy = measureElementsSpacingsStrategy;
		this.beatOffsetsStrategy = beatOffsetsStrategy;
		this.barlinesBeatOffsetsStrategy = barlinesBeatOffsetsStrategy;
		this.beatBasedVoiceSpacingStrategy = beatBasedVoiceSpacingStrategy;
		this.measureLeadingSpacingStrategy = measureLeadingSpacingStrategy;
	}

	/**
	 * Creates the optimum horizontal spacing for the given measure column.
	 * If createLeading is true, a leading spacing is created.
	 * @param measureIndex    index of this measure
	 * @param column          the measure column for which the spacing is computed
	 * @param createLeading   true, if a leading spacing has to be created, otherwise false
	 * @param notations       the already computed notations
	 * @param lc              the context of the layouter
	 * @return                the measure column spacing is returned, and also the optimal
	 *                        voice spacings for all voices within the measure column (useful,
	 *                        since these can be reused later) and the notations of the
	 *                        leading spacing, if created, otherwise null
	 */
	public Tuple2<ColumnSpacing, NotationsCache> computeColumnSpacing(
		int measureIndex, boolean createLeading, NotationsCache notations, ScoreLayouterContext lc) {
		Score score = lc.getScore();
		Column column = score.getColumn(measureIndex);
		ColumnHeader columnHeader = score.getHeader().getColumnHeader(measureIndex);

		//beats within this measure column
		Fraction measureBeats = score.getMeasureBeats(measureIndex);

		//compute the optimal spacings for each voice separately
		List<List<VoiceSpacing>> voiceSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			List<VoiceSpacing> vss = alist();
			Measure measure = column.get(iStaff);
			for (Voice voice : measure.getVoices()) {
				
				//TODO
				Context context = new Context();
				context.score = score;
				context.mp = MP.atVoice(iStaff, measureIndex, measure.getVoices().indexOf(voice));
				context.notationsCache = notations;
				context.symbols = lc.getSymbolPool();
				context.settings = lc.getLayoutSettings();
				
				VoiceSpacing vs = singleVoiceSpacer.compute(context, measureBeats);
				vss.add(vs);
			}
			voiceSpacingsByStaff.add(vss);
		}

		//compute the measure elements (like inner clefs) and accordingly updated voice spacings
		ArrayList<MeasureElementsSpacings> optimalMeasureElementsSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			MeasureElementsSpacings measureSpacing = measureElementsSpacingsStrategy
				.computeMeasureElementsSpacings(score, iStaff, measureIndex, createLeading,
					voiceSpacingsByStaff.get(iStaff), notations, lc.getLayoutSettings());
			optimalMeasureElementsSpacingsByStaff.add(measureSpacing);
		}

		//compute the beat offsets of this measure column
		VoiceSpacingsByStaff voiceSpacings = new VoiceSpacingsByStaff(voiceSpacingsByStaff);
		BeatOffset[] beatOffsets = beatOffsetsStrategy.computeBeatOffsets(voiceSpacings,
			measureBeats, lc.getLayoutSettings());

		//recompute beat offsets with respect to barlines
		Tuple2<BeatOffset[], BeatOffset[]> offsets = barlinesBeatOffsetsStrategy
			.computeBeatOffsets(beatOffsets, columnHeader, lc.getMaxInterlineSpace());
		beatOffsets = offsets.get1();
		BeatOffset[] barlineOffsets = offsets.get2();

		//compute the spacings for the whole column, so that equal beats are aligned
		ArrayList<MeasureElementsSpacings> alignedMeasureElementsSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			Measure measure = column.get(iStaff);
			//voice spacings
			for (int iVoice : range(measure.getVoices())) {
				beatBasedVoiceSpacingStrategy.computeVoiceSpacing(
					voiceSpacings.get(iStaff, iVoice), beatOffsets);
			}
			//measure elements, based on the aligned voice spacings
			alignedMeasureElementsSpacingsByStaff.add(measureElementsSpacingsStrategy
				.computeMeasureElementsSpacings(lc.getScore(), iStaff, measureIndex, createLeading,
					voiceSpacings.getStaff(iStaff),
					notations, lc.getLayoutSettings()));
		}
		
		//TODO
		Context context = new Context();
		context.notationsCache = notations;
		context.score = score;
		context.settings = lc.getLayoutSettings();
		context.symbols = lc.getSymbolPool();
		context.mp = atBeat(0, measureIndex, 0, _0);

		//compute spacings for each staff
		NotationsCache leadingNotations = (createLeading ? new NotationsCache() : null);
		MeasureSpacing[] measureSpacings = new MeasureSpacing[column.size()];
		context.saveMp();
		for (int iStaff : range(column)) {
			//create leading spacing, if needed
			LeadingSpacing leadingSpacing = null;
			if (createLeading) {
				context.mp = atBeat(iStaff, measureIndex, 0, Fraction._0);
				Tuple2<LeadingSpacing, NotationsCache> ls = measureLeadingSpacingStrategy
					.computeLeadingSpacing(context);
				leadingSpacing = ls.get1();
				leadingNotations.merge(ls.get2());
			}
			//create measure spacing
			measureSpacings[iStaff] = new MeasureSpacing(atMeasure(iStaff, measureIndex), voiceSpacings
				.getStaff(iStaff), alignedMeasureElementsSpacingsByStaff.get(iStaff), leadingSpacing);
		}
		context.restoreMp();

		return Tuple2.t(
			new ColumnSpacing(lc.getScore(), measureSpacings, beatOffsets, barlineOffsets), leadingNotations);
	}

}
