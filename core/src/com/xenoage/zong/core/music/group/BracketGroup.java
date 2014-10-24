package com.xenoage.zong.core.music.group;

import com.xenoage.utils.annotations.NonNull;

import lombok.Data;


/**
 * Group of staves with a common bracket.
 * 
 * See the <a href="http://www.finalemusic.com/UserManuals/MusicXML/Content/ST-MusicXML-group-symbol-value.htm">MusicXML manual</a>
 * for example images of the styles.
 * 
 * @author Andreas Wenger
 */
@Data public final class BracketGroup {

	/** Visual styles of bracket grouping. */
	public enum Style {
		/** No grouping. Use this value instead of null. */
		None,
		/** Curly brackets. */
		Brace,
		/** Square brackets. */
		Bracket,
		/** Line brackets. Like {@link #Bracket}, but without the caps at the end. */
		Line,
		/** Square brackets. A thin rectangle. */
		Square;
	}

	/** The range of staves */
	@NonNull private StavesRange staves;
	/** The visual style of this group */
	@NonNull private Style style;

}
