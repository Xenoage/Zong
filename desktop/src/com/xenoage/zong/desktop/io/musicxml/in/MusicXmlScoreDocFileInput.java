package com.xenoage.zong.desktop.io.musicxml.in;

import static com.xenoage.utils.jse.async.Sync.sync;

import java.io.IOException;

import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.io.InputStream;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreDocFileReader;

/**
 * This class reads a MusicXML 2.0 file
 * into a instance of the {@link ScoreDoc} class.
 *
 * @author Andreas Wenger
 */
public class MusicXmlScoreDocFileInput
	implements FileInput<ScoreDoc> {

	/**
	 * See {@link MusicXmlScoreDocFileReader}.
	 */
	@Override public ScoreDoc read(InputStream stream, String filePath)
		throws InvalidFormatException, IOException {
		try {
			return sync(new MusicXmlScoreDocFileReader(stream, filePath));
		} catch (InvalidFormatException ex) {
			throw ex; //forward
		} catch (IOException ex) {
			throw ex; //forward
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

	

}
