package com.xenoage.zong.desktop.io.png.out;

import static com.xenoage.utils.io.FilenameUtils.numberFiles;
import static com.xenoage.utils.kernel.Range.range;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import lombok.Setter;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.FilenameUtils;
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
	implements FileOutput<ScoreDoc> {
	
	@Setter private boolean justOnePage = false;


	@Override public void write(ScoreDoc document, com.xenoage.utils.io.OutputStream stream,
		String filePath)
		throws IOException {
		Layout layout = document.getLayout();
		if (justOnePage || layout.getPages().size() == 1 || filePath == null) {
			//simple case: just one page
			PngPrinter.print(layout, 0, new JseOutputStream(stream));
		}
		else {
			//more pages and file path is given: one PNG file for each page
			List<String> filenames = numberFiles(filePath, layout.getPages().size());
			for (int page : range(layout.getPages()))
				PngPrinter.print(document.getLayout(), page, new FileOutputStream(filenames.get(page)));
		}
	}

	/**
	 * Returns true, since multiple files may be required.
	 */
	@Override public boolean isFilePathRequired(ScoreDoc document) {
		return true;
	}

}
