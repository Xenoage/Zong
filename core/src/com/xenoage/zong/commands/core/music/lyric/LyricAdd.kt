package com.xenoage.zong.commands.core.music.lyric

import com.xenoage.utils.collections.addOrNew
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.collections.removeOrEmpty
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.lyric.Lyric


/**
 * Adds the given [Lyric] to the given [Chord].
 */
class LyricAdd(
		private val lyric: Lyric,
		private val chord: Chord
) : UndoableCommand() {

	override fun execute() {
		chord.lyrics = chord.lyrics.addOrNew(lyric)
	}

	override fun undo() {
		chord.lyrics = chord.lyrics.removeOrEmpty(lyric)
	}

}
