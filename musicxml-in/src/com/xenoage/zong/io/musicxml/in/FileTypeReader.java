package com.xenoage.zong.io.musicxml.in;

import java.io.IOException;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.io.InputStream;
import com.xenoage.zong.io.musicxml.FileType;

/**
 * This class returns the {@link FileType} of MusicXML data
 * in a given inputstream.
 * 
 * @author Andreas Wenger
 */
public class FileTypeReader {

	@MaybeNull public static FileType getFileType(InputStream inputStream)
		throws IOException {
		//read first two characters. if "PK", we have a compressed MusicXML file.
		int bytes[] = new int[] { inputStream.read(), inputStream.read() };
		if (bytes[0] == 80 && bytes[1] == 75) //P, K
		{
			return FileType.Compressed;
		}
		bis.reset();
		//otherwise, try to parse as XML up to the root element (using StAX)
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false); //don't resolve entities
			XMLEventReader eventReader = inputFactory.createXMLEventReader(bis);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
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
		} catch (XMLStreamException ex) {
			//unknown (no XML)
			return null;
		}
		//unknown
		return null;
	}

}
