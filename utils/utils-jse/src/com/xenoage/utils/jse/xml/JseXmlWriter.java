package com.xenoage.utils.jse.xml;

import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.xenoage.utils.jse.xml.indent.IndentingXMLStreamWriter;
import com.xenoage.utils.xml.XmlException;
import com.xenoage.utils.xml.XmlWriter;

/**
 * Java SE implementation of an {@link XmlWriter},
 * based on a {@link XMLStreamWriter}.
 * 
 * @author Andreas Wenger
 */
public class JseXmlWriter
	extends XmlWriter {

	private XMLStreamWriter writer;


	public JseXmlWriter(OutputStream out) {
		XMLOutputFactory output = XMLOutputFactory.newInstance();
		try {
			writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(out, "UTF-8"));
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeStartDocument() {
		try {
			writer.writeStartDocument();
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeDTD(String dtd) {
		try {
			writer.writeDTD(dtd);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeElementStart(String name) {
		try {
			writer.writeStartElement(name);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeElementEnd() {
		try {
			writer.writeEndElement();
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeElementEmpty(String name) {
		try {
			writer.writeEmptyElement(name);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeText(String text) {
		try {
			writer.writeCharacters(text);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override protected void writeAttributeInternal(String name, String value) {
		try {
			writer.writeAttribute(name, value);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeComment(String text) {
		try {
			writer.writeComment(text);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void writeLineBreak() {
		try {
			writer.writeCharacters("\n");
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

}
