package com.xenoage.zong.musiclayout.spacing;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.Score;

/**
 * The {@link ColumnSpacing}s of a {@link Score}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class ScoreSpacing {
	
	public Score score;
	public List<ColumnSpacing> columns;
	public List<FrameSpacing> frames;

}
