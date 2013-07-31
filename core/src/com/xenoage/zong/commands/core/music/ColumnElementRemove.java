package com.xenoage.zong.commands.core.music;

import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.Undoability;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureSide;


/**
 * Removes the given {@link ColumnElement} from the given {@link ColumnHeader}.
 * 
 * @author Andreas Wenger
 */
public class ColumnElementRemove
	implements Command {
	
	//data
	private ColumnHeader column;
	private ColumnElement element;
	//backup data
	private Fraction beat;
	private MeasureSide side;
	

	public ColumnElementRemove(ColumnHeader column, ColumnElement element) {
		this.column = column;
		this.element = element;
	}
	

	@Override public void execute() {
		beat = column.getMP(element).beat;
		side = column.getSide(element);
		column.removeColumnElement(element);
	}

	
	@Override public Undoability getUndoability() {
		return Undoability.Undoable;
	}
	
	
	@Override public void undo() {
  	column.setColumnElement(element, beat, side);
  }

}
