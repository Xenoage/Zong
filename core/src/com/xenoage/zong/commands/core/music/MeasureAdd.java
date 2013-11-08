package com.xenoage.zong.commands.core.music;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.document.exceptions.UselessException;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Staff;


/**
 * Adds empty measures at the end of a score.
 * 
 * @author Andreas Wenger
 */
@Untested public class MeasureAdd
	implements Command {
	
	//data
	private Score score;
	private int measuresCount;
	

	/**
	 * Creates a {@link MeasureAdd} command.
	 * @param score          the affected score
	 * @param measuresCount  the number of measures to add
	 */
	public MeasureAdd(Score score, int measuresCount) {
		this.score = score;
		this.measuresCount = measuresCount;
	}


	@Override public void execute() {
		if (measuresCount < 1)
			throw new UselessException();
		//column headers
		score.getHeader().addEmptyMeasures(measuresCount);
		//staves
		for (Staff staff : score.getStavesList().getStaves()) {
			staff.addEmptyMeasures(measuresCount);
		}
	}
	

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		//remove the added measures.
		//this is trivial, since they still have no content in this state.
		int oldSize = score.getMeasuresCount();
		for (int i : range(measuresCount)) {
			int measureIndex = oldSize - i - 1;
			//column header
			score.getHeader().getColumnHeaders().remove(measureIndex);
			//staves
			for (Staff staff : score.getStavesList().getStaves()) {
				staff.getMeasures().remove(measureIndex);
			}
		}
	}

}
