package com.xenoage.zong.desktop.io.musiclayout;

import static com.xenoage.utils.jse.xml.XMLReader.attributeNotNull;
import static com.xenoage.utils.jse.xml.XMLReader.element;
import static java.lang.Float.parseFloat;

import java.io.IOException;

import org.w3c.dom.Element;

import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Desktop reader for {@link ChordWidths}.
 * 
 * @author Andreas Wenger
 */
public class ChordWidthsReader {

	/**
	 * Reads a {@link ChordWidths} from the given XML element.
	 */
	public static ChordWidths readChordWidths(Element e)
		throws IOException {
		return new ChordWidths(w(e, "whole"), w(e, "half"), w(e, "quarter"), w(e, "dotGap"),
			w(e, "dot"), w(e, "accToNoteGap"), w(e, "accToAccGap"), w(e, "doubleSharp"), w(e, "sharp"),
			w(e, "natural"), w(e, "flat"), w(e, "doubleFlat"));
	}

	private static float w(Element parent, String elementName) {
		return parseFloat(attributeNotNull(element(parent, elementName), "width"));
	}
	
}
