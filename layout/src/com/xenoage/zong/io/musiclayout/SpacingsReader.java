package com.xenoage.zong.io.musiclayout;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;
import com.xenoage.zong.musiclayout.settings.Spacings;

import java.io.IOException;

import static com.xenoage.zong.io.musiclayout.ChordSpacingsReader.readChordSpacings;
import static java.lang.Float.parseFloat;

/**
 * Desktop reader for {@link Spacings}.
 * 
 * @author Andreas Wenger
 */
public class SpacingsReader {

	/**
	 * Reads a {@link Spacings} from the given {@link XmlReader} at the "spacings" element.
	 */
	public static Spacings readSpacings(XmlReader r)
		throws IOException {
		ChordSpacings normalChordSpacings = null, graceChordSpacings = null;
		float widthSharp = 0, widthFlat = 0, widthClef = 0, widthMeasureEmpty = 0, widthDistanceMin = 0;

		while (r.openNextChildElement()) {
			String n = r.getElementName();
			switch (n) {
				case "chords":
					//chord spacings
					while (r.openNextChildElement()) {
						String n2 = r.getElementName();
						if (n2.equals("normal"))
							normalChordSpacings = readChordSpacings(r);
						else if (n2.equals("grace"))
							graceChordSpacings = readChordSpacings(r);
						r.closeElement();
					}
					break;
				case "clef":
					//clef
					widthClef = parseFloat(r.getAttributeNotNull("width"));
					break;
				case "key":
					//keys
					while (r.openNextChildElement()) {
						String n2 = r.getElementName();
						if (n2.equals("sharp"))
							widthSharp = parseFloat(r.getAttributeNotNull("width"));
						else if (n2.equals("flat"))
							widthFlat = parseFloat(r.getAttributeNotNull("width"));
						r.closeElement();
					}
					break;
				case "measure":
					//measure
					widthMeasureEmpty = parseFloat(r.getAttributeNotNull("empty"));
					break;
				case "distance":
					//distance
					widthDistanceMin = parseFloat(r.getAttributeNotNull("minimal"));
					break;
			}
			r.closeElement();
		}
		
		return new Spacings(normalChordSpacings, graceChordSpacings, widthSharp, widthFlat, widthClef,
			widthMeasureEmpty, widthDistanceMin);
	}
	
}
