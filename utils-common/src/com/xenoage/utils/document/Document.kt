package com.xenoage.utils.document

import com.xenoage.utils.document.command.CommandPerformer
import com.xenoage.utils.document.io.SupportedFormats

/**
 * Interface for a document.
 */
interface Document {

	/**
	 * Gets the [CommandPerformer], that can be used to execute,
	 * undo or redo commands on this document.
	 */
	val commandPerformer: CommandPerformer

	/**
	 * Gets the supported file formats for reading and writing documents of this type.
	 */
	val supportedFormats: SupportedFormats<out Document>?

}
