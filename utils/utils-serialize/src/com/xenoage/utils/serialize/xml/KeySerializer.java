package com.xenoage.utils.serialize.xml;

import static com.xenoage.utils.jse.xml.XMLWriter.addAttribute;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.Key;
import com.xenoage.utils.jse.xml.Parse;
import com.xenoage.utils.jse.xml.XMLWriter;

public class KeySerializer {

	public static final String ELEMENT_NAME = "Key";


	public static Key read(Element e) {
		int code = Parse.parseAttrInt(e, "code");
		boolean shift = Parse.parseAttrBooleanDefault(e, "shift", false);
		boolean ctrl = Parse.parseAttrBooleanDefault(e, "ctrl", false);
		boolean alt = Parse.parseAttrBooleanDefault(e, "alt", false);
		return new Key(code, shift, ctrl, alt);
	}

	public static void write(Element parent, Key k) {
		Element e = XMLWriter.addElement(ELEMENT_NAME, parent);
		addAttribute(e, "code", k.code);
		if (k.shift)
			addAttribute(e, "shift", true);
		if (k.ctrl)
			addAttribute(e, "ctrl", true);
		if (k.alt)
			addAttribute(e, "alt", true);
	}

}
