package com.xenoage.utils.document.command;

/**
 * Levels of support for undoing and redoing commands.
 * 
 * @author Andreas Wenger
 */
public enum Undoability {
	/** Undoable and redoable command. For example, chaning the format of a text within the document
	 * would be a good candidate for an undoable command. */
	Undoable,
	/** Non-undoable command, which does not break the chain of previously executed commands. For example,
	 * a zoom command does not affect the content of a document. */
	Transparent,
	/** Non-undoable command, which breaks the chain of previously executed commands. For example,
	 * when a document was closed, this can not be undone, and previous operations on this document can also not be
	 * undone any more. */
	Stopper;
}
