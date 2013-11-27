package com.xenoage.zong.desktop.io.musiclayout;

import static com.xenoage.utils.jse.xml.XMLReader.attributeNotNull;
import static com.xenoage.utils.jse.xml.XMLReader.elementNotNull;
import static com.xenoage.zong.desktop.io.musiclayout.ChordSpacingsReader.readChordSpacings;

import java.io.IOException;

import org.w3c.dom.Element;

import com.xenoage.zong.musiclayout.settings.ChordSpacings;
import com.xenoage.zong.musiclayout.settings.Spacings;

/**
 * Desktop reader for {@link Spacings}.
 * 
 * @author Andreas Wenger
 */
public class SpacingsReader {

	/**
	 * Reads a {@link Spacings} from the given XML element.
	 */
	public static Spacings readSpacings(Element eSpacings)
		throws IOException {
		ChordSpacings normalChordSpacings, graceChordSpacings;
		float widthSharp, widthFlat, widthClef, widthMeasureEmpty, widthDistanceMin;

		Element eChords = elementNotNull(eSpacings, "chords");
		normalChordSpacings = readChordSpacings(elementNotNull(eChords, "normal"));
		graceChordSpacings = readChordSpacings(elementNotNull(eChords, "grace"));

		Element e = elementNotNull(eSpacings, "clef");
		widthClef = Float.parseFloat(attributeNotNull(e, "width"));
		Element eKey = elementNotNull(eSpacings, "key");
		e = elementNotNull(eKey, "sharp");
		widthSharp = Float.parseFloat(attributeNotNull(e, "width"));
		e = elementNotNull(eKey, "flat");
		widthFlat = Float.parseFloat(attributeNotNull(e, "width"));
		e = elementNotNull(eSpacings, "measure");
		widthMeasureEmpty = Float.parseFloat(attributeNotNull(e, "empty"));
		e = elementNotNull(eSpacings, "distance");
		widthDistanceMin = Float.parseFloat(attributeNotNull(e, "minimal"));

		return new Spacings(normalChordSpacings, graceChordSpacings, widthSharp, widthFlat, widthClef,
			widthMeasureEmpty, widthDistanceMin);
	}
	
}
