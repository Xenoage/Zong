package com.xenoage.utils.document.io;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.io.OutputStream;

/**
 * This is the interface for all classes that allow the creation of one or
 * more files from some document data.
 * 
 * There may be a MusicXML writer and a MIDI writer for example.
 *
 * @author Andreas Wenger
 */
public abstract class FileOutput<T extends Document> {

	/**
	 * Writes the given document to the given stream.
	 * @param document   the document to write
	 * @param fileIndex  the index of the output file. 0 for single file outputs.
	 *                   To find out the number of files, call {@link #getFileNames(Document, String)}.
	 * @param stream     the output stream for the given file
	 */
	public abstract void write(T document, int fileIndex, OutputStream stream)
		throws IOException;
	
	/**
	 * Gets the proposed file names for the given document,
	 * based on the given preferred file name.
	 * For writers, that generate only a single file, the
	 * returned name will be the same as the given one.
	 */
	public List<String> getFileNames(T document, String fileName) {
		return alist(fileName);
	}
}
