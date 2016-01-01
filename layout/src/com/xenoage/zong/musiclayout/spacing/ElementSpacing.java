package com.xenoage.zong.musiclayout.spacing;

import java.util.Collections;
import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.notation.Notation;

/**
 * An element spacing stores the beat, the position (offset)
 * and the {@link Notation} of a given {@link MusicElement} in a layout.
 *
 * GOON: TIDY just interface, and two implementations: ChordSpacing and RestSpacing
 *
 * @author Andreas Wenger
 */
public class ElementSpacing {

	/** The corresponding element notation, e.g. a chord. May be null, e.g. when this
	 * element denotes the end point of the measure. */ //GOON: no! never null!
	@MaybeNull public final Notation notation;
	/** The beat where this music element can be found in the measure */
	@NonNull public final Fraction beat;
	/** The horizontal offset of the element in interline spaces, relative to the
	 * beginning of the measure. */
	public float xIs;
	
	/** The parent voice spacing. */
	public VoiceSpacing parent;
	
	/** Empty list of {@link ElementSpacing}s. */
	public static final List<ElementSpacing> empty = Collections.<ElementSpacing>emptyList();
	

	public ElementSpacing(Notation notation, Fraction beat, float offsetIs) {
		this.notation = notation;
		this.beat = beat;
		this.xIs = offsetIs;
	}

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
	
	/**
	 * Gets the horizontal position in mm of the beginning of the parent measure
	 * relative to beginning of the parent staff.
	 */
	public float getMeasureXMm() {
		ColumnSpacing column = parent.parent.parent;
		return column.parentSystem.getColumnXMm(column.measureIndex);
	}

}
