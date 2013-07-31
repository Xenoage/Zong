package com.xenoage.zong.commands.core.music;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;


/**
 * Writes the given {@link MeasureElement} at the given beat
 * into the given measure (not into the whole measure column).
 * Dependent on its type, it may replace elements of the same type.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementWrite
	implements Command {
	
	//data
	private MeasureElement element;
	private Measure measure;
	private Fraction beat;
	//backup data
	private MeasureElement replacedElement = null;
	

	public MeasureElementWrite(MeasureElement element, Measure measure, Fraction beat) {
		this.element = element;
		this.measure = measure;
		this.beat = beat;
	}


	@Override public void execute() {
		replacedElement = measure.addMeasureElement(element, beat);
	}


	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}


	@Override public void undo() {
		if (replacedElement != null)
			measure.addMeasureElement(replacedElement, beat);
	}

}
