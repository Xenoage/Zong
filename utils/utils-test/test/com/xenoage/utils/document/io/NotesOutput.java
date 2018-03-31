package com.xenoage.utils.document.io;

import java.io.IOException;
import java.io.PrintWriter;

import com.xenoage.utils.document.Notes;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;

/**
 * This class writes {@link Notes} to a text file.
 * 
 * @author Andreas Wenger
 */
public class NotesOutput
	extends FileOutput<Notes> {

	@Override public void write(Notes document, int fileIndex, OutputStream stream)
		throws IOException {
		PrintWriter writer = new PrintWriter(new JseOutputStream(stream));
		for (String line : document.getLines())
			writer.println(line);
		writer.flush();
		writer.close();
	}

}
