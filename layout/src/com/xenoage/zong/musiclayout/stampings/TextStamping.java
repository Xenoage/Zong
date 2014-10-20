package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Base class for all text stampings.
 *
 * @author Andreas Wenger
 */
@Const public abstract class TextStamping
	extends Stamping {

	/** The formatted text. */
	@NonNull @Getter protected final FormattedText text;


	public TextStamping(@NonNull FormattedText text, @MaybeNull StaffStamping parentStaff,
		@MaybeNull MusicElement musicElement, @MaybeNull Shape boundingShape) {
		super(parentStaff, Level.Text, musicElement, boundingShape);
		this.text = checkNotNull(text);
	}

	@Override public StampingType getType() {
		return StampingType.TextStamping;
	}

	/**
	 * Gets the position within the frame in mm.
	 */
	public abstract Point2f getPositionInFrame();

}
