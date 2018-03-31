package com.xenoage.utils.serialize.xml;

import static com.xenoage.utils.jse.xml.XMLReader.element;
import static com.xenoage.utils.jse.xml.XMLWriter.addAttribute;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLWriter;
import com.xenoage.utils.math.geom.Rectangle2f;

public class Rectangle2fSerializer {

	public static final String ELEMENT_NAME = "Rectangle2f";


	public static Rectangle2f read(Element e) {
		return new Rectangle2f(Point2fSerializer.read(element(e, Point2fSerializer.ELEMENT_NAME)),
			Size2fSerializer.read(element(e, Size2fSerializer.ELEMENT_NAME)));
	}

	public static void write(Element parent, Rectangle2f r) {
		write(parent, r, null);
	}

	public static void write(Element parent, Rectangle2f r, String id) {
		Element e = XMLWriter.addElement(ELEMENT_NAME, parent);
		if (id != null)
			addAttribute(e, "id", id);
		Point2fSerializer.write(e, r.position);
		Size2fSerializer.write(e, r.size);
	}

}
