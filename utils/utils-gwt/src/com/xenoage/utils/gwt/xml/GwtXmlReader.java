package com.xenoage.utils.gwt.xml;

import com.google.gwt.xml.client.*;
import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlException;
import com.xenoage.utils.xml.XmlReader;

import static com.xenoage.utils.kernel.Range.range;

/**
 * GWT implementation of an {@link XmlReader},
 * based on GWT's {@link XMLParser} and {@link Document}.
 * 
 * @author Andreas Wenger
 */
public class GwtXmlReader
	extends XmlReader {

	private Document doc;
	private Node currentNode;
	private boolean cursorAtElementEnd = false;
	

	public GwtXmlReader(String xmlData) {
		try {
			this.doc = XMLParser.parse(xmlData);
			this.currentNode = doc;
		} catch (DOMException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getElementName() {
		return currentNode.getNodeName();
	}

	@Override public String getText() {
		String s = "";
		for (int i : range(currentNode.getChildNodes().getLength())) {
			Node child = currentNode.getChildNodes().item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				s += ((Text) child).getNodeValue();
		}
		return (s.length() > 0 ? s : null);
	}

	@Override public int getAttributeCount() {
		return currentNode.getAttributes().getLength();
	}

	@Override public String getAttributeName(int index) {
		return currentNode.getAttributes().item(index).getNodeName();
	}

	@Override public String getAttribute(int index) {
		return currentNode.getAttributes().item(index).getNodeValue();
	}

	@Override public boolean openNextChildElement() {
		//if the cursor is within an element and the current node has a child element, open it
		if (false == cursorAtElementEnd) {
			Node firstChild = getFirstChildElement(currentNode);
			if (firstChild != null) {
				currentNode = firstChild;
				cursorAtElementEnd = false;
				return true;
			}
		}
		//otherwise, move to the next sibling if there is one
		else {
			Node nextSibiling = getNextSiblingElement(currentNode);
			if (nextSibiling != null) {
				currentNode = nextSibiling;
				cursorAtElementEnd = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the first child element of the given element,
	 * or null if there is none.
	 */
	private Node getFirstChildElement(Node element) {
		for (int i : range(element.getChildNodes().getLength())) {
			Node child = element.getChildNodes().item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				return child;
		}
		return null;
	}

	/**
	 * Gets the next sibling element of the given element,
	 * or null if there is none.
	 */
	private Node getNextSiblingElement(Node element) {
		Node sibling = element;
		while (true) {
			sibling = sibling.getNextSibling();
			if (sibling == null)
				return null;
			else if (sibling.getNodeType() == Node.ELEMENT_NODE)
				return sibling;
		}
	}

	@Override public void closeElement() {
		//if we are within an element, move cursor to the end of the element
		if (false == cursorAtElementEnd)
			cursorAtElementEnd = true;
		//else if we are at the end of an element, move back to the parent element
		else
			currentNode = currentNode.getParentNode();
	}

	@Override public XmlDataException dataException() {
		return new XmlDataException("");
	}

	@Override public XmlDataException dataException(String message) {
		return new XmlDataException(message);
	}

	@Override public int getLine() {
		return -1;
	}

	@Override public void close() {
		currentNode = null;
		doc = null;
	}

}
