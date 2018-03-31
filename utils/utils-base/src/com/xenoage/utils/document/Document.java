package com.xenoage.utils.document;

import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.document.io.SupportedFormats;

/**
 * Interface for a document.
 * 
 * @author Andreas Wenger
 */
public interface Document {

	/**
	 * Gets the {@link CommandPerformer}, that can be used to execute,
	 * undo or redo commands on this document.
	 */
	public CommandPerformer getCommandPerformer();

	/**
	 * Gets the supported file formats for reading and writing documents of this type.
	 */
	public SupportedFormats<? extends Document> getSupportedFormats();

}
