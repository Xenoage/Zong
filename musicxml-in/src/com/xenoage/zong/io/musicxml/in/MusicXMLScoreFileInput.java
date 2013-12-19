package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;

import java.io.IOException;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musicxml.in.readers.MusicReader;
import com.xenoage.zong.io.musicxml.in.readers.ScoreFormatReader;
import com.xenoage.zong.io.musicxml.in.readers.ScoreInfoReader;
import com.xenoage.zong.io.musicxml.in.readers.StavesListReader;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;

/**
 * This class reads a MusicXML 2.0 file
 * into a instance of the {@link Score} class.
 * 
 * It uses the Zong! MusicXML Library.
 *
 * @author Andreas Wenger
 */
public class MusicXMLScoreFileInput
	implements FileInput<Score> {

	/**
	 * Creates a new MusicXML 2.0 reader.
	 */
	public MusicXMLScoreFileInput() {
	}

	/**
	 * Creates a {@link Score} instance from the document
	 * behind the given {@link InputStream}. 
	 * @param inputStream  the input stream with the MusicXML document
	 * @param filePath     file path if known, null otherwise
	 */
	@Override public Score read(InputStream stream, @MaybeNull String filePath)
    throws InvalidFormatException, IOException {
		//parse MusicXML file
		MxlScorePartwise score;
		try {
			XmlReader xmlReader = platformUtils().createXmlReader(stream);
			MusicXMLDocument doc = MusicXMLDocument.read(xmlReader);
			score = doc.getScore();
		} catch (XmlDataException ex) {
			//no valid MusicXML
			throw new InvalidFormatException(ex);
		} catch (Exception ex) {
			throw new IOException(ex);
		}

		return read(score, true);
	}

	/**
	 * Builds a {@link Score} entity from a {@link MxlScorePartwise} document.
	 * @param doc           the provided score-partwise document
	 * @param ignoreErrors  if true, try to ignore errors (like overfull measures) as long
	 *                      as a consistent state can be guaranteed, or false, to cancel
	 *                      loading as soon as something is wrong
	 */
	public Score read(MxlScorePartwise mxlScore, boolean ignoreErrors)
		throws InvalidFormatException {
		//create new score
		Score score = new Score();

		//read information about the score
		score.setInfo(ScoreInfoReader.read(mxlScore));

		//read score format
		ScoreFormatReader.Value scoreFormatValue = ScoreFormatReader.read(mxlScore);
		score.setFormat(scoreFormatValue.scoreFormat);
		score.setMetaData("layoutformat", scoreFormatValue.layoutFormat);

		//create the list of staves
		StavesListReader.Value stavesListValue = StavesListReader.read(mxlScore);
		stavesListValue.stavesList.setScore(score);
		score.setStavesList(stavesListValue.stavesList);

		//read the musical contents
		MusicReader.read(mxlScore, score, ignoreErrors);

		//remember the XML document for further application-dependend processing
		score.setMetaData("mxldoc", mxlScore);

		return score;
	}

}
