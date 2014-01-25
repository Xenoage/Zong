package com.xenoage.zong.io.musiclayout;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.io.IOException;
import java.util.HashMap;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Reader for {@link ChordWidths}.
 * 
 * @author Andreas Wenger
 */
public class ChordWidthsReader {

	/**
	 * Reads a {@link ChordWidths} from the given {@link XmlReader} at the "chordwidths" element.
	 */
	public static ChordWidths readChordWidths(XmlReader r)
		throws IOException {
		HashMap<String, Float> m = map();
		while (r.openNextChildElement()) {
			String n = r.getElementName();
			String width = r.getAttribute("width");
			if (width != null) {
				m.put(n, Float.parseFloat(width));
			}
			r.closeElement();
		}
		return new ChordWidths(v(m, "whole"), v(m, "half"), v(m, "quarter"), v(m, "dotGap"),
			v(m, "dot"), v(m, "accToNoteGap"), v(m, "accToAccGap"), v(m, "doubleSharp"), v(m, "sharp"),
			v(m, "natural"), v(m, "flat"), v(m, "doubleFlat"));
	}

	private static float v(HashMap<String, Float> m, String key) {
		Float ret = m.get(key);
		if (ret == null)
			return 0;
		return ret;
	}

}
