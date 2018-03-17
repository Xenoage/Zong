package com.xenoage.zong.core.music

import com.xenoage.zong.core.text.Text


/**
 * Interface for all musical elements containing a text.
 */
interface TextElement : MusicElement {

	/** The text of this element. */
	val text: Text?

}
