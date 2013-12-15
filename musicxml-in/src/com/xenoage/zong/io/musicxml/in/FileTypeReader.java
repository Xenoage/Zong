package com.xenoage.zong.io.musicxml.in;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.xenoage.zong.io.musicxml.FileType;


/**
 * This class returns the {@link FileType} of MusicXML data
 * in a given inputstream.
 * 
 * @author Andreas Wenger
 */
public class FileTypeReader
{
	
	public static FileType getFileType(InputStream inputStream)
		throws IOException
	{
		//create buffered stream for reuse
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		bis.mark(2);
		//read first two characters. if "PK", we have a compressed MusicXML file.
		int bytes[] = new int[]{bis.read(), bis.read()};
		if (bytes[0] == 80 && bytes[1] == 75) //P, K
		{
			return FileType.Compressed;
		}
		bis.reset();
		//otherwise, try to parse as XML up to the root element (using StAX)
		try
		{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false); //don't resolve entities
			XMLEventReader eventReader = inputFactory.createXMLEventReader(bis);
			while (eventReader.hasNext())
			{
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement())
				{
					//document root element
					String name = event.asStartElement().getName().getLocalPart();
					if (name.equals("score-partwise"))
						return FileType.XMLScorePartwise;
					else if (name.equals("score-timewise"))
						return FileType.XMLScoreTimewise;
					else if (name.equals("opus"))
						return FileType.XMLOpus;
					break;
				}
			}
		}
		catch (XMLStreamException ex)
		{
			//unknown (no XML)
			return null;
		}
		//unknown
		return null;
	}

}
