package com.xenoage.zong.io.musicxml.in;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;

import com.xenoage.utils.base.exceptions.InvalidFormatException;
import com.xenoage.utils.xml.InvalidXMLData;
import com.xenoage.utils.xml.XMLReader;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.ScoreFileInput;
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
  implements ScoreFileInput
{
	
	
	/**
	 * Creates a new MusicXML 2.0 reader.
	 */
	public MusicXMLScoreFileInput()
	{
	}
  
  
  /**
   * Creates a {@link Score} instance from the document
   * behind the given {@link InputStream}. 
   * @param inputStream  the input stream with the MusicXML document
   * @param filePath     file path if known, null otherwise
   */
  @Override public Score read(InputStream inputStream, String filePath)
    throws InvalidFormatException, IOException
  {
  	
  	//parse MusicXML file
  	MxlScorePartwise score;
  	try
  	{
  		Document xmlDoc = XMLReader.readFile(inputStream);
  		MusicXMLDocument doc = MusicXMLDocument.read(xmlDoc);
  		score = doc.getScore();
  	}
  	catch (InvalidXMLData ex)
  	{
  		//no valid MusicXML
  		throw new InvalidFormatException(ex);
  	}
  	catch (Exception ex)
  	{
  		throw new IOException(ex);
  	}
  	
  	return read(score, true);
  }
  
  
  /**
   * Builds a {@link Score} entity from a {@link MxlScorePartwise} document.
   * @param doc           the provided ScorePartwise document
   * @param ignoreErrors  if true, try to ignore errors (like overfull measures) as long
   *                      as a consistent state can be guaranteed, or false, to cancel
   *                      loading as soon as something is wrong
   */
  public Score read(MxlScorePartwise mxlScore, boolean ignoreErrors)
  	throws InvalidFormatException
  {
		//create new score
		Score score = Score.empty;
		
		//read information about the score
		score = score.withScoreInfo(ScoreInfoReader.read(mxlScore));

		//read score format
		ScoreFormatReader.Value scoreFormatValue = ScoreFormatReader.read(mxlScore);
		score = score.withFormat(scoreFormatValue.scoreFormat);
		score = score.plusMetaData("layoutformat", scoreFormatValue.layoutFormat);

		//create the list of staves
		StavesListReader.Value stavesListValue = StavesListReader.read(mxlScore);
		score = score.withStavesList(stavesListValue.stavesList);

		//read the musical contents
		score = MusicReader.read(mxlScore, score, ignoreErrors);

		//remember the XML document for further application-dependend processing
		score = score.plusMetaData("mxldoc", mxlScore);

		return score;
  }
  

}
