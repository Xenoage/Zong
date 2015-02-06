package com.xenoage.zong.musiclayout;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;

/**
 * Target frames for a score layout.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class Target {
	
	/** Information about the score frames in which to layout. */
	public List<ScoreLayoutArea> areas;
	/** If the given areas are not enough, additional areas with this settings are used. */
	public ScoreLayoutArea additionalArea;
	/** True to layout the whole score, false to layout
	 * only the given areas. */
	public boolean isCompleteLayout;
	
	
	/**
	 * Gets a {@link ScoreLayoutArea} for the area with the given index.
	 * If it is an area after the last explicitly known area, the additional area
	 * is returned.
	 */
	public ScoreLayoutArea getArea(int index) {
		if (index < areas.size())
			return areas.get(index);
		else
			return additionalArea;
	}
	
}
