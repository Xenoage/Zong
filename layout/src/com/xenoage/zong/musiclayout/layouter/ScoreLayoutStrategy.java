package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.pdlib.IVector.ivec;
import static com.xenoage.utils.pdlib.It.it;
import static com.xenoage.zong.core.music.util.Column.column;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.pdlib.IVector;
import com.xenoage.utils.pdlib.It;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Globals;
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
	implements ScoreLayouterStrategy
{

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
		ColumnSpacingStrategy measureColumnSpacingStrategy, FrameArrangementStrategy frameArrangementStrategy,
		BeamedStemAlignmentNotationsStrategy beamedStemAlignmentNotationsStrategy,
		ScoreFrameLayoutStrategy scoreFrameLayoutStrategy)
	{
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
	public ScoreLayout computeScoreLayout(ScoreLayouterContext lc)
	{
		Score score = lc.getScore();
		//notations of elements
		NotationsCache notations = notationStrategy.computeNotations(lc.getScore(), lc.getSymbolPool(),
			lc.layoutSettings);
		//update beamed notations with correct stem directions
		notations.merge(computeBeamStemDirections(notations, lc));
		//TODO: stem directions dependent on their voice
		for (int iStaff : range(0, score.getStavesCount() - 1)) {
			notations.merge(voiceStemDirectionNotationsStrategy.computeNotations(iStaff, notations, lc));
		}
		//optimal measure spacings
		Vector<ColumnSpacing> optimalMeasureColumnSpacings = computeColumnSpacings(notations, lc);
		//break into systems and frames
		Tuple2<Vector<FrameArrangement>, NotationsCache> t = computeFrameArrangements(
			optimalMeasureColumnSpacings, notations, lc);
		Vector<FrameArrangement> frameArrangements = t.get1();
		notations.merge(t.get2());
		//system stretching (horizontal)
		frameArrangements = fillSystemsHorizontally(frameArrangements, lc);
		//frame filling (vertical)
		frameArrangements = fillFramesVertically(frameArrangements, lc);
		//compute beams
		notations
			.merge(computeBeamStemAlignments(frameArrangements, optimalMeasureColumnSpacings, notations, lc));
		//create score layout from the collected information
		Vector<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(frameArrangements, notations, lc);
		//create score layout
		return new ScoreLayout(lc.score, scoreFrameLayouts, lc.symbolPool, lc.layoutSettings);
	}


	/**
	 * Computes beamed notations with correct stem directions.
	 */
	NotationsCache computeBeamStemDirections(NotationsCache notations, ScoreLayouterContext lc)
	{
		NotationsCache ret = NotationsCache.create();
		//go again through all elements, finding beams, and recompute stem direction
		Score score = lc.getScore();
		Globals globals = score.globals;
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			Column measureColumn = column(score, iMeasure);
			for (int iStaff : range(measureColumn)) {
				Measure measure = measureColumn.get(iStaff);
				for (Voice voice : measure.voices) {
					for (MusicElement element : voice.elements) {
						if (element instanceof Chord) {
							Chord chord = (Chord) element;
							//compute each beam only one time (when the end waypoint is found)
							Beam beam = globals.getBeams().get(chord);
							if (beam != null && beam.getLastWaypoint().getChord() == chord) {
								ret.merge(beamedStemDirectionNotationsStrategy.computeNotations(beam, notations,
									lc.getScore(), lc.layoutSettings));
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
	Vector<ColumnSpacing> computeColumnSpacings(NotationsCache notations, ScoreLayouterContext lc)
	{
		Score score = lc.getScore();
		IVector<ColumnSpacing> ret = ivec();
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			ret.add(measureColumnSpacingStrategy.computeColumnSpacing(
				iMeasure, false, notations, lc).get1()); //TODO: also save optimal voice spacings for later reuse
		}
		return ret.close();
	}


	/**
	 * Breaks the given measure column spacings into systems and frames.
	 * The list of frame arrangements and the list of created leading notations
	 * is returned.
	 */
	Tuple2<Vector<FrameArrangement>, NotationsCache> computeFrameArrangements(
		Vector<ColumnSpacing> measureColumnSpacings, NotationsCache notations, ScoreLayouterContext lc)
	{
		IVector<FrameArrangement> ret = ivec();
		NotationsCache retLeadingNotations = NotationsCache.create();
		int measuresCount = lc.getScore().getMeasuresCount();
		int iMeasure = 0;
		int iSystem = 0;
		int iFrame = 0;
		boolean additionalFrameIteration = false;
		while (true) {
			additionalFrameIteration = false;

			//find score frame
			Size2f frameSize;
			if (iFrame < lc.areas.size()) {
				//there is another existing score frame
				frameSize = lc.areas.get(iFrame).size;
			} else {
				//there is no another existing score frame
				if (!lc.isCompleteLayout) {
					//we are not interested in the stuff after the last score frame. exit.
					break;
				} else if (iMeasure >= measuresCount) {
					//all measures layouted. exit.
					break;
				} else {
					//still material to layout and additional frames requested. create one.
					frameSize = lc.additionalArea.size;
					additionalFrameIteration = true;
				}
			}

			//some material left to layout?
			if (iMeasure < measuresCount) {
				//more measures to layout
				Tuple2<FrameArrangement, NotationsCache> t = frameArrangementStrategy.computeFrameArrangement(
					iMeasure, iSystem, frameSize, measureColumnSpacings, notations, lc);
				FrameArrangement frameArr = t.get1();
				NotationsCache leadingNotations = t.get2();
				if (frameArr.getSystems().size() > 0) {
					//at least one measure in this frame. remember frame arrangement and leading notations
					ret.add(frameArr);
					retLeadingNotations.merge(leadingNotations);
					iMeasure = frameArr.getSystems().getLast().getEndMeasureIndex() + 1;
					iSystem += frameArr.getSystems().size();
				} else {
					//no space for a measure in this frame. empty frame. but only, if existing frame.
					//do not create endless list of empty additional frames!
					if (!additionalFrameIteration) {
						ret.add(new FrameArrangement(new IVector<SystemArrangement>().close(), frameSize));
					} else {
						break;
					}
				}
			} else {
				//no material left. empty frame.
				ret.add(new FrameArrangement(new IVector<SystemArrangement>().close(), frameSize));
			}

			//next frame
			iFrame++;
		}
		return t((Vector<FrameArrangement>) ret.close(), retLeadingNotations);
	}


	/**
	 * Fills the systems horizontally according to the {@link HorizontalSystemFillingStrategy}
	 * of the frame.
	 */
	Vector<FrameArrangement> fillSystemsHorizontally(Vector<FrameArrangement> frameArrangements,
		ScoreLayouterContext lc)
	{
		IVector<FrameArrangement> ret = ivec();
		for (int iFrame : range(frameArrangements)) {
			FrameArrangement frameArr = frameArrangements.get(iFrame);
			HorizontalSystemFillingStrategy hFill = lc.getArea(iFrame).hFill;
			if (hFill != null) {
				//apply strategy
				IVector<SystemArrangement> systemArrs = ivec();
				for (SystemArrangement oldSystemArr : frameArr.getSystems()) {
					float usableWidth = frameArr.getUsableSize().width - oldSystemArr.getMarginLeft() -
						oldSystemArr.getMarginRight();
					systemArrs.add(hFill.computeSystemArrangement(oldSystemArr, usableWidth));
				}
				ret.add(new FrameArrangement(systemArrs.close(), frameArr.getUsableSize()));
			} else {
				//unmodified frame
				ret.add(frameArr);
			}
		}
		return ret.close();
	}


	/**
	 * Fills the frames vertically according to the {@link VerticalFrameFillingStrategy}
	 * of the frame.
	 */
	Vector<FrameArrangement> fillFramesVertically(Vector<FrameArrangement> frameArrs,
		ScoreLayouterContext lc)
	{
		IVector<FrameArrangement> ret = ivec();
		Score score = lc.getScore();
		for (int iFrame : range(frameArrs)) {
			VerticalFrameFillingStrategy vFill = lc.getArea(iFrame).vFill;
			if (vFill != null) {
				ret.add(vFill.computeFrameArrangement(frameArrs.get(iFrame), score));
			}
		}
		return ret.close();
	}


	/**
	 * Computes beamed notations with correct stem alignments.
	 */
	NotationsCache computeBeamStemAlignments(Vector<FrameArrangement> frameArrangements,
		Vector<ColumnSpacing> optimalMeasureColumnSpacings, NotationsCache notations, ScoreLayouterContext lc)
	{
		NotationsCache ret = NotationsCache.create();
		//collect actual measure column spacings from all frames
		//(now also including leading spacings and possibly stretched measures)
		Score score = lc.getScore();
		Globals globals = score.globals;
		IVector<ColumnSpacing> columnSpacings = ivec();
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
		columnSpacings.close();
		//go again through all elements, finding beams, and recompute stem alignment
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			Column measureColumn = column(score, iMeasure);
			for (Measure measure : measureColumn) {
				for (Voice voice : measure.voices) {
					for (MusicElement element : voice.elements) {
						if (element instanceof Chord) {
							Chord chord = (Chord) element;
							//compute each beam only one time (when the end waypoint is found)
							Beam beam = globals.getBeams().get(chord);
							if (beam != null && beam.getLastWaypoint().getChord() == chord) {
								ret.merge(beamedStemAlignmentNotationsStrategy.computeNotations(lc.getScore(), beam,
									columnSpacings, notations));
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
	Vector<ScoreFrameLayout> createScoreFrameLayouts(Vector<FrameArrangement> frameArrs,
		NotationsCache notations, ScoreLayouterContext lc)
	{
		IVector<ScoreFrameLayout> ret = ivec();
		Vector<ContinuedElement> continuedElements = ivec();
		It<FrameArrangement> itFrameArrs = it(frameArrs);
		for (FrameArrangement frameArr : itFrameArrs) {
			ScoreFrameLayout sfl = scoreFrameLayoutStrategy.computeScoreFrameLayout(frameArr,
				itFrameArrs.getIndex(), notations, continuedElements, lc);
			ret.add(sfl);
			continuedElements = sfl.getContinuedElements();
		}
		return ret.close();
	}


}
