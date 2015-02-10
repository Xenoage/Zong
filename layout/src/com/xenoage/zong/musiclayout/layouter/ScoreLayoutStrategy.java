package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.musiclayout.notator.Notator.notator;
import static com.xenoage.zong.musiclayout.notator.beam.BeamNotator.beamNotator;
import static com.xenoage.zong.musiclayout.spacer.frame.FrameSpacer.frameSpacer;
import static com.xenoage.zong.musiclayout.spacer.measure.ColumnSpacer.columnSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.iterators.It;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.util.VoiceElementIterator;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.Target;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayoutStrategy;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.spacer.frame.fill.FrameFiller;
import com.xenoage.zong.musiclayout.spacer.system.fill.SystemFiller;
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
	private final ScoreFrameLayoutStrategy scoreFrameLayoutStrategy;


	/**
	 * Creates a new {@link ScoreLayoutStrategy}.
	 */
	public ScoreLayoutStrategy(
		ScoreFrameLayoutStrategy scoreFrameLayoutStrategy) {
		this.scoreFrameLayoutStrategy = scoreFrameLayoutStrategy;
	}

	/**
	 * Computes and returns the layout of the given score
	 * within the given chain of score frames.
	 */
	public ScoreLayout computeScoreLayout(ScoreLayouterContext lc) {
		
		//notations of elements
		Context context = new Context();
		context.score = lc.getScore();
		context.settings = lc.getLayoutSettings();
		context.symbols = lc.getSymbolPool();
		context.mp = mp0;
		Notations notations = notator.computeAll(context);
		
		//optimal measure spacings
		List<ColumnSpacing> columnSpacings = computeColumnSpacings(notations, context);

		//break into systems and frames
		Target target = new Target(lc.getAreas(), lc.getAdditionalArea(), lc.isCompleteLayout());
		List<FrameSpacing> frames = computeFrameArrangements(target, context,
			columnSpacings, notations);
		
		//system stretching (horizontal)
		fillSystemsHorizontally(frames, target);
		//frame filling (vertical)
		fillFramesVertically(frames, target, context.score);
		
		//compute beams
		computeBeamNotations(columnSpacings, context);
		
		//create score layout from the collected information
		List<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(frames,
			notations, lc);
		
		//create score layout
		return new ScoreLayout(context.score, scoreFrameLayouts, context.symbols, context.settings);
	}

	/**
	 * Computes the {@link ColumnSpacing} for each measure
	 * (without leading spacing).
	 */
	List<ColumnSpacing> computeColumnSpacings(Notations notations, Context context) {
		ArrayList<ColumnSpacing> ret = alist();
		for (int iMeasure : range(0, context.score.getMeasuresCount() - 1)) {
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
	 * Fills the systems horizontally according to the {@link SystemFiller}
	 * of the frame.
	 */
	void fillSystemsHorizontally(List<FrameSpacing> frames, Target target) {
		for (int iFrame : range(frames)) {
			FrameSpacing frameArr = frames.get(iFrame);
			SystemFiller hFill = target.getArea(iFrame).hFill;
			//apply strategy
			for (SystemSpacing oldSystemArr : frameArr.getSystems()) {
				float usableWidth = frameArr.getUsableSizeMm().width - oldSystemArr.getMarginLeftMm() -
					oldSystemArr.getMarginRightMm();
				hFill.compute(oldSystemArr, usableWidth);
			}
		}
	}

	/**
	 * Fills the frames vertically according to the {@link FrameFiller} of the frame.
	 */
	void fillFramesVertically(List<FrameSpacing> frames, Target target, Score score) {
		for (int iFrame : range(frames)) {
			FrameFiller vFill = target.getArea(iFrame).vFill;
			vFill.compute(frames.get(iFrame), score);
		}
	}

	/**
	 * Computes beamed notations with correct stem alignments.
	 */
	void computeBeamNotations(List<ColumnSpacing> columnSpacings, Context context) {
		//go through all elements, finding beams, and recompute stem alignment
		VoiceElementIterator voiceElementsIt = new VoiceElementIterator(context.score);
		for (VoiceElement e : voiceElementsIt) {
			if (e instanceof Chord) {
				Chord chord = (Chord) e;
				//compute each beam only one time (when the end waypoint is found)
				Beam beam = chord.getBeam();
				if (beam != null && beam.getStop().getChord() == chord) {
					beamNotator.compute(beam, columnSpacings);
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
