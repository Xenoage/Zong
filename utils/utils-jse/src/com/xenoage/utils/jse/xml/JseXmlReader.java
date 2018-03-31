package com.xenoage.utils.jse.xml;

import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlException;
import com.xenoage.utils.xml.XmlReader;

import javax.xml.stream.*;
import java.io.InputStream;

/**
 * Java SE implementation of an {@link XmlReader},
 * based on a {@link XMLStreamReader}.
 * 
 * @author Andreas Wenger
 */
public class JseXmlReader
	extends XmlReader {
	
	private InputStream inputStream;
	private XMLStreamReader reader;
	private boolean cancelNextClose = false;
	
	public JseXmlReader(InputStream inputStream) {
		XMLInputFactory input = XMLInputFactory.newInstance();
		//disable DTDs for speed
		input.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		input.setProperty(XMLInputFactory.IS_COALESCING, true);
		input.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
		try {
			this.inputStream = inputStream;
			this.reader = input.createXMLStreamReader(inputStream);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getElementName() {
		try {
			return reader.getLocalName();
		} catch (IllegalStateException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getText() {
		try {
			while (reader.hasNext()) {
				int event = reader.next();
				if (event == XMLStreamConstants.CHARACTERS) {
					break;
				}
				else if (event == XMLStreamConstants.END_ELEMENT) {
					cancelNextClose = true;
					return null;
				}
			}
			return reader.getText();
		} catch (Exception ex) {
			throw new XmlException(ex);
		}
	}

	@Override public int getAttributeCount() {
		try {
			return reader.getAttributeCount();
		} catch (IllegalStateException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getAttributeName(int index) {
		try {
			return reader.getAttributeLocalName(index);
		} catch (IllegalStateException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getAttribute(int index) {
		try {
			return reader.getAttributeValue(index);
		} catch (IllegalStateException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public boolean openNextChildElement() {
		try {
			while (reader.hasNext()) {
				int event = reader.next();
				if (event == XMLStreamConstants.START_ELEMENT)
					return true;
				else if (event == XMLStreamConstants.END_ELEMENT) {
					cancelNextClose = true; //we already reached the end of the parent
					return false;
				}
			}
			return false;
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public void closeElement() {
		if (cancelNextClose) {
			cancelNextClose = false;
			return;
		}
		try {
			int openChildren = 1;
			while (reader.hasNext()) {
				int event = reader.next();
				if (event == XMLStreamConstants.START_ELEMENT) {
					openChildren++;
					//TEST System.out.println(reader.getLocalName());
				}
				else if (event == XMLStreamConstants.END_ELEMENT) {
					openChildren--;
				//TEST System.out.println("/" + reader.getLocalName());
					if (openChildren == 0)
						return;
				}	
			}
		} catch (XMLStreamException ex) {
			throw new XmlException(ex);
		}
	}
	
	private String getLocation() {
		Location location = reader.getLocation();
		return "line " + location.getLineNumber() + ", column " + location.getColumnNumber();
	}

	@Override public XmlDataException dataException() {
		return new XmlDataException("(at " + getLocation() + ")");
	}

	@Override public XmlDataException dataException(String message) {
		return new XmlDataException(message + " (at " + getLocation() + ")");
	}

	@Override public int getLine() {
		return reader.getLocation().getLineNumber();
	}

	@Override public void close() {
		try {
			reader.close();
			inputStream.close();
		} catch (Exception ex) {
		}
	}

}
