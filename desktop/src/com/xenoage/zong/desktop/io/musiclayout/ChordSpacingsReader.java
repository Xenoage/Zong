package com.xenoage.zong.desktop.io.musiclayout;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLReader;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;

/**
 * Desktop reader for {@link ChordSpacings}.
 * 
 * @author Andreas Wenger
 */
public class ChordSpacingsReader {

	/**
	 * Reads the {@link ChordSpacings} from the given XML element.
	 */
	public static ChordSpacings readChordSpacings(Element e)
		throws IOException {
		HashMap<Fraction, Float> durationWidths = map();

		//load the duration-to-width mapping
		List<Element> listChords = XMLReader.elements(e, "chord");
		for (Element el : listChords) {
			//duration format: x/y, e.g. "1/4"
			Fraction duration = Fraction.fromString(XMLReader.attributeNotNull(el, "duration"));
			//width format: x+y/z, eg. "3+1/2"
			float width = Fraction.fromString(XMLReader.attributeNotNull(el, "width")).toFloat();
			durationWidths.put(duration, width);
		}

		return new ChordSpacings(durationWidths);
	}

}
