package com.xenoage.zong.commands.core.music;

import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.document.exceptions.UselessException;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Staff;


/**
 * Adds staves at the end of a score.
 * The staves are filled with empty measures, according to the number of measures in the score.
 * 
 * @author Andreas Wenger
 */
@Untested public class StaffAdd
	implements Command {
	
	//data
	private Score score;
	private int stavesCount;
	

	/**
	 * Creates a {@link StaffAdd} command.
	 * @param score          the affected score
	 * @param stavesCount    the number of staves to add
	 */
	public StaffAdd(Score score, int stavesCount) {
		this.score = score;
		this.stavesCount = stavesCount;
	}


	@Override public void execute() {
		if (stavesCount < 1)
			throw new UselessException();
		for (int i = 0; i < stavesCount; i++) {
			Staff staff = Staff.staffMinimal();
			staff.setParent(score.getStavesList());
			staff.addEmptyMeasures(score.getMeasuresCount());
			score.getStavesList().getStaves().add(staff);
		}
	}
	

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		//remove the added staves
		for (int i = 0; i < stavesCount; i++) {
			score.getStavesList().getStaves().remove(score.getStavesCount() - 1);
		}
	}

}
