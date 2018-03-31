package com.xenoage.utils.jse.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * Runtime exception for invalid XML data.
 * 
 * @author Andreas Wenger
 */
public final class InvalidXMLData
	extends RuntimeException {

	private final Element element;


	public InvalidXMLData(Element element) {
		if (element == null)
			throw new IllegalArgumentException();
		this.element = element;
	}

	public static InvalidXMLData invalid(Element element) {
		return new InvalidXMLData(element);
	}

	public static <T> T throwNull(T o, Element e) {
		if (o != null) {
			return o;
		}
		throw invalid(e);
	}

	public List<Node> getStack() {
		Node n = element;
		LinkedList<Node> stack = new LinkedList<>();
		do {
			stack.addFirst(n);
			n = n.getParentNode();
		} while (n != null);
		return stack;
	}

	@Override public String getMessage() {
		StringBuilder ret = new StringBuilder();
		for (Node node : getStack()) {
			ret.append("/" + node.getNodeName());
		}
		return ret.toString();
	}

}
