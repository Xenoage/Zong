package com.xenoage.zong.musiclayout.layouter;

import java.util.Collections;
import java.util.List;

import com.xenoage.utils.math.geom.Size2f;

import lombok.AllArgsConstructor;

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
	 * Creates a target for a complete layout with the given area size. 
	 */
	public static Target completeLayoutTarget(ScoreLayoutArea area) {
		return new Target(Collections.<ScoreLayoutArea>emptyList(), area, true);
	}
	
	/**
	 * Creates a target for a complete layout with A4 paper size (2 cm margins).
	 */
	public static Target targetA4() {
		float m = 20;
		Size2f s = new Size2f(210 - 2 * m, 297 - 2 * m);
		return completeLayoutTarget(new ScoreLayoutArea(s));
	}
	
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
