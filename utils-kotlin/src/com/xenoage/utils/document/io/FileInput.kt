package com.xenoage.utils.document.io

import com.xenoage.utils.document.Document
import com.xenoage.utils.io.InputStream
import com.xenoage.utils.io.InvalidFormatException
import com.xenoage.utils.io.IoException

/**
 * This is the interface for all classes for the creation
 * of some document data from a file.
 *
 * There may be a MusicXML reader and a MIDI reader for example.
 */
interface FileInput<T : Document> {

	/**
	 * Creates document data from the document behind the given input stream.
	 * If the file path is known too, it can be given, otherwise it is null.
	 * @throws InvalidFormatException
	 * @throws IoException
	 */
	fun read(stream: InputStream, filePath: String?): T


}
