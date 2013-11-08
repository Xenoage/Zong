package com.xenoage.zong.commands.core.music;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.zong.core.music.Staff.staff;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;


/**
 * Adds a given part to a score.
 * 
 * The staves for the part are either given (the number of measures must
 * match the number of measures in the score) or they are created with
 * default settings and filled with empty measures, according to the number of measures in the score.
 * 
 * @author Andreas Wenger
 */
@Untested public class PartAdd
	implements Command {
	
	//data
	private Score score;
	private Part part;
	private int partIndex;
	@MaybeNull List<Staff> staves;
	//backup data
	private int staffStartIndex;
	
	
	public PartAdd(Score score, Part part, int partIndex, List<Staff> staves) {
		if (staves != null && staves.size() != part.getStavesCount())
			throw new IllegalArgumentException("number of staves is not equal");
		this.score = score;
		this.part = part;
		this.partIndex = partIndex;
		this.staves = staves;
	}


	@Override public void execute() {
		
		//prepare staves
		int measuresCount = score.getMeasuresCount();
		if (staves == null) {
			//staves are not given. create them with default settings
			staves = new ArrayList<Staff>(part.getStavesCount());
			for (int i = 0; i < part.getStavesCount(); i++) {
				Staff staff = staff(5, null);
				staff.setParent(score.getStavesList());
				staff.addEmptyMeasures(measuresCount); //fill with empty measures
				this.staves.add(staff);
			}
		}
		else {
			//check staves and measures
			for (int i : range(staves)) {
				if (staves.get(i).getMeasures().size() != measuresCount)
					throw new IllegalStateException("number of measures in score and staff " + i + " is not equal");
			}
		}
		
		//add part
		StavesList stavesList = score.getStavesList();
		stavesList.getParts().add(partIndex, part);
		
		//add staves
		staffStartIndex = 0;
		for (int iPart = 0; iPart < partIndex; iPart++)
			staffStartIndex += stavesList.getParts().get(iPart).getStavesCount();
		stavesList.getStaves().addAll(staffStartIndex, staves);
		
		//shift the staff indexes of the groups
		//beginning at the start index by the given number of staves
		for (BracketGroup group : stavesList.getBracketGroups())
			group.getStaves().insert(staffStartIndex, part.getStavesCount());
		for (BarlineGroup group : stavesList.getBarlineGroups())
			group.getStaves().insert(staffStartIndex, part.getStavesCount());
	}
	
	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		//remove part
		StavesList stavesList = score.getStavesList();
		stavesList.getParts().remove(partIndex);
		//remove staves
		for (int i : rangeReverse(staffStartIndex + part.getStavesCount(), staffStartIndex))
			stavesList.getStaves().remove(i);
		//shift staff indexes of the groups. we ignore the boolean return value of .remove
		//because the staves we remove can not cross group borders in this context
		for (BracketGroup group : stavesList.getBracketGroups())
			group.getStaves().remove(staffStartIndex, part.getStavesCount());
		for (BarlineGroup group : stavesList.getBarlineGroups())
			group.getStaves().remove(staffStartIndex, part.getStavesCount());
	}

}
