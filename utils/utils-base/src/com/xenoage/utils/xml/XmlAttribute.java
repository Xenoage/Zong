package com.xenoage.utils.xml;

/**
 * An XML attribute has a local name and a value.
 * 
 * @author Andreas Wenger
 */
public class XmlAttribute {

	public final String name, value;


	public XmlAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

}
