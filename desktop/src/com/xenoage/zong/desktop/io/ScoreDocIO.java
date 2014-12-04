package com.xenoage.zong.desktop.io;

import java.io.File;
import java.io.IOException;

import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * Reads and writes a {@link ScoreDoc} from or to the given {@link File}
 * using the given {@link FileInput} or {@link FileOutput}.
 * 
 * @author Andreas Wenger
 */
public class ScoreDocIO {
	
	public static ScoreDoc read(File file, FileInput<ScoreDoc> input)
		throws IOException {
		return input.read(new JseInputStream(file), file.getAbsolutePath());
	}
	
	public static void write(ScoreDoc doc, File file, FileOutput<ScoreDoc> output)
		throws IOException {
		output.write(doc, new JseOutputStream(file), file.getAbsolutePath());
	}

}
