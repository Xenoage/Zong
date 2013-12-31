package com.xenoage.zong.desktop.io.symbols;

import java.awt.geom.GeneralPath;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.Parser;
import com.xenoage.utils.jse.xml.XMLReader;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.io.symbols.SvgPathReader;
import com.xenoage.zong.symbols.PathSymbol;

/**
 * A {@link SvgPathSymbolReader} creates a {@link PathSymbol}
 * from a given SVG document.
 *
 * @author Andreas Wenger
 */
final class SvgPathSymbolReader {

	/**
	 * Creates a {@link PathSymbol} from the given SVG document.
	 * If an error occurs, an IllegalArgumentException is thrown.
	 */
	public static PathSymbol read(String id, Document doc) {
		SvgPathReader<GeneralPath> svgPathReader = new AwtSvgPathReader();

		//read baseline and ascent, if there
		Element root = XMLReader.root(doc);
		Float baseline = null;
		Float ascent = null;
		String attr = XMLReader.attribute(root, "score:baseline");
		if (attr != null)
			baseline = Parser.parseFloat(attr) * 0.01f - 10;
		attr = XMLReader.attribute(root, "score:ascent");
		if (attr != null)
			ascent = Parser.parseFloat(attr) * 0.01f;

		//custom left and right border, if there
		Float leftBorder = null;
		Float rightBorder = null;
		attr = XMLReader.attribute(root, "score:leftborder");
		if (attr != null)
			leftBorder = Parser.parseFloat(attr) * 0.01f - 10;
		attr = XMLReader.attribute(root, "score:rightborder");
		if (attr != null)
			rightBorder = Parser.parseFloat(attr) * 0.01f - 10;

		//search for a path
		Element ePath = XMLReader.element(root, "path");

		//if not found, search for a group, and a path in this group
		if (ePath == null) {
			Element eGroup = XMLReader.element(root, "g");
			if (eGroup != null) {
				ePath = XMLReader.element(eGroup, "path");
			}
			//if still not found, search for another group
			if (ePath == null) {
				eGroup = XMLReader.element(eGroup, "g");
				if (eGroup != null) {
					ePath = XMLReader.element(eGroup, "path");
				}
			}
		}

		//if the path was found, parse it and create a PathSymbol,
		//otherwise return null.
		if (ePath != null && XMLReader.attribute(ePath, "d") != null) {
			Tuple2<GeneralPath, Rectangle2f> path = svgPathReader.read(XMLReader.attribute(ePath, "d"));
			PathSymbol ret = new PathSymbol(id, path.get1(), path.get2(),
				baseline, ascent, leftBorder, rightBorder);
			return ret;
		}
		else {
			throw new IllegalArgumentException("No path element was found!");
		}
	}

}
