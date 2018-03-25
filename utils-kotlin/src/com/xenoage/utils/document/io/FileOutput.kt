package com.xenoage.utils.document.io

import com.xenoage.utils.document.Document
import com.xenoage.utils.io.OutputStream
import com.xenoage.utils.io.IoException

/**
 * This is the interface for all classes for the creation of one or
 * more files from some document data.
 *
 * There may be a MusicXML writer and a MIDI writer for example.
 */
abstract class FileOutput<T : Document> {

	/**
	 * Writes the given document to the given stream.
	 * @param document   the document to write
	 * @param fileIndex  the index of the output file. 0 for single file outputs.
	 * To find out the number of files, call [getFileNames].
	 * @param stream     the output stream for the given file
	 * @throws IoException
	 */
	abstract fun write(document: T, fileIndex: Int, stream: OutputStream)

	/**
	 * Gets the proposed file names for the given document,
	 * based on the given preferred file name.
	 * For writers, that generate only a single file, the
	 * returned name will be the same as the given one.
	 */
	open fun getFileNames(document: T, fileName: String) = listOf(fileName)

}
