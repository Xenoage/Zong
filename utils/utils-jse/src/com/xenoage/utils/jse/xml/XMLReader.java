package com.xenoage.utils.jse.xml;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.kernel.Range.range;

/**
 * This class contains some helper functions
 * to parse XML files and read values
 * out of XML documents with JAXP.
 *
 * @author Andreas Wenger
 */
public class XMLReader {

	/**
	 * Reads and returns the XML document at the given path.
	 */
	public static Document readFile(String file)
		throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new NoResolver());
		return builder.parse(file);
	}

	/**
	 * Reads and returns the XML document at the given input stream.
	 */
	public static Document readFile(InputStream stream)
		throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new NoResolver());
			return builder.parse(stream);
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * Reads and returns the XML document in the given string.
	 */
	public static Document read(String data)
		throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new NoResolver());
			return builder.parse((new InputSource(new StringReader(data))));
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * Returns a XMLStreamReader for the XML document at the given path.
	 */
	public static XMLStreamReader createXMLStreamReader(String file)
		throws IOException, XMLStreamException {
		return createXMLStreamReader(new FileInputStream(file));
	}

	/**
	 * Returns a XMLStreamReader for the XML document at the given input stream.
	 */
	public static XMLStreamReader createXMLStreamReader(InputStream stream)
		throws IOException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		//disable DTD resolver
		factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		XMLStreamReader reader = factory.createXMLStreamReader(stream);
		return reader;
	}

	/**
	 * Gets the root element of the given document.
	 */
	public static Element root(Document doc) {
		return doc.getDocumentElement();
	}

	/**
	 * Gets the trimmed text of the given element, or "".
	 */
	public static String text(Element element) {
		if (element == null)
			return "";
		else
			return getTextContent(element).trim();
	}

	/**
	 * Gets the untrimmed trimmed text of the given element, or "".
	 */
	public static String textUntrimmed(Element element) {
		if (element == null)
			return "";
		else
			return getTextContent(element);
	}

	/**
	 * Reads and returns the value of the attribute with the
	 * given name of the given element, or null if not found.
	 */
	public static String attribute(Element element, String name) {
		String ret = attributeNotNull(element, name);
		return (ret.length() > 0 ? ret : null);
	}

	/**
	 * Reads and returns the value of the attribute with the
	 * given name of the given element, or "" if not found.
	 */
	public static String attributeNotNull(Element element, String name) {
		return element.getAttribute(name);
	}

	/**
	 * Gets the first child element of the given node, or null if not found.
	 */
	public static Element element(Node parent) {
		for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node instanceof Element)
				return (Element) node;
		}
		return null;
	}

	/**
	 * Gets the first child element of the given node with
	 * the given name and id attribute, or throws a {@link RuntimeException} if it is missing.
	 */
	public static Element elementWithIDNotNull(Element parent, String name, String id) {
		Element ret = element(parent, name, id);
		if (ret == null)
			throw new RuntimeException("Required child element \"" + name + "\" with id \"" + id + "\" " +
				"in parent element \"" + parent.getNodeName() + "\" is missing!");
		return ret;
	}

	/**
	 * Gets the first child element of the given node with
	 * the given name, or throws a {@link RuntimeException} if it is missing.
	 */
	public static Element elementNotNull(Element parent, String name) {
		Element ret = element(parent, name);
		if (ret == null)
			throw new RuntimeException("Required child element \"" + name + "\" in parent element \"" +
				parent.getNodeName() + "\" is missing!");
		return ret;
	}

	/**
	 * Gets the first child element of the given node with
	 * the given name, or null if not found.
	 */
	public static Element element(Element parent, String name) {
		for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node instanceof Element && node.getNodeName().equals(name))
				return (Element) node;
		}
		return null;
	}

	/**
	 * Gets the first child element of the given node with
	 * the given name and id attribute, or null if not found.
	 */
	public static Element element(Element parent, String name, String id) {
		for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node instanceof Element && node.getNodeName().equals(name)) {
				Element e = (Element) node;
				if (id.equals(e.getAttribute("id")))
					return (Element) node;
			}
		}
		return null;
	}

	/**
	 * Gets the a list of all child elements of the given node with
	 * the given name. If no such elements are found, the returned
	 * list is empty. 
	 */
	public static List<Element> elements(@MaybeNull Node parent, @NonNull String name) {
		ArrayList<Element> ret = new ArrayList<>();
		if (parent != null) {
			for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeName().equals(name))
					ret.add((Element) node);
			}
		}
		return ret;
	}

	/**
	 * Gets the a list of all child elements of the given node.
	 * If no such elements are found, the returned list is empty. 
	 */
	public static ArrayList<Element> elements(Element parent) {
		ArrayList<Element> ret = new ArrayList<>(parent.getChildNodes().getLength());
		for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node.getNodeType() == Node.ELEMENT_NODE)
				ret.add((Element) node);
		}
		return ret;
	}

	/**
	 * Gets the text of the given child element with the given name
	 * of the given parent element, or null if it does not exist.
	 */
	public static String elementText(Element parentElement, String elementName) {
		Element e = element(parentElement, elementName);
		if (e == null)
			return null;
		else
			return text(e);
	}

	/**
	 * Gets the text of the given child element with the given name
	 * of the given parent element, or "", even if it does not exist.
	 */
	public static String elementTextNotNull(Element parentElement, String elementName) {
		return text(element(parentElement, elementName));
	}

	/**
	 * getTextContent reimplementation (needed e.g. for Android).
	 * See documentation of J2SE's org.w3c.dom.Node.getTextContent().
	 */
	public static String getTextContent(Node node) {
		switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
			case Node.ATTRIBUTE_NODE:
			case Node.ENTITY_NODE:
			case Node.ENTITY_REFERENCE_NODE:
			case Node.DOCUMENT_FRAGMENT_NODE:
				StringBuilder ret = new StringBuilder();
				NodeList cs = node.getChildNodes();
				for (int i : range(cs.getLength())) {
					Node c = cs.item(i);
					short ct = c.getNodeType();
					if (ct != Node.COMMENT_NODE && ct != Node.PROCESSING_INSTRUCTION_NODE)
						ret.append(getTextContent(c));
				}
				return ret.toString();
			case Node.TEXT_NODE:
			case Node.CDATA_SECTION_NODE:
			case Node.COMMENT_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
				return node.getNodeValue();
			case Node.DOCUMENT_NODE:
			case Node.DOCUMENT_TYPE_NODE:
			case Node.NOTATION_NODE:
				return null;
		}
		return null;
	}

}
