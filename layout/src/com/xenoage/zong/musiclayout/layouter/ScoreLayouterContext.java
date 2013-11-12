package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.base.NullUtils.throwNullArg;
import static com.xenoage.zong.core.position.BMP.atStaff;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.score.ScoreController;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;


/**
 * This class can be used as a parameter in all {@link ScoreLayouterStrategy}
 * classes. It contains basic information about the layouting process,
 * like the {@link Score} to be layouted and the used {@link SymbolPool}.
 * 
 * @author Andreas Wenger
 */
public final class ScoreLayouterContext
{
	
	public final Score score;
	public final SymbolPool symbolPool;
	public final LayoutSettings layoutSettings;
	public final boolean isCompleteLayout;
	
	public final PVector<ScoreLayoutArea> areas;
	public final ScoreLayoutArea additionalArea;
	
	//cache
	private final float maxInterlineSpace;
	
	
	/**
	 * Creates a new {@link ScoreLayouterContext}.
	 * @param score             the score to layout
	 * @param symbolPool        the pool of musical symbols
	 * @param layoutSettings    general layout preferences
	 * @param isCompleteLayout  true to layout the whole score, false to layout
	 *                          only the frames of the score frame chain
	 * @param areas             information about the score frames in which to layout
	 * @param additionalArea    if the given areas are not enough, additional areas with
	 *                          this settings are used
	 */
	public ScoreLayouterContext(Score score, 
		SymbolPool symbolPool, LayoutSettings layoutSettings,
		boolean isCompleteLayout, PVector<ScoreLayoutArea> areas, ScoreLayoutArea additionalArea)
	{
		throwNullArg(score, areas, symbolPool, layoutSettings);
		this.score = score;
		this.symbolPool = symbolPool;
		this.layoutSettings = layoutSettings;
		this.isCompleteLayout = isCompleteLayout;
		this.areas = areas;
		this.additionalArea = additionalArea;
		//cache
		float maxInterlineSpace = 0;
		for (int staff = 0; staff < score.getStavesCount(); staff++)
		{
			maxInterlineSpace = Math.max(maxInterlineSpace,
				ScoreController.getInterlineSpace(score, atStaff(staff)));
		}
		this.maxInterlineSpace = maxInterlineSpace;
	}
	
	
	/**
	 * Gets the score which is layouted.
	 */
	public Score getScore()
	{
		return score;
	}
	
	
	/**
	 * Gets the used symbol style.
	 */
	public SymbolPool getSymbolPool()
	{
		return symbolPool;
	}
	
	
	/**
	 * Gets the biggest interline space of the score.
	 */
	public float getMaxInterlineSpace()
	{
		return maxInterlineSpace;
	}
	
	
	/**
	 * Gets a {@link ScoreLayoutArea} for the area with the given index.
	 */
	public ScoreLayoutArea getArea(int index)
	{
		if (index < areas.size())
			return areas.get(index);
		else
			return additionalArea;
	}
	

}
