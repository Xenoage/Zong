package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;

import java.io.IOException;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.io.BufferedInputStream;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.xml.XmlException;
import com.xenoage.utils.xml.XmlReader;
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
		//create buffered stream for reuse
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		bis.mark();
		//read first two characters. if "PK", we have a compressed MusicXML file.
		int bytes[] = new int[] { bis.read(), bis.read() };
		if (bytes[0] == 80 && bytes[1] == 75) //P, K
		{
			return FileType.Compressed;
		}
		bis.reset();
		bis.unmark();
		//otherwise, try to parse as XML up to the root element (using StAX)
		try {
			XmlReader reader = platformUtils().createXmlReader(bis);
			if (reader.openNextChildElement()) {
				String n = reader.getElementName();
				if (n.equals("score-partwise"))
					return FileType.XMLScorePartwise;
				else if (n.equals("score-timewise"))
					return FileType.XMLScoreTimewise;
				else if (n.equals("opus"))
					return FileType.XMLOpus;
				reader.closeElement();
			}
		} catch (XmlException ex) {
			//unknown (no XML)
			return null;
		}
		//unknown
		return null;
	}

}
