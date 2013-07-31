package com.xenoage.zong.commands.core.music.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.base.collections.NullableList;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;


/**
 * Adds the given {@link Direction} to the given {@link Chord}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class DirectionAdd
	implements Command {
	
	//data
	private Direction direction;
	private Chord chord;

	@Override public void execute() {
		chord.setDirections(NullableList.add(chord.getDirections(), direction));
	}

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	@Override public void undo() {
		chord.setDirections(NullableList.remove(chord.getDirections(), direction));
	}

}
