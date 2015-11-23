package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.musiclayout.notation.Notation;

/**
 * Class for a text stamping positioned within a frame, e.g. a part name.
 * 
 * If the text belongs to a certain staff, use the
 * {@link StaffTextStamping} class instead.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public class FrameTextStamping
	extends TextStamping {

	/** The text element. */
	@NonNull public final FormattedText text;
	/** The position in Mm, relative to the top left corner of the score frame. */
	@NonNull public final Point2f positionMm;
	/** The notation this stamping belongs to. */
	@MaybeNull public final Notation element;


	@Override public Shape getBoundingShape() {
		return text.getBoundingRect().move(positionMm);
	}

}
