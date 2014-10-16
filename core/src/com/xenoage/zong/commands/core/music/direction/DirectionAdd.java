package com.xenoage.zong.commands.core.music.direction;

import static com.xenoage.utils.collections.CollectionUtils.addOrNew;
import lombok.AllArgsConstructor;

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
		chord.setDirections(addOrNew(chord.getDirections(), direction));
	}

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	@Override public void undo() {
		chord.setDirections(addOrNew(chord.getDirections(), direction));
	}

}
