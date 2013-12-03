package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple3.t3;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;

import java.util.ArrayList;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.BeatOffset;
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
	private final SeparateVoiceSpacingStrategy separateVoiceSpacingStrategy;
	private final MeasureElementsSpacingsStrategy measureElementsSpacingsStrategy;
	private final BeatOffsetsStrategy beatOffsetsStrategy;
	private final BarlinesBeatOffsetsStrategy barlinesBeatOffsetsStrategy;
	private final BeatOffsetBasedVoiceSpacingStrategy beatBasedVoiceSpacingStrategy;
	private final LeadingSpacingStrategy measureLeadingSpacingStrategy;


	/**
	 * Creates a new {@link ColumnSpacingStrategy}.
	 */
	public ColumnSpacingStrategy(SeparateVoiceSpacingStrategy separateVoiceSpacingStrategy,
		MeasureElementsSpacingsStrategy measureElementsSpacingsStrategy,
		BeatOffsetsStrategy beatOffsetsStrategy,
		BarlinesBeatOffsetsStrategy barlinesBeatOffsetsStrategy,
		BeatOffsetBasedVoiceSpacingStrategy beatBasedVoiceSpacingStrategy,
		LeadingSpacingStrategy measureLeadingSpacingStrategy) {
		this.separateVoiceSpacingStrategy = separateVoiceSpacingStrategy;
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
	public Tuple3<ColumnSpacing, VoiceSpacingsByStaff, NotationsCache> computeColumnSpacing(
		int measureIndex, boolean createLeading, NotationsCache notations, ScoreLayouterContext lc) {
		Score score = lc.getScore();
		Column column = score.getColumn(measureIndex);
		ColumnHeader columnHeader = score.getHeader().getColumnHeader(measureIndex);

		//beats within this measure column
		Fraction measureBeats = score.getMeasureBeats(measureIndex);

		//compute the optimal spacings for each voice separately
		CList<IList<VoiceSpacing>> optimalVoiceSpacingsByStaff = clist();
		for (int iStaff : range(column)) {
			CList<VoiceSpacing> vss = clist();
			Measure measure = column.get(iStaff);
			for (Voice voice : measure.getVoices()) {
				VoiceSpacing vs = separateVoiceSpacingStrategy.computeVoiceSpacing(voice,
					score.getInterlineSpace(MP.atStaff(iStaff)), notations,
					measureBeats, lc.getLayoutSettings());
				vss.add(vs);
			}
			optimalVoiceSpacingsByStaff.add(vss.close());
		}

		//compute the measure elements (like inner clefs) and accordingly updated voice spacings
		ArrayList<MeasureElementsSpacings> optimalMeasureElementsSpacingsByStaff = alist();
		for (int iStaff : range(column)) {
			Tuple2<MeasureElementsSpacings, IList<VoiceSpacing>> measureSpacing = measureElementsSpacingsStrategy
				.computeMeasureElementsSpacings(score, iStaff, measureIndex, createLeading,
					optimalVoiceSpacingsByStaff.get(iStaff), notations, lc.getLayoutSettings());
			optimalMeasureElementsSpacingsByStaff.add(measureSpacing.get1());
			optimalVoiceSpacingsByStaff.set(iStaff, measureSpacing.get2());
		}
		optimalVoiceSpacingsByStaff.close();

		//compute the beat offsets of this measure column
		VoiceSpacingsByStaff optimalVoiceSpacings = new VoiceSpacingsByStaff(
			optimalVoiceSpacingsByStaff);
		IList<BeatOffset> beatOffsets = beatOffsetsStrategy.computeBeatOffsets(optimalVoiceSpacings,
			measureBeats, lc.getLayoutSettings());

		//recompute beat offsets with respect to barlines
		Tuple2<IList<BeatOffset>, IList<BeatOffset>> offsets = barlinesBeatOffsetsStrategy
			.computeBeatOffsets(beatOffsets, columnHeader, lc.getMaxInterlineSpace());
		beatOffsets = offsets.get1();
		IList<BeatOffset> barlineOffsets = offsets.get2();

		//compute the spacings for the whole column, so that equal beats are aligned
		ArrayList<MeasureElementsSpacings> alignedMeasureElementsSpacingsByStaff = alist();
		ArrayList<IList<VoiceSpacing>> alignedVoiceSpacings = alist();
		for (int iStaff : range(column)) {
			Measure measure = column.get(iStaff);
			//voice spacings
			CList<VoiceSpacing> vs = clist();
			for (int iVoice : range(measure.getVoices())) {
				vs.add(beatBasedVoiceSpacingStrategy.computeVoiceSpacing(
					optimalVoiceSpacings.get(iStaff, iVoice), beatOffsets));
			}
			//measure elements, based on the aligned voice spacings
			alignedMeasureElementsSpacingsByStaff.add(measureElementsSpacingsStrategy
				.computeMeasureElementsSpacings(lc.getScore(), iStaff, measureIndex, createLeading, vs,
					notations, lc.getLayoutSettings()).get1());
			alignedVoiceSpacings.add(vs.close());
		}

		//compute spacings for each staff
		NotationsCache leadingNotations = (createLeading ? new NotationsCache() : null);
		CList<MeasureSpacing> measureSpacings = clist();
		for (int iStaff : range(column)) {
			//create leading spacing, if needed
			LeadingSpacing leadingSpacing = null;
			if (createLeading) {
				MusicContext mc = score.getMusicContext(atBeat(iStaff, measureIndex, 0, Fraction._0), At, null);
				Tuple2<LeadingSpacing, NotationsCache> ls = measureLeadingSpacingStrategy
					.computeLeadingSpacing(mc, iStaff, lc.getLayoutSettings());
				leadingSpacing = ls.get1();
				leadingNotations.merge(ls.get2());
			}
			//create measure spacing
			measureSpacings.add(new MeasureSpacing(atMeasure(iStaff, measureIndex), alignedVoiceSpacings
				.get(iStaff), alignedMeasureElementsSpacingsByStaff.get(iStaff), leadingSpacing));
		}
		measureSpacings.close();

		return t3(
			new ColumnSpacing(lc.getScore(), measureSpacings, beatOffsets, barlineOffsets),
			optimalVoiceSpacings, leadingNotations);
	}

}
