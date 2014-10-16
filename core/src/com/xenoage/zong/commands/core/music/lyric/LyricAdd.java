package com.xenoage.zong.commands.core.music.lyric;

import static com.xenoage.utils.collections.CollectionUtils.addOrNew;
import static com.xenoage.utils.collections.CollectionUtils.removeOrEmpty;
import lombok.AllArgsConstructor;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.lyric.Lyric;


/**
 * Adds the given {@link Lyric} to the given {@link Chord}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class LyricAdd
	implements Command {
	
	//data
	private Lyric lyric;
	private Chord chord;

	@Override public void execute() {
		chord.setLyrics(addOrNew(chord.getLyrics(), lyric));
	}

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	@Override public void undo() {
		chord.setLyrics(removeOrEmpty(chord.getLyrics(), lyric));
	}

}
