package com.xenoage.zong.commands.core.music.group;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BracketGroup;


/**
 * Removes a {@link BracketGroup}.
 * 
 * @author Andreas Wenger
 */
public class BracketGroupRemove
	implements Command {
	
	//data
	private StavesList stavesList;
	private BracketGroup group;
	//backup data
	private int groupIndex;
	
	
	public BracketGroupRemove(StavesList stavesList, BracketGroup group) {
		this.stavesList = stavesList;
		this.group = group;
	}
	
	
	@Override public void execute() {
		//remember element index, since the list is sorted
		groupIndex = stavesList.getBracketGroups().indexOf(group);
		if (groupIndex == -1)
			throw new IllegalStateException("group is unknown");
		stavesList.getBracketGroups().remove(group);
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		stavesList.getBracketGroups().add(groupIndex, group);
	}

}
