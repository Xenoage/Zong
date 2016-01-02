package com.xenoage.zong.musiclayout.spacing;

import static java.util.Collections.emptyList;

import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.notation.Notation;

/**
 * An element spacing stores the beat, the position (offset)
 * and the {@link Notation} of a given {@link MusicElement} in a layout.
 *
 * @author Andreas Wenger
 */
public abstract class ElementSpacing {

	
	/** The beat where this music element can be found in the measure. */
	public Fraction beat;
	
	/** The horizontal offset of the element in interline spaces, relative to the
	 * beginning of the measure. */
	public float xIs;
	
	/** The parent voice spacing. */
	public VoiceSpacing voice;
	
	/** Empty list of {@link ElementSpacing}s. */
	public static final List<ElementSpacing> empty = emptyList();
	

	public ElementSpacing(Fraction beat, float xIs) {
		this.beat = beat;
		this.xIs = xIs;
	}
	
	/**
	 * Gets the {@link Notation} associated with this spacing, or null.
	 */
	public Notation getNotation() {
		return null;
	}
	
	/**
	 * Gets the {@link MusicElement} associated with this spacing, or null.
	 */
	public MusicElement getElement() {
		Notation notation = getNotation();
		if (notation != null)
			return notation.getElement();
		else
			return null;
	}

	/**
	 * Gets the horizontal position in mm of the beginning of the parent voice
	 * relative to beginning of the parent staff.
	 */
	public float getVoiceXMm() {
		ColumnSpacing column = voice.measure.column;
		float measureXMm = column.parentSystem.getColumnXMm(column.measureIndex);
		float leadingMm = column.getLeadingWidthMm();
		return measureXMm + leadingMm;
	}

	/**
	 * Returns true, if this element is a grace chord or rest, otherwise false.
	 */
	public boolean isGrace() {
		MusicElement element = getElement();
		return (element instanceof VoiceElement &&
			((VoiceElement) element).getDuration().isGreater0() == false);
	}
	
}
