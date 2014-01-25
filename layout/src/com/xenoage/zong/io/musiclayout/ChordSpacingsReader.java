package com.xenoage.zong.io.musiclayout;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.io.IOException;
import java.util.HashMap;

import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;

/**
 * Desktop reader for {@link ChordSpacings}.
 * 
 * @author Andreas Wenger
 */
public class ChordSpacingsReader {

	/**
	 * Reads the {@link ChordSpacings} from the given {@link XmlReader} at a child element
	 * of the "chords" element.
	 */
	public static ChordSpacings readChordSpacings(XmlReader r)
		throws IOException {
		HashMap<Fraction, Float> durationWidths = map();

		//load the duration-to-width mapping
		while (r.openNextChildElement()) {
			if (r.getElementName().equals("chord")) {
				//duration format: x/y, e.g. "1/4"
				Fraction duration = Fraction.fromString(r.getAttributeNotNull("duration"));
				//width format: x+y/z, eg. "3+1/2"
				float width = Fraction.fromString(r.getAttributeNotNull("width")).toFloat();
				durationWidths.put(duration, width);
			}
			r.closeElement();
		}

		return new ChordSpacings(durationWidths);
	}

}
