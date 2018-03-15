package com.xenoage.zong.core.music.group

import lombok.Data
import lombok.NonNull


/**
 * Group of staves with a common bracket.
 *
 * See the [MusicXML manual](http://www.finalemusic.com/UserManuals/MusicXML/Content/ST-MusicXML-group-symbol-value.htm)
 * for example images of the styles.
 */
class BracketGroup(
	/** The range of staves. */
	var staves: StavesRange,
	/** The visual style of this group. */
	var style: Style
) {

	/** Visual styles of bracket grouping. */
	enum class Style {
		/** No grouping. */
		None,
		/** Curly brackets. */
		Brace,
		/** Square brackets. */
		Bracket,
		/** Line brackets. Like [Bracket], but without the caps at the end. */
		Line,
		/** Square brackets. A thin rectangle. */
		Square
	}

}
