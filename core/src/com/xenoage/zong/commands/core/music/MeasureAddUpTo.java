package com.xenoage.zong.commands.core.music;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.document.exceptions.UselessException;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Staff;


/**
 * Adds empty measures to the score until the score has the given
 * number of measures.
 * 
 * @author Andreas Wenger
 */
@Untested public class MeasureAddUpTo
	implements Command {
	
	//data
	private Score score;
	private int measuresCount;
	
	//backup
	private int measuresToAdd = 0;
	

	/**
	 * Creates a {@link MeasureAddUpTo} command.
	 * @param score          the affected score
	 * @param measuresCount  the total number of measures for the score
	 */
	public MeasureAddUpTo(Score score, int measuresCount) {
		this.score = score;
		this.measuresCount = measuresCount;
	}


	@Override public void execute() {
		measuresToAdd = measuresCount - score.getMeasuresCount();
		if (measuresToAdd <= 0)
			throw new UselessException();
		//column headers
		score.getHeader().addEmptyMeasures(measuresToAdd);
		//staves
		for (Staff staff : score.getStavesList().getStaves()) {
			staff.addEmptyMeasures(measuresToAdd);
		}
	}
	

	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}

	
	@Override public void undo() {
		//remove the added measures.
		//this is trivial, since they still have no content in this state.
		int oldSize = score.getMeasuresCount();
		for (int i : range(measuresToAdd)) {
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
