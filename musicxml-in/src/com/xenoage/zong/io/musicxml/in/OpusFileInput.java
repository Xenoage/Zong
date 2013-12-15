package com.xenoage.zong.io.musicxml.in;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.base.Parser;
import com.xenoage.utils.base.exceptions.InvalidFormatException;
import com.xenoage.utils.io.IO;
import com.xenoage.utils.xml.XMLReader;
import com.xenoage.zong.io.musicxml.link.LinkAttributes;
import com.xenoage.zong.io.musicxml.opus.Opus;
import com.xenoage.zong.io.musicxml.opus.OpusItem;
import com.xenoage.zong.io.musicxml.opus.OpusLink;
import com.xenoage.zong.io.musicxml.opus.Score;


/**
 * This class reads a MusicXML 2.0 opus file.
 * 
 * @author Andreas Wenger
 */
public class OpusFileInput
{
	
	/**
   * Creates an {@link Opus} instance from the document
   * behind the given {@link InputStream}.
   */
  public Opus readOpusFile(InputStream inputStream)
    throws InvalidFormatException, IOException
  {
  	//parse XML file
  	Document doc;
  	try
  	{
  		doc = XMLReader.readFile(inputStream);
  	}
  	catch (Exception ex)
  	{
  		throw new IOException("Opus file does not exist or has invalid format", ex);
  	}
  	//interpret XML document
  	Element root = XMLReader.root(doc);
  	if (!root.getNodeName().equals("opus"))
  		throw new InvalidFormatException("No opus document");
  	return readOpus(root);
  }
  
  
  private Opus readOpus(Element eOpus)
  {
  	String title = XMLReader.elementText(eOpus, "title");
  	LinkedList<OpusItem> items = new LinkedList<OpusItem>();
  	for (Element e: XMLReader.elements(eOpus))
  	{
  		String name = e.getNodeName();
  		if (name.equals("opus"))
  			items.add(readOpus(e));
  		else if (name.equals("opus-link"))
  			items.add(readOpusLink(e));
  		else if (name.equals("score"))
  			items.add(readScore(e));
  	}
  	return new Opus(title, items);
  }
  
  
  private OpusLink readOpusLink(Element eOpusLink)
  {
  	String href = eOpusLink.getAttribute("xlink:href");
  	return new OpusLink(new LinkAttributes(href));
  }
  
  
  private Score readScore(Element eScore)
  {
  	String href = eScore.getAttribute("xlink:href");
  	Boolean newPage = Parser.parseBooleanNullYesNo(eScore.getAttribute("new-page"));
  	return new Score(new LinkAttributes(href), newPage);
  }


	/**
	 * Resolves all {@link OpusLink} items within the given {@link Opus} to
	 * instances of {@link Opus} and returns the result.
	 * Therefore, a base directory has to be given (compressed MusicXML files
	 * have to be extracted there) where the files are placed (or the empty string
	 * to use paths relative to the current directory).
	 */
	public Opus resolveOpusLinks(Opus opus, String baseDirectory)
		throws InvalidFormatException, IOException
	{
		LinkedList<OpusItem> resolvedItems = new LinkedList<OpusItem>();
		for (OpusItem item : opus.getItems())
		{
			OpusItem resolvedItem = item;
			if (item instanceof OpusLink)
			{
				Opus newOpus = readOpusFile(IO.openInputStreamPreservePath(
					baseDirectory + "/" + ((OpusLink) item).getHref()));
				resolvedItem = resolveOpusLinks(newOpus, baseDirectory);
			}
			resolvedItems.add(resolvedItem);
		}
		return new Opus(opus.getTitle(), resolvedItems);
	}

}
