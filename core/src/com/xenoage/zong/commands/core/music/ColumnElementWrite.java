package com.xenoage.zong.commands.core.music;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureSide;


/**
 * Writes the given {@link ColumnElement} at the given beat and measure side in the given
 * {@link ColumnHeader}.
 * 
 * The beat and side is not needed for some {@link ColumnElement}s.
 * See {@link ColumnHeader#setColumnElement(ColumnElement, Fraction, MeasureSide)}.
 * 
 * @author Andreas Wenger
 */
public class ColumnElementWrite
	implements Command {
	
	//data
	private ColumnElement element;
	private ColumnHeader column;
	@MaybeNull private Fraction beat;
	@MaybeNull private MeasureSide side; //TODO: get rid of the side. place it in the barline class itself
	//backup data
	private ColumnElement replacedElement = null;
	

	public ColumnElementWrite(ColumnElement element, ColumnHeader column,
		@MaybeNull Fraction beat, @MaybeNull MeasureSide side) {
		this.column = column;
		this.element = element;
		this.beat = beat;
		this.side = side;
	}
	

	@Override public void execute() {
		replacedElement = column.setColumnElement(element, beat, side);
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}
	
	
	@Override public void undo() {
  	column.removeColumnElement(element);
  	if (replacedElement != null)
  		column.setColumnElement(replacedElement, beat, side);
  }

}
