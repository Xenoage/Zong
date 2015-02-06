package com.xenoage.zong.musiclayout.spacing.measure;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.Voice;

/**
 * This class contains the spacing of one voice
 * of a measure of a single staff.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public final class VoiceSpacing {

	/** The {@link Voice} this spacing belongs to. */
	public final Voice voice;
	/** The interline space in mm of this voice. */
	public final float interlineSpace;
	/** The {@link ElementSpacing}s of this voice. */
	public final List<ElementSpacing> spacingElements;
	
	
	public ElementSpacing getLast() {
		return spacingElements.get(spacingElements.size() - 1);
	}
	
}
