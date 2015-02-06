package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.notations.Notation;

/**
 * An element spacing stores the beat and the position (offset)
 * of a given {@link MusicElement} in a layout.
 * 
 * All units are measured in interline spaces.
 *
 * @author Andreas Wenger
 */
@Data @AllArgsConstructor 
public class ElementSpacing {

	/** The corresponding element notation, e.g. a chord. May be null, e.g. when this
	 * element denotes the end point of the measure. */
	@MaybeNull public final Notation notation;
	/** The beat where this music element can be found in the measure */
	@NonNull public final Fraction beat;
	/** The horizontal offset of the element in interline spaces */
	public float offsetIs;
	
	/**
	 * Returns the MusicElement, or null, if there is none.
	 */
	public MusicElement getElement() {
		if (notation == null)
			return null;
		return notation.getElement();
	}
	
	/**
	 * Returns true, if this element is a grace chord, otherwise false.
	 */
	public boolean isGrace() {
		MusicElement element = getElement();
		return (element != null && element instanceof VoiceElement &&
			((VoiceElement) element).getDuration().isGreater0() == false);
	}


	@Deprecated //this class is mutable
	public ElementSpacing withOffset(float offset) {
		return new ElementSpacing(notation, beat, offset);
	}

}
