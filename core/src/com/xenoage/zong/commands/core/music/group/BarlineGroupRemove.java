package com.xenoage.zong.commands.core.music.group;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;


/**
 * Removes a {@link BarlineGroup}.
 * 
 * @author Andreas Wenger
 */
public class BarlineGroupRemove
	implements Command {
	
	//data
	private StavesList stavesList;
	private BarlineGroup group;
	//backup data
	private int groupIndex;
	
	
	public BarlineGroupRemove(StavesList stavesList, BarlineGroup group) {
		this.stavesList = stavesList;
		this.group = group;
	}
	
	
	
	@Override public void execute() {
		//remember element index, since the list is sorted
		groupIndex = stavesList.getBarlineGroups().indexOf(group);
		if (groupIndex == -1)
			throw new IllegalStateException("group is unknown");
		stavesList.getBarlineGroups().remove(group);
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		stavesList.getBarlineGroups().add(groupIndex, group);
	}

}
