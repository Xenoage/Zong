package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.musiclayout.notator.Notator.notator;
import static com.xenoage.zong.musiclayout.spacer.frame.FrameSpacer.frameSpacer;
import static com.xenoage.zong.musiclayout.spacer.measure.ColumnSpacer.columnSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.Target;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.layouter.beamednotation.BeamedStemAlignmentNotationsStrategy;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayoutStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * This strategy creates a {@link ScoreLayout} from a given
 * {@link Score} and a list of {@link ScoreFrame}s.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayoutStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	private final BeamedStemAlignmentNotationsStrategy beamedStemAlignmentNotationsStrategy;
	private final ScoreFrameLayoutStrategy scoreFrameLayoutStrategy;


	/**
	 * Creates a new {@link ScoreLayoutStrategy}.
	 */
	public ScoreLayoutStrategy(
		BeamedStemAlignmentNotationsStrategy beamedStemAlignmentNotationsStrategy,
		ScoreFrameLayoutStrategy scoreFrameLayoutStrategy) {
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
		Context context = new Context();
		context.score = lc.getScore();
		context.settings = lc.getLayoutSettings();
		context.symbols = lc.getSymbolPool();
		context.mp = mp0;
		Notations notations = notator.computeAll(context);
		
		//optimal measure spacings
		List<ColumnSpacing> optimalMeasureColumnSpacings = computeColumnSpacings(notations, lc);
		
		//break into systems and frames
		Target target = new Target(lc.getAreas(), lc.getAdditionalArea(), lc.isCompleteLayout());
		List<FrameSpacing> frames = computeFrameArrangements(target, context,
			optimalMeasureColumnSpacings, notations);

		//system stretching (horizontal)
		frames = fillSystemsHorizontally(frames, target);
		//frame filling (vertical)
		frames = fillFramesVertically(frames, target, context.score);
		//compute beams
		computeBeamStemAlignments(frames, optimalMeasureColumnSpacings, notations, lc);
		//create score layout from the collected information
		List<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(frames,
			notations, lc);
		//create score layout
		return new ScoreLayout(lc.getScore(), scoreFrameLayouts, lc.getSymbolPool(), lc.getLayoutSettings());
	}

	/**
	 * Computes the {@link ColumnSpacing} for each measure
	 * (without leading spacing).
	 */
	List<ColumnSpacing> computeColumnSpacings(Notations notations, ScoreLayouterContext lc) {
		Score score = lc.getScore();
		ArrayList<ColumnSpacing> ret = alist();
		for (int iMeasure : range(0, score.getMeasuresCount() - 1)) {
			
			//TODO
			Context context = new Context();
			context.score = lc.getScore();
			context.settings = lc.getLayoutSettings();
			context.symbols = lc.getSymbolPool();
			
			context.mp = MP.atMeasure(iMeasure);
			ret.add(columnSpacer.compute(context, false, notations));
		}
		return ret;
	}

	/**
	 * Breaks the given measure column spacings into systems and frames.
	 * The list of frame arrangements is returned.
	 */
	ArrayList<FrameSpacing> computeFrameArrangements(Target target, Context context,
		List<ColumnSpacing> measureColumnSpacings, Notations notations) {
		context.saveMp();
		
		ArrayList<FrameSpacing> ret = alist();
		int measuresCount = context.score.getMeasuresCount();
		int iMeasure = 0;
		int iSystem = 0;
		int iFrame = 0;
		boolean additionalFrameIteration;
		
		while (true) {
			additionalFrameIteration = false;

			//find score frame
			Size2f frameSize;
			if (iFrame < target.areas.size()) {
				//there is another existing score frame
				frameSize = target.getArea(iFrame).size;
			}
			else {
				//there is no another existing score frame
				if (false == target.isCompleteLayout) {
					//we are not interested in the stuff after the last score frame. exit.
					break;
				}
				else if (iMeasure >= measuresCount) {
					//all measures layouted. exit.
					break;
				}
				else {
					//still material to layout and additional frames requested. create one.
					frameSize = target.additionalArea.size;
					additionalFrameIteration = true;
				}
			}

			//some material left to layout?
			if (iMeasure < measuresCount) {
				//more measures to layout
				context.mp = atMeasure(iMeasure);
				FrameSpacing frame = frameSpacer.compute(context, iSystem, frameSize,
					measureColumnSpacings, notations);
				if (frame.getSystems().size() > 0) {
					//at least one measure in this frame. remember frame
					ret.add(frame);
					iMeasure = frame.getEndMeasureIndex() + 1;
					iSystem += frame.getSystems().size();
				}
				else {
					//no space for a measure in this frame. empty frame. but only, if frame exists.
					//do not create endless list of empty additional frames
					if (!additionalFrameIteration) {
						ret.add(FrameSpacing.empty(frameSize));
					}
					else {
						break;
					}
				}
			}
			else {
				//no material left. empty frame.
				ret.add(FrameSpacing.empty(frameSize));
			}

			//next frame
			iFrame++;
		}
		
		context.restoreMp();
		return ret;
	}

	/**
	 * Fills the systems horizontally according to the {@link HorizontalSystemFillingStrategy}
	 * of the frame.
	 */
	List<FrameSpacing> fillSystemsHorizontally(List<FrameSpacing> frameArrangements, Target target) {
		ArrayList<FrameSpacing> ret = alist();
		for (int iFrame : range(frameArrangements)) {
			FrameSpacing frameArr = frameArrangements.get(iFrame);
			HorizontalSystemFillingStrategy hFill = target.getArea(iFrame).hFill;
			if (hFill != null) {
				//apply strategy
				CList<SystemSpacing> systemArrs = clist();
				for (SystemSpacing oldSystemArr : frameArr.getSystems()) {
					float usableWidth = frameArr.getUsableSizeMm().width - oldSystemArr.getMarginLeftMm() -
						oldSystemArr.getMarginRightMm();
					systemArrs.add(hFill.computeSystemArrangement(oldSystemArr, usableWidth));
				}
				ret.add(new FrameSpacing(systemArrs.close(), frameArr.getUsableSizeMm()));
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
	List<FrameSpacing> fillFramesVertically(List<FrameSpacing> frameArrs, Target target, Score score) {
		ArrayList<FrameSpacing> ret = alist();
		for (int iFrame : range(frameArrs)) {
			VerticalFrameFillingStrategy vFill = target.getArea(iFrame).vFill;
			if (vFill != null) {
				ret.add(vFill.computeFrameArrangement(frameArrs.get(iFrame), score));
			}
		}
		return ret;
	}

	/**
	 * Computes beamed notations with correct stem alignments.
	 */
	void computeBeamStemAlignments(List<FrameSpacing> frameArrangements,
		List<ColumnSpacing> optimalMeasureColumnSpacings, Notations notations,
		ScoreLayouterContext lc) {
		//collect actual measure column spacings from all frames
		//(now also including leading spacings and possibly stretched measures)
		Score score = lc.getScore();
		ArrayList<ColumnSpacing> columnSpacings = alist();
		for (FrameSpacing frameArr : frameArrangements) {
			for (SystemSpacing systemArr : frameArr.getSystems()) {
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
								beamedStemAlignmentNotationsStrategy.computeNotations(lc.getScore(),
									beam, columnSpacings, notations);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Creates all {@link ScoreFrameLayout}s.
	 */
	List<ScoreFrameLayout> createScoreFrameLayouts(List<FrameSpacing> frameArrs,
		Notations notations, ScoreLayouterContext lc) {
		ArrayList<ScoreFrameLayout> ret = alist();
		ArrayList<ContinuedElement> continuedElements = alist();
		It<FrameSpacing> itFrameArrs = it(frameArrs);
		for (FrameSpacing frameArr : itFrameArrs) {
			ScoreFrameLayout sfl = scoreFrameLayoutStrategy.computeScoreFrameLayout(frameArr,
				itFrameArrs.getIndex(), notations, continuedElements, lc);
			ret.add(sfl);
			continuedElements = sfl.getContinuedElements();
		}
		return ret;
	}

}
