package com.xenoage.zong.desktop.io;

import static com.xenoage.utils.kernel.Range.range;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;

/**
 * Reads and writes a {@link Document} from or to the given {@link File}
 * using the given {@link FileInput} or {@link FileOutput}.
 * 
 * @author Andreas Wenger
 */
public class DocumentIO {
	
	public static <T extends Document> T read(File file, FileInput<T> input)
		throws IOException {
		return input.read(new JseInputStream(file), file.getAbsolutePath());
	}
	
	public static <T extends Document> void write(T doc, File file, FileOutput<T> output)
		throws IOException {
		List<String> fileNames = output.getFileNames(doc, file.getAbsolutePath());
		for (int iFile : range(fileNames)) {
			try (OutputStream stream = new JseOutputStream(fileNames.get(iFile))) {
				output.write(doc, iFile, stream);
			}
		}
	}

}
