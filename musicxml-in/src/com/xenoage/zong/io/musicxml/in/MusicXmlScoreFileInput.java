package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.io.IOException;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.io.musicxml.in.readers.LayoutFormatReader;
import com.xenoage.zong.io.musicxml.in.readers.ScoreFormatReader;
import com.xenoage.zong.io.musicxml.in.readers.ScoreInfoReader;
import com.xenoage.zong.io.musicxml.in.readers.ScoreReader;
import com.xenoage.zong.io.musicxml.in.readers.StavesListReader;
import com.xenoage.zong.io.musicxml.in.util.ErrorHandling;
import com.xenoage.zong.io.musicxml.in.util.ErrorHandling.Level;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.MxlDefaults;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;
import com.xenoage.zong.musicxml.types.groups.MxlScoreHeader;

/**
 * This class reads a MusicXML 2.0 file
 * into a instance of the {@link Score} class.
 * 
 * It uses the Zong! MusicXML Library.
 *
 * @author Andreas Wenger
 */
public class MusicXmlScoreFileInput
	implements FileInput<Score> {

	/**
	 * Creates a new MusicXML 2.0 reader.
	 */
	public MusicXmlScoreFileInput() {
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

		return read(score, Level.LogErrors);
	}

	/**
	 * Builds a {@link Score} entity from a {@link MxlScorePartwise} document.
	 * @param doc            the provided score-partwise document
	 * @param errorHandling  how to deal with errors
	 */
	public Score read(MxlScorePartwise mxlScore, Level errorHandling)
		throws InvalidFormatException {
		try {
			//create new score
			Score score = new Score();
	
			//read information about the score
			ScoreInfo scoreInfo = new ScoreInfoReader(mxlScore.getScoreHeader()).read();
			score.setInfo(scoreInfo);
	
			//read score format
			MxlScoreHeader mxlScoreHeader = mxlScore.getScoreHeader();
			MxlDefaults mxlDefaults = mxlScoreHeader.getDefaults();
			ScoreFormat scoreFormat = new ScoreFormatReader(mxlDefaults).read();
			score.setFormat(scoreFormat);
			
			//read layout format
			MxlLayout mxlLayout = (mxlDefaults != null ? mxlDefaults.getLayout() : null);
			LayoutFormat layoutFormat = new LayoutFormatReader(
				mxlLayout, scoreFormat.getInterlineSpace() / 10).read();
			score.setMetaData("layoutformat", layoutFormat); //TIDY
	
			//create the list of staves
			ErrorHandling mxlErrorHandling = new ErrorHandling(errorHandling);
			StavesListReader stavesListReader = new StavesListReader(mxlScore, mxlErrorHandling);
			StavesList stavesList = stavesListReader.read();
			stavesList.setScore(score);
			score.setStavesList(stavesList);
	
			//read the musical contents
			new ScoreReader(mxlScore).readToScore(score, mxlErrorHandling);
	
			//remember the XML document for further application-dependend processing
			score.setMetaData("mxldoc", mxlScore); //TIDY
	
			//when errors were collected, log them and save them as metadata
			if (mxlErrorHandling.getErrorMessages().size() > 0) {
				log(warning("The file could be loaded, but the following error(s) were reported: " +
					mxlErrorHandling.getErrorMessages()));
				score.setMetaData("mxlerrors", mxlErrorHandling.getErrorMessages());
			}
			
			return score;
		}
		catch (RuntimeException ex) {
			//catch runtime exceptions and rethrow them in the expected type
			throw new InvalidFormatException(ex);
		}
	}

}
