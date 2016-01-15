package com.xenoage.zong.desktop.io.png.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.io.FilenameUtils.numberFiles;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.FilenameUtils;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.desktop.io.print.PngPrinter;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;

/**
 * This class writes one or more PNG files from a given {@link ScoreDoc}.
 * 
 * If the document has multiple pages, multiple files are created and
 * named according to {@link FilenameUtils#numberFiles(String, int)}.
 * 
 * @author Andreas Wenger
 */
public class PngScoreDocFileOutput
	extends FileOutput<ScoreDoc> {
	
	
	@Override public void write(ScoreDoc document, int fileIndex, OutputStream stream)
		throws IOException {
		Layout layout = document.getLayout();
		PngPrinter.print(layout, fileIndex, new JseOutputStream(stream));
	}

	@Override public List<String> getFileNames(ScoreDoc document, String fileName) {
		Layout layout = document.getLayout();
		if (layout.getPages().size() == 1) {
			//simple case: just one page
			return alist(fileName);
		}
		else {
			//more pages and file path is given: one PNG file for each page
			return numberFiles(fileName, layout.getPages().size());
		}
	}

	

}
