package com.xenoage.utils.serialize.xml;

import static com.xenoage.utils.jse.xml.Parse.parseAttrFloat;
import static com.xenoage.utils.jse.xml.XMLWriter.addAttribute;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLWriter;
import com.xenoage.utils.math.geom.Point2f;

public class Point2fSerializer {

	public static final String ELEMENT_NAME = "Point2f";


	public static Point2f read(Element e) {
		return new Point2f(parseAttrFloat(e, "x"), parseAttrFloat(e, "y"));
	}

	public static void write(Element parent, Point2f p) {
		Element e = XMLWriter.addElement(ELEMENT_NAME, parent);
		addAttribute(e, "x", p.x);
		addAttribute(e, "y", p.y);
	}

}
