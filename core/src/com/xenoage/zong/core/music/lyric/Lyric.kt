package com.xenoage.zong.core.music.lyric

import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.TextElement
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.lyric.SyllableType.Extend
import com.xenoage.zong.core.text.Text


/**
 * This class represents a single syllable of lyrics.
 */
class Lyric(
		/** The text. Usually a single syllable. Null, if type is [Extend]. */
		override val text: Text?,
		/** The position of the syllable within the lyrics.  */
		val syllableType: SyllableType,
		/** The verse number. 0 is the first one.  */
		val verse: Int = 0
) : TextElement {

	/** Back reference: The parent element, or null, if not attached to an element.  */
	var parent: VoiceElement? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Lyric

	override fun toString() = "Lyric (${if (text != null) "\"$text\"" else "extend"})"

	companion object {

		/** Creates a new lyric. */
		operator fun invoke(text: Text?, syllableType: SyllableType, verse: Int): Lyric {
			check(syllableType != Extend || text != null, { "text must be null for extend"})
			return Lyric(text, syllableType, verse)
		}

		/** Creates a new extend lyric. */
		fun lyricExtend(verse: Int) = Lyric(null, Extend, verse)

	}

}
