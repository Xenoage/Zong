package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.layouter.arrangement.FrameArrangementStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.BeamedStemAlignmentNotationsStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.BeamedStemDirectionNotationsStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.columnspacing.ColumnSpacingStrategy;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayoutStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.voicenotation.VoiceStemDirectionNotationsStrategy;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * This strategy creates a {@link ScoreLayout} from a given
 * {@link Score} and a list of {@link ScoreFrame}s.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayoutStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	private final NotationStrategy notationStrategy;
	private final BeamedStemDirectionNotationsStrategy beamedStemDirectionNotationsStrategy;
	private final VoiceStemDirectionNotationsStrategy voiceStemDirectionNotationsStrategy;
	private final ColumnSpacingStrategy measureColumnSpacingStrategy;
	private final FrameArrangementStrategy frameArrangementStrategy;
	private final BeamedStemAlignmentNotationsStrategy beamedStemAlignmentNotationsStrategy;
	private final ScoreFrameLayoutStrategy scoreFrameLayoutStrategy;


	/**
	 * Creates a new {@link ScoreLayoutStrategy}.
	 */
	public ScoreLayoutStrategy(NotationStrategy notationStrategy,
		BeamedStemDirectionNotationsStrategy beamedStemDirectionNotationsStrategy,
		VoiceStemDirectionNotationsStrategy voiceStemDirectionNotationsStrategy,
		ColumnSpacingStrategy measureColumnSpacingStrategy,
		FrameArrangementStrategy frameArrangementStrategy,
		BeamedStemAlignmentNotationsStrategy beamedStemAlignmentNotationsStrategy,
		ScoreFrameLayoutStrategy scoreFrameLayoutStrategy) {
		this.notationStrategy = notationStrategy;
		this.beamedStemDirectionNotationsStrategy = beamedStemDirectionNotationsStrategy;
		this.voiceStemDirectionNotationsStrategy = voiceStemDirectionNotationsStrategy;
		this.measureColumnSpacingStrategy = measureColumnSpacingStrategy;
		this.frameArrangementStrategy = frameArrangementStrategy;
		this.beamedStemAlignmentNotationsStrategy = beamedStemAlignmentNotationsStrategy;
		this.scoreFrameLayoutStrategy = scoreFrameLayoutStrategy;
	}

	/**
	 * Computes and returns the layout of the given score
	 * within the given chain of score frames.
	 */
	public ScoreLayout computeScoreLayout(ScoreLayouterContext lc) {
		Score score = lc.getScore();
		//notations of elements
		NotationsCache notations = notationStrategy.computeNotations(lc.getScore(), lc.getSymbolPool(),
			lc.getLayoutSettings());
		//update beamed notations with correct stem directions
		notations.merge(computeBeamStemDirections(notations, lc));
		//TODO: stem directions dependent on their voice
		for (int iStaff : range(0, score.getStavesCount() - 1)) {
			notations.merge(voiceStemDirectionNotationsStrategy.computeNotations(iStaff, notations, lc));
		}
		//optimal measure spacings
		List<ColumnSpacing> optimalMeasureColumnSpacings = computeColumnSpacings(notations, lc);
		//break into systems and frames
		Tuple2<ArrayList<FrameArrangement>, NotationsCache> t = computeFrameArrangements(
			optimalMeasureColumnSpacings, notations, lc);
		List<FrameArrangement> frameArrangements = t.get1();
		notations.merge(t.get2());
		//system stretching (horizontal)
		frameArrangements = fillSystemsHorizontally(frameArrangements, lc);
		//frame filling (vertical)
		frameArrangements = fillFramesVertically(frameArrangements, lc);
		//compute beams
		notations.merge(computeBeamStemAlignments(frameArrangements, optimalMeasureColumnSpacings,
			notations, lc));
		//create score layout from the collected information
		List<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(frameArrangements,
			notations, lc);
		//create score layout
		return new ScoreLayout(lc.getScore(), scoreFrameLayouts, lc.getSymbolPool(), lc.getLayoutSettings());
	}

	/**
	 * Computes beamed notations with correct stem directions.
	 */
	NotationsCache computeBeamStemDirections(NotationsCache notations, ScoreLayouterContext lc) {
		NotationsCache ret = new NotationsCache();
		//go again through all elements, finding beams, and recompute stem direction
		Score score = lc.getScore();
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			Column measureColumn = score.getColumn(iMeasure);
			for (int iStaff : range(measureColumn)) {
				Measure measure = measureColumn.get(iStaff);
				for (Voice voice : measure.getVoices()) {
					for (MusicElement element : voice.getElements()) {
						if (element instanceof Chord) {
							Chord chord = (Chord) element;
							//compute each beam only one time (when the end waypoint is found)
							Beam beam = chord.getBeam();
							if (beam != null && beam.getStop().getChord() == chord) {
								ret.merge(beamedStemDirectionNotationsStrategy.computeNotations(beam, notations,
									lc.getScore(), lc.getLayoutSettings()));
							}
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Computes the {@link ColumnSpacing} for each measure
	 * (without leading spacing).
	 */
	List<ColumnSpacing> computeColumnSpacings(NotationsCache notations, ScoreLayouterContext lc) {
		Score score = lc.getScore();
		ArrayList<ColumnSpacing> ret = alist();
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			ret.add(measureColumnSpacingStrategy.computeColumnSpacing(iMeasure, false, notations, lc)
				.get1()); //TODO: also save optimal voice spacings for later reuse
		}
		return ret;
	}

	/**
	 * Breaks the given measure column spacings into systems and frames.
	 * The list of frame arrangements and the list of created leading notations
	 * is returned.
	 */
	Tuple2<ArrayList<FrameArrangement>, NotationsCache> computeFrameArrangements(
		List<ColumnSpacing> measureColumnSpacings, NotationsCache notations, ScoreLayouterContext lc) {
		ArrayList<FrameArrangement> ret = alist();
		NotationsCache retLeadingNotations = new NotationsCache();
		int measuresCount = lc.getScore().getMeasuresCount();
		int iMeasure = 0;
		int iSystem = 0;
		int iFrame = 0;
		boolean additionalFrameIteration = false;
		while (true) {
			additionalFrameIteration = false;

			//find score frame
			Size2f frameSize;
			if (iFrame < lc.getAreas().size()) {
				//there is another existing score frame
				frameSize = lc.getArea(iFrame).getSize();
			}
			else {
				//there is no another existing score frame
				if (false == lc.isCompleteLayout()) {
					//we are not interested in the stuff after the last score frame. exit.
					break;
				}
				else if (iMeasure >= measuresCount) {
					//all measures layouted. exit.
					break;
				}
				else {
					//still material to layout and additional frames requested. create one.
					frameSize = lc.getAdditionalArea().getSize();
					additionalFrameIteration = true;
				}
			}

			//some material left to layout?
			if (iMeasure < measuresCount) {
				//more measures to layout
				Tuple2<FrameArrangement, NotationsCache> t = frameArrangementStrategy
					.computeFrameArrangement(iMeasure, iSystem, frameSize, measureColumnSpacings, notations,
						lc);
				FrameArrangement frameArr = t.get1();
				NotationsCache leadingNotations = t.get2();
				if (frameArr.getSystems().size() > 0) {
					//at least one measure in this frame. remember frame arrangement and leading notations
					ret.add(frameArr);
					retLeadingNotations.merge(leadingNotations);
					iMeasure = frameArr.getSystems().getLast().getEndMeasureIndex() + 1;
					iSystem += frameArr.getSystems().size();
				}
				else {
					//no space for a measure in this frame. empty frame. but only, if existing frame.
					//do not create endless list of empty additional frames!
					if (!additionalFrameIteration) {
						ret.add(new FrameArrangement(CList.<SystemArrangement>ilist(), frameSize));
					}
					else {
						break;
					}
				}
			}
			else {
				//no material left. empty frame.
				ret.add(new FrameArrangement(CList.<SystemArrangement>ilist(), frameSize));
			}

			//next frame
			iFrame++;
		}
		return t(ret, retLeadingNotations);
	}

	/**
	 * Fills the systems horizontally according to the {@link HorizontalSystemFillingStrategy}
	 * of the frame.
	 */
	List<FrameArrangement> fillSystemsHorizontally(List<FrameArrangement> frameArrangements,
		ScoreLayouterContext lc) {
		ArrayList<FrameArrangement> ret = alist();
		for (int iFrame : range(frameArrangements)) {
			FrameArrangement frameArr = frameArrangements.get(iFrame);
			HorizontalSystemFillingStrategy hFill = lc.getArea(iFrame).getHFill();
			if (hFill != null) {
				//apply strategy
				CList<SystemArrangement> systemArrs = clist();
				for (SystemArrangement oldSystemArr : frameArr.getSystems()) {
					float usableWidth = frameArr.getUsableSize().width - oldSystemArr.getMarginLeft() -
						oldSystemArr.getMarginRight();
					systemArrs.add(hFill.computeSystemArrangement(oldSystemArr, usableWidth));
				}
				ret.add(new FrameArrangement(systemArrs.close(), frameArr.getUsableSize()));
			}
			else {
				//unmodified frame
				ret.add(frameArr);
			}
		}
		return ret;
	}

	/**
	 * Fills the frames vertically according to the {@link VerticalFrameFillingStrategy}
	 * of the frame.
	 */
	List<FrameArrangement> fillFramesVertically(List<FrameArrangement> frameArrs,
		ScoreLayouterContext lc) {
		ArrayList<FrameArrangement> ret = alist();
		Score score = lc.getScore();
		for (int iFrame : range(frameArrs)) {
			VerticalFrameFillingStrategy vFill = lc.getArea(iFrame).getVFill();
			if (vFill != null) {
				ret.add(vFill.computeFrameArrangement(frameArrs.get(iFrame), score));
			}
		}
		return ret;
	}

	/**
	 * Computes beamed notations with correct stem alignments.
	 */
	NotationsCache computeBeamStemAlignments(List<FrameArrangement> frameArrangements,
		List<ColumnSpacing> optimalMeasureColumnSpacings, NotationsCache notations,
		ScoreLayouterContext lc) {
		NotationsCache ret = new NotationsCache();
		//collect actual measure column spacings from all frames
		//(now also including leading spacings and possibly stretched measures)
		Score score = lc.getScore();
		ArrayList<ColumnSpacing> columnSpacings = alist();
		for (FrameArrangement frameArr : frameArrangements) {
			for (SystemArrangement systemArr : frameArr.getSystems()) {
				columnSpacings.addAll(systemArr.getColumnSpacings());
			}
		}
		//if not all measures were arranged (because of missing space), add their
		//optimal spacings to the list
		for (int iMeasure = columnSpacings.size(); iMeasure < score.getMeasuresCount(); iMeasure++) {
			columnSpacings.add(optimalMeasureColumnSpacings.get(iMeasure));
		}
		//go again through all elements, finding beams, and recompute stem alignment
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			Column measureColumn = score.getColumn(iMeasure);
			for (Measure measure : measureColumn) {
				for (Voice voice : measure.getVoices()) {
					for (MusicElement element : voice.getElements()) {
						if (element instanceof Chord) {
							Chord chord = (Chord) element;
							//compute each beam only one time (when the end waypoint is found)
							Beam beam = chord.getBeam();
							if (beam != null && beam.getStop().getChord() == chord) {
								ret.merge(beamedStemAlignmentNotationsStrategy.computeNotations(lc.getScore(),
									beam, columnSpacings, notations));
							}
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Creates all {@link ScoreFrameLayout}s.
	 */
	List<ScoreFrameLayout> createScoreFrameLayouts(List<FrameArrangement> frameArrs,
		NotationsCache notations, ScoreLayouterContext lc) {
		ArrayList<ScoreFrameLayout> ret = alist();
		ArrayList<ContinuedElement> continuedElements = alist();
		It<FrameArrangement> itFrameArrs = it(frameArrs);
		for (FrameArrangement frameArr : itFrameArrs) {
			ScoreFrameLayout sfl = scoreFrameLayoutStrategy.computeScoreFrameLayout(frameArr,
				itFrameArrs.getIndex(), notations, continuedElements, lc);
			ret.add(sfl);
			continuedElements = sfl.getContinuedElements();
		}
		return ret;
	}

}
