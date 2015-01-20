package com.xenoage.zong.io.symbols;

import com.xenoage.utils.Parser;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.path.Path;

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
	 * @param id         the ID of the symbol
	 * @param xmlReader  the {@link XmlReader}, positioned at the root element. It will
	 *                   not be closed by this this method.
	 */
	public static PathSymbol read(String id, XmlReader xmlReader) {
		
		//read baseline and ascent, if there
		Float baseline = null;
		Float ascent = null;
		String attr = xmlReader.getAttribute("score:baseline");
		if (attr != null)
			baseline = Parser.parseFloat(attr) * 0.01f - 10;
		attr = xmlReader.getAttribute("score:ascent");
		if (attr != null)
			ascent = Parser.parseFloat(attr) * 0.01f;

		//custom left and right border, if there
		Float leftBorder = null;
		Float rightBorder = null;
		attr = xmlReader.getAttribute("score:leftborder");
		if (attr != null)
			leftBorder = Parser.parseFloat(attr) * 0.01f - 10;
		attr = xmlReader.getAttribute("score:rightborder");
		if (attr != null)
			rightBorder = Parser.parseFloat(attr) * 0.01f - 10;

		//search for a path
		String d = findPath(xmlReader);

		//if the path was found, parse it and create a PathSymbol,
		//otherwise throw exception.
		if (d != null) {
			Path path = new SvgPathReader(d).read();
			PathSymbol ret = new PathSymbol(id, path, baseline, ascent, leftBorder, rightBorder);
			return ret;
		}
		else {
			throw new IllegalArgumentException("No path element was found!");
		}
	}
	
	/**
	 * Searches the first element named "path" and returns its "d" attribute.
	 * If not found, null is returned.
	 */
	private static String findPath(XmlReader xmlReader) {
		while (xmlReader.openNextChildElement()) {
			String n = xmlReader.getElementName();
			if (n.equals("path")) {
				//path found
				return xmlReader.getAttribute("d");
			}
			else {
				//other element. look into children
				String d = findPath(xmlReader);
				if (d != null)
					return d;
			}
			xmlReader.closeElement();
		}
		return null;
	}

}
