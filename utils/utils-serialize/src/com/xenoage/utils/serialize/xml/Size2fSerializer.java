package com.xenoage.utils.serialize.xml;

import static com.xenoage.utils.jse.xml.Parse.parseAttrFloat;
import static com.xenoage.utils.jse.xml.XMLWriter.addAttribute;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLWriter;
import com.xenoage.utils.math.geom.Size2f;

public class Size2fSerializer {

	public static final String ELEMENT_NAME = "Size2f";


	public static Size2f read(Element e) {
		return new Size2f(parseAttrFloat(e, "width"), parseAttrFloat(e, "height"));
	}

	public static void write(Element parent, Size2f s) {
		Element e = XMLWriter.addElement(ELEMENT_NAME, parent);
		addAttribute(e, "width", s.width);
		addAttribute(e, "height", s.height);
	}

}
