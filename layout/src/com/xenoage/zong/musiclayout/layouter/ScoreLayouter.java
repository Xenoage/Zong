package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.lang.VocByString.voc;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayouter.scoreFrameLayouter;
import static com.xenoage.zong.musiclayout.notator.Notator.notator;
import static com.xenoage.zong.musiclayout.notator.beam.BeamNotator.beamNotator;
import static com.xenoage.zong.musiclayout.spacer.ScoreSpacer.scoreSpacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSpacer.beamSpacer;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.Zong;
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
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacer.frame.fill.FrameFiller;
import com.xenoage.zong.musiclayout.spacer.system.fill.SystemFiller;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * A score layouter creates the content for
 * score frames from a given score.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayouter {

	//layouter context
	private ScoreLayouterContext context;

	//the score layout created by this layouter
	private ScoreLayout layout = null;


	/**
	 * Computes the whole layout and returns it.
	 * If something fails, an error layout is returned.
	 * @param score             the score to layout
	 * @param symbolPool        the pool of musical symbols
	 * @param layoutSettings    general layout preferences
	 * @param isCompleteLayout  true to layout the whole score, false to layout
	 *                          only the frames of the score frame chain
	 * @param areas             information about the score frames in which to layout
	 * @param additionalArea    if the given areas are not enough, additional areas with
	 *                          this settings are used
	 */
	public static ScoreLayout createScoreLayout(Score score, SymbolPool symbolPool,
		LayoutSettings layoutSettings, boolean isCompleteLayout, List<ScoreLayoutArea> areas,
		ScoreLayoutArea additionalArea) {
		ScoreLayouterContext context = new ScoreLayouterContext(score, symbolPool, layoutSettings,
			isCompleteLayout, areas, additionalArea);
		return new ScoreLayouter(context).createScoreLayout();
	}

	/**
	 * Computes the whole layout and returns it.
	 * If something fails, an error layout is returned.
	 * @param score             the score to layout
	 * @param symbolPool        the pool of musical symbols
	 * @param layoutSettings    general layout preferences
	 * @param isCompleteLayout  true to layout the whole score, false to layout
	 *                          only the frames of the score frame chain
	 * @param areaSize          size of all score frames
	 */
	public ScoreLayouter(Score score, SymbolPool symbolPool, LayoutSettings layoutSettings,
		boolean isCompleteLayout, Size2f areaSize) {
		this.context = new ScoreLayouterContext(score, symbolPool, layoutSettings,
			isCompleteLayout, CList.<ScoreLayoutArea>ilist(), new ScoreLayoutArea(areaSize));
	}

	public ScoreLayouter(ScoreLayouterContext c) {
		this.context = c;
	}

	public ScoreLayout createScoreLayout() {
		try {
			//long startTime = System.currentTimeMillis();
			layout = createLayoutWithExceptions();
			//long duration = System.currentTimeMillis() - startTime;
			//System.out.println(this.getClass().getSimpleName() + ": " + duration);
		} catch (Exception ex) {
			//exception during the layouting process.
			//show error page, but still allow saving or other things
			log(warning("Layouting failed", ex));
			layout = ScoreLayout.createErrorLayout(context.getScore(), context.getSymbolPool());
		}
		return layout;
	}

	/**
	 * Gets the localized name of the given layouter strategy class, e.g.
	 * "Empty staff lines over the whole page".
	 * 
	 * TIDY: move elsewhere
	 */
	public static String getName(Class<?> strategyClass) {
		String className = strategyClass.getName();
		if (className.startsWith(Zong.projectPackage + "."))
			className = className.substring((Zong.projectPackage + ".").length());
		return Lang.get(voc(className));
	}
	
	/**
	 * Computes the whole layout and returns it.
	 * If something fails, an exception is thrown.
	 */
	public ScoreLayout createLayoutWithExceptions() {
		
		//notations of elements
		Context context = new Context();
		context.score = this.context.getScore();
		context.settings = this.context.getLayoutSettings();
		context.symbols = this.context.getSymbolPool();
		context.mp = mp0;
		Notations notations = notator.computeAll(context);
		
		//compute optimal measure spacings and break into systems and frames
		Target target = new Target(this.context.getAreas(), this.context.getAdditionalArea(), this.context.isCompleteLayout());  //TODO: target should be a param
		ScoreSpacing scoreSpacing = scoreSpacer.compute(context, target, notations);
		
		//system stretching (horizontal)
		fillSystemsHorizontally(scoreSpacing.frames, target);
		//frame filling (vertical)
		fillFramesVertically(scoreSpacing.frames, target, context.score);
		
		//compute beams - GOON do this earlier
		computeBeamNotations(scoreSpacing);
		
		//compute beam spacings
		computeBeamSpacings(scoreSpacing);
		
		//create score layout from the collected information
		List<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(scoreSpacing.frames,
			notations, this.context);
		
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
	 * Computes the {@link BeamNotation}s.
	 */
	void computeBeamNotations(ScoreSpacing scoreSpacing) {
		//go through all elements, finding beams, and recompute stem alignment
		VoiceElementIterator voiceElementsIt = new VoiceElementIterator(scoreSpacing.score);
		for (VoiceElement e : voiceElementsIt) {
			if (e instanceof Chord) {
				Chord chord = (Chord) e;
				//compute each beam only one time (when the end waypoint is found)
				Beam beam = chord.getBeam();
				if (beam != null && beam.getStop().getChord() == chord) {
					beamNotator.compute(beam, scoreSpacing);
				}
			}
		}
	}
	
	/**
	 * Computes the BeamSpacings with correct stem alignments.
	 * TIDY
	 */
	void computeBeamSpacings(ScoreSpacing scoreSpacing) {
		//go through all elements, finding beams, and recompute stem alignment
		VoiceElementIterator voiceElementsIt = new VoiceElementIterator(scoreSpacing.score);
		for (VoiceElement e : voiceElementsIt) {
			if (e instanceof Chord) {
				Chord chord = (Chord) e;
				//compute each beam only one time (when the end waypoint is found)
				Beam beam = chord.getBeam();
				ColumnSpacing column = scoreSpacing.getColumn(getMP(chord).measure);
				ChordNotation cn = (ChordNotation) column.getElement(chord).notation;
				if (beam != null && beam.getStop().getChord() == chord) {
					scoreSpacing.beams.put(beam,
						beamSpacer.compute(cn.beam, column.parentSystem.staves, scoreSpacing));
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
			ScoreFrameLayout sfl = scoreFrameLayouter.computeScoreFrameLayout(frameArr,
				itFrameArrs.getIndex(), notations, continuedElements, lc);
			ret.add(sfl);
			continuedElements = sfl.getContinuedElements();
		}
		return ret;
	}

}
