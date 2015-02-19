package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import lombok.Getter;

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
public abstract class TextStamping
	extends Stamping {

	/** The text element. */
	@NonNull @Getter FormattedText text;
	
	/** The element this stamping belongs to. */
	@Getter MusicElement element;


	public TextStamping(@NonNull FormattedText text, @MaybeNull MusicElement element,
		@MaybeNull StaffStamping parentStaff, @MaybeNull Shape boundingShape) {
		super(parentStaff, boundingShape);
		this.text = checkNotNull(text);
	}

	@Override public StampingType getType() {
		return StampingType.TextStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Text;
	}

	/**
	 * Gets the position within the frame in mm.
	 */
	public abstract Point2f getPositionInFrame();

}
