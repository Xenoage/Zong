package com.xenoage.zong.commands.core.music.group;

import java.util.List;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;


/**
 * Adds a {@link BarlineGroup} to the score.
 * 
 * @author Andreas Wenger
 */
public class BracketGroupAdd
	implements Command {

	//data
	private StavesList stavesList;
	private BracketGroup group;


	public BracketGroupAdd(StavesList stavesList, BracketGroup group) {
		this.stavesList = stavesList;
		this.group = group;
	}


	@Override public void execute() {
		//check parameters
		int startStaff = group.getStaves().getStart();
		int endStaff = group.getStaves().getStop();
		if (endStaff >= stavesList.getStaves().size())
			throw new IllegalArgumentException("staves out of range");

		//add new group at the right position
		//(the barline groups are sorted by start index)
		List<BracketGroup> groups = stavesList.getBracketGroups();
		int i = 0;
		while (i < groups.size() && startStaff > groups.get(i).getStaves().getStart())
			i++;
		groups.add(i, group);
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}


	@Override public void undo() {
		stavesList.getBracketGroups().remove(group);
	}

}
