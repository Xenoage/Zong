package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.lang.VocByString.voc;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.DirectionStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.LyricStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.MusicElementStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayoutStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.SlurStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.StaffStampingsStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.TupletStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.VoltaStampingStrategy;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * A score layouter creates the content for
 * score frames from a given score.
 * 
 * TIDY: why state and not just a factory?
 * currently we do not cache intermediate results
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
	 * Computes the whole layout and returns it.
	 * If something fails, an exception is thrown.
	 */
	public ScoreLayout createLayoutWithExceptions() {
		return strategy.computeScoreLayout(context);
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

}
