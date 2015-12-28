package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayouter.scoreFrameLayouter;
import static com.xenoage.zong.musiclayout.notator.Notator.notator;
import static com.xenoage.zong.musiclayout.spacer.ScoreSpacer.scoreSpacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSpacer.beamSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.iterators.It;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.util.VoiceElementIterator;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.Target;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacer.frame.fill.FrameFiller;
import com.xenoage.zong.musiclayout.spacer.system.fill.SystemFiller;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * A score layouter creates the content for
 * score frames from a given score.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayouter {

	//input
	private Context context;
	private Target target;

	//output
	private ScoreLayout layout = null;


	/**
	 * Creates a {@link ScoreLayout} for the given context and target.
	 * If an error occurs, an error layout is returned.
	 */
	public static ScoreLayout createScoreLayout(Context context, Target target) {
		return new ScoreLayouter(context, target).createScoreLayout();
	}

	public ScoreLayouter(Context context, Target target) {
		this.context = context;
		this.target = target;
	}

	public ScoreLayout createScoreLayout() {
		try {
			layout = createLayoutWithExceptions();
		} catch (Exception ex) {
			//exception during the layouting process. show error page
			log(warning("Layouting failed", ex));
			layout = ScoreLayout.createErrorLayout(context.score, context.symbols);
		}
		return layout;
	}

	
	/**
	 * Computes the whole layout and returns it.
	 * If something fails, an exception is thrown.
	 */
	public ScoreLayout createLayoutWithExceptions() {
		//notations of elements
		Notations notations = notator.computeAll(context);
		
		//compute optimal measure spacings and break into systems and frames
		ScoreSpacing scoreSpacing = scoreSpacer.compute(context, target, notations);
		
		//system stretching (horizontal)
		fillSystemsHorizontally(scoreSpacing.frames, target);
		//frame filling (vertical)
		fillFramesVertically(scoreSpacing.frames, target, context.score);
		
		//compute beam spacings
		computeBeamSpacings(scoreSpacing, notations);
		
		//create score layout from the collected information
		List<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(scoreSpacing.frames,
			notations, context);
		
		//create score layout
		return new ScoreLayout(context.score, scoreFrameLayouts, context.symbols, context.settings);
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
	 * Computes the BeamSpacings with correct stem alignments.
	 * TIDY
	 */
	void computeBeamSpacings(ScoreSpacing scoreSpacing, Notations notations) {
		//go through all elements, finding beams, and recompute stem alignment
		VoiceElementIterator voiceElementsIt = new VoiceElementIterator(scoreSpacing.score);
		for (VoiceElement e : voiceElementsIt) {
			if (e instanceof Chord) {
				Chord chord = (Chord) e;
				//compute each beam only one time (when the end waypoint is found)
				Beam beam = chord.getBeam();
				ColumnSpacing column = scoreSpacing.getColumn(getMP(chord).measure);
				ChordNotation cn = (ChordNotation) column.getElement(chord).notation;
				BeamNotation beamNot = (BeamNotation) notations.get(beam);
				if (beam != null && beam.getStop().getChord() == chord) {
					scoreSpacing.beams.put(beam,
						beamSpacer.compute(beam, notations, column.parentSystem.staves, scoreSpacing));
				}
			}
		}
	}

	/**
	 * Creates all {@link ScoreFrameLayout}s.
	 */
	List<ScoreFrameLayout> createScoreFrameLayouts(List<FrameSpacing> frameArrs,
		Notations notations, Context context) {
		ArrayList<ScoreFrameLayout> ret = alist();
		ArrayList<ContinuedElement> continuedElements = alist();
		It<FrameSpacing> itFrameArrs = it(frameArrs);
		for (FrameSpacing frameArr : itFrameArrs) {
			ScoreFrameLayout sfl = scoreFrameLayouter.computeScoreFrameLayout(frameArr,
				itFrameArrs.getIndex(), notations, continuedElements, context);
			ret.add(sfl);
			continuedElements = sfl.getContinuedElements();
		}
		return ret;
	}

}
