package com.xenoage.zong.core.music.group;

import com.xenoage.utils.annotations.NonNull;

import lombok.Data;


/**
 * Group of staves with a common bracket.
 * 
 * @author Andreas Wenger
 */
@Data public final class BracketGroup {

	/** Visual styles of bracket grouping. */
	public enum Style {
		/** Curly brackets. */
		Brace,
		/** Square brackets. */
		Bracket;
	}

	/** The range of staves */
	@NonNull private StavesRange staves;
	/** The visual style of this group */
	@NonNull private Style style;

}
