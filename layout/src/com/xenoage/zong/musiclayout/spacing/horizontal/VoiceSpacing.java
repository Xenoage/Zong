package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.Voice;

/**
 * This class contains the spacing of one voice
 * of a measure of a single staff.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class VoiceSpacing {

	/** The {@link Voice} this spacing belongs to. */
	public final Voice voice;
	/** The interline space in mm of this voice. */
	public final float interlineSpace;
	/** The {@link SpacingElement}s of this voice. */
	public final SpacingElement[] spacingElements;
	
}
