package com.xenoage.zong.commands.core.music.group;

import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.document.exceptions.UselessException;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;


/**
 * Adds a {@link BarlineGroup} to the score.
 * 
 * Since a staff may only have one barline group, existing barline groups
 * at the given positions are removed. Special case: If the given group is completely
 * within another group, a {@link UselessException} is thrown.
 * 
 * @author Andreas Wenger
 */
public class BarlineGroupAdd
	implements Command {

	//data
	private StavesList stavesList;
	private BarlineGroup group;
	//backup data
	private List<Command> backupCmds = null;


	public BarlineGroupAdd(StavesList stavesList, BarlineGroup group) {
		this.stavesList = stavesList;
		this.group = group;
	}


	@Override public void execute() {
		//check parameters
		int startStaff = group.getStaves().getStart();
		int endStaff = group.getStaves().getStop();
		if (endStaff >= stavesList.getStaves().size())
			throw new IllegalArgumentException("staves out of range");

		//if the given group is within an existing one, ignore the new group
		//(we do not support nested barline groups)
		List<BarlineGroup> groups = stavesList.getBarlineGroups();
		for (int i : range(groups)) {
			BarlineGroup group = groups.get(i);
			if (startStaff >= group.getStaves().getStart() && endStaff <= group.getStaves().getStop()) {
				throw new UselessException();
			}
		}

		//delete existing groups intersecting the given range
		for (int i : rangeReverse(groups)) {
			BarlineGroup group = groups.get(i);
			if (startStaff <= group.getStaves().getStop() && endStaff >= group.getStaves().getStart()) {
				executeAndRemember(new BarlineGroupRemove(stavesList, group));
			}
		}

		//add new group at the right position
		//(the barline groups are sorted by start index)
		int i = 0;
		while (i < groups.size() && startStaff > groups.get(i).getStaves().getStart())
			i++;
		groups.add(i, group);
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}


	@Override public void undo() {
		stavesList.getBarlineGroups().remove(group);
		if (backupCmds != null) {
			for (Command cmd : reverseIt(backupCmds))
				cmd.undo();
		}
	}


	private void executeAndRemember(Command cmd) {
		if (backupCmds == null)
			backupCmds = new ArrayList<Command>();
		cmd.execute();
		backupCmds.add(cmd);
	}

}
