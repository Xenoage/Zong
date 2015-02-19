package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Base class for all text stampings.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public abstract class TextStamping
	extends Stamping {

	
	public abstract FormattedText getText();
	
	/**
	 * Gets the position of this stamping within the frame in mm.
	 */
	public abstract Point2f getPositionMm();

	@Override public StampingType getType() {
		return StampingType.TextStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Text;
	}

}
