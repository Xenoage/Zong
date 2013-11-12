package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.lang.VocByString.voc;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea.area;

import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.arrangement.FrameArrangementStrategy;
import com.xenoage.zong.musiclayout.layouter.arrangement.SystemArrangementStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.BeamedStemAlignmentNotationsStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.BeamedStemDirectionNotationsStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.BarlinesBeatOffsetsStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.BeatOffsetBasedVoiceSpacingStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.BeatOffsetsStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.ColumnSpacingStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.LeadingSpacingStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.MeasureElementsSpacingsStrategy;
import com.xenoage.zong.musiclayout.layouter.columnspacing.SeparateVoiceSpacingStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.AccidentalsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.ArticulationsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotesAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemDirectionStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.BeamStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.CurvedLineStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.DirectionStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.LyricStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.MusicElementStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayoutStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.StaffStampingsStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.TupletStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.VoltaStampingStrategy;
import com.xenoage.zong.musiclayout.layouter.voicenotation.VoiceStemDirectionNotationsStrategy;
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
public class ScoreLayouter
{
  
  //the score layout created by this layouter
  private ScoreLayout layout = null;
  
  //strategy for the layouter and its context
  private ScoreLayoutStrategy strategy;
  private ScoreLayouterContext context;
  
  
  /**
   * Creates a new ScoreLayouter.
   * @param score             the score to layout
	 * @param symbolPool        the pool of musical symbols
	 * @param layoutSettings    general layout preferences
	 * @param isCompleteLayout  true to layout the whole score, false to layout
	 *                          only the frames of the score frame chain
	 * @param areas             information about the score frames in which to layout
	 * @param additionalArea    if the given areas are not enough, additional areas with
	 *                          this settings are used
	 */
	public ScoreLayouter(Score score, 
		SymbolPool symbolPool, LayoutSettings layoutSettings,
		boolean isCompleteLayout, PVector<ScoreLayoutArea> areas, ScoreLayoutArea additionalArea)
	{
    this.context = new ScoreLayouterContext(score, symbolPool, layoutSettings,
    	isCompleteLayout, areas, additionalArea);
    this.strategy = createStrategyTree();
  }
	
	
	/**
   * Creates a new ScoreLayouter.
   * @param score             the score to layout
	 * @param symbolPool        the pool of musical symbols
	 * @param layoutSettings    general layout preferences
	 * @param isCompleteLayout  true to layout the whole score, false to layout
	 *                          only the frames of the score frame chain
	 * @param areaSize          size of all score frames
	 */
	public ScoreLayouter(Score score, 
		SymbolPool symbolPool, LayoutSettings layoutSettings,
		boolean isCompleteLayout, Size2f areaSize)
	{
    this.context = new ScoreLayouterContext(score, symbolPool, layoutSettings,
    	isCompleteLayout, pvec(area(areaSize)), area(areaSize));
    this.strategy = createStrategyTree();
  }
  
  
  /**
   * Recomputes the whole layout and returns it.
   * If something fails, an error layout is returned.
   */
  public ScoreLayout createLayout()
  {
  	try
  	{
  		long startTime = System.currentTimeMillis();
  		layout = createLayoutWithExceptions();
  		long duration = System.currentTimeMillis() - startTime;
  		System.out.println(this.getClass().getSimpleName() + ": " + duration);
  	}
  	catch (Exception ex)
  	{
  		//exception during the layouting process.
  		//show error page, but still allow saving or other things
  		log(warning("Layouting failed", ex));
  		layout = ScoreLayout.createErrorLayout(context.getScore(), context.getSymbolPool());
  	}
    return layout;
  }
  
  
  /**
   * Recomputes the whole layout and returns it.
   * If something fails, an exception is thrown.
   */
  ScoreLayout createLayoutWithExceptions()
  {
  	return strategy.computeScoreLayout(context);
  }
  
  
  /**
   * Gets the score this layouter is working with.
   */
  public Score getScore()
  {
    return context.getScore();
  }
  
  
  /**
   * Gets the current layout.
   */
  public ScoreLayout getLayout()
  {
    return layout;
  }
  
  
  /**
   * Creates the strategy tree.
   * See "Dokumentation/Skizzen/Layoutengine-12-2008/Baum.odg"
   */
  ScoreLayoutStrategy createStrategyTree()
  {
  	//notation subtree
  	NotationStrategy notationStrategy = new NotationStrategy(
			new StemDirectionStrategy(),
			new NotesAlignmentStrategy(),
			new AccidentalsAlignmentStrategy(),
			new StemAlignmentStrategy(),
			new ArticulationsAlignmentStrategy());
  	//measure column subtree
  	ColumnSpacingStrategy measureColumnSpacingStrategy = new ColumnSpacingStrategy(
			new SeparateVoiceSpacingStrategy(),
			new MeasureElementsSpacingsStrategy(),
			new BeatOffsetsStrategy(),
			new BarlinesBeatOffsetsStrategy(),
			new BeatOffsetBasedVoiceSpacingStrategy(),
			new LeadingSpacingStrategy(
				notationStrategy));
  	//complete tree
  	return new ScoreLayoutStrategy(
  		notationStrategy,
  		new BeamedStemDirectionNotationsStrategy(
  			notationStrategy),
  		new VoiceStemDirectionNotationsStrategy(
  			notationStrategy),
  		measureColumnSpacingStrategy,
  		new FrameArrangementStrategy(
  			new SystemArrangementStrategy(
  				measureColumnSpacingStrategy)),
  		new BeamedStemAlignmentNotationsStrategy(),
  		new ScoreFrameLayoutStrategy(
  			new StaffStampingsStrategy(),
  			new MusicElementStampingStrategy(),
  			new BeamStampingStrategy(),
  			new CurvedLineStampingStrategy(),
  			new LyricStampingStrategy(),
  			new VoltaStampingStrategy(),
  			new DirectionStampingStrategy(),
  			new TupletStampingStrategy()));
  }
  
  
  
  /**
   * Gets the localized name of the given layouter strategy class, e.g.
   * "Empty staff lines over the whole page".
   * 
   * TIDY: move elsewhere
   */
  public static String getName(Class<?> strategyClass)
  {
  	String className = strategyClass.getName();
    if (className.startsWith(Zong.PACKAGE + "."))
      className = className.substring((Zong.PACKAGE + ".").length());
    return Lang.get(voc(className));
  }
  

}
