package com.xenoage.utils.jse.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class contains some helper functions
 * to write XML files, using Java's built-in JAXP.
 *
 * @author Andreas Wenger
 */
public class XMLWriter {

	/**
	 * Creates an empty DOM document.
	 */
	public static Document createEmptyDocument() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.newDocument();
		} catch (Exception ex) {
			throw new RuntimeException("Creating a new DOM document failed: " + ex.toString());
		}
	}

	/**
	 * Writes the given XML document into the given {@link OutputStream}.
	 */
	public static void writeFile(Document doc, OutputStream outputStream)
		throws IOException {
		try {
			Transformer transformer = createTransformer(doc);

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outputStream);

			transformer.transform(source, result);
		} catch (Exception ex) {
			throw new IOException("Problem with XML-Transformer: " + ex.toString());
		}
	}

	/**
	 * Adds and returns a new element with the given
	 * name as a child of the given element.
	 */
	public static Element addElement(String name, Node parent) {
		//get the parent document.
		//parentElement may be the document itself.
		Document parentDoc = parent.getOwnerDocument();
		Document doc = (parentDoc != null ? parentDoc : (Document) parent);
		//add element
		Node ret = doc.createElement(name);
		parent.appendChild(ret);
		return (Element) ret;
	}

	/**
	 * Adds and returns a new element with the given
	 * name as a child of the given element, using the given text content.
	 * But if the given text is null, nothing is added and null is returned.
	 */
	public static Element addElement(String name, Object text, Node parent) {
		if (text != null) {
			Element ret = addElement(name, parent);
			ret.setTextContent(text.toString());
			return ret;
		}
		else {
			return null;
		}
	}

	/**
	 * Adds a new attribute with the given name and value to the given element.
	 * If the given value is null, the attribute is not written.
	 */
	public static void addAttribute(Element element, String name, Object value) {
		if (value != null) {
			element.setAttribute(name, value.toString());
		}
	}

	/**
	 * Returns the given document as a String. If it fails, null is returned.
	 */
	public static String toString(Document document) {
		try {
			StringWriter sw = new StringWriter();
			Transformer t = createTransformer(document);
			t.transform(new DOMSource(document), new StreamResult(sw));
			return sw.toString();
		} catch (TransformerException ex) {
			return null;
		}
	}

	private static Transformer createTransformer(Document doc)
		throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		if (doc.getDoctype() != null) {
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doc.getDoctype().getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doc.getDoctype().getSystemId());
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		return transformer;
	}

}
