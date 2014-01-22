package com.xenoage.utils.android.xml;

import java.io.InputStream;

import javax.xml.stream.XMLStreamConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlException;
import com.xenoage.utils.xml.XmlReader;

/**
 * Android implementation of an {@link XmlReader},
 * based on the Android {@link XmlPullParser}.
 * 
 * @author Andreas Wenger
 */
public class AndroidXmlReader
	extends XmlReader {
	
	private XmlPullParser reader;
	private boolean cancelNextClose = false;
	
	public AndroidXmlReader(InputStream in) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);
	    factory.setNamespaceAware(false);
			reader = factory.newPullParser();
			reader.setInput(new JseInputStream(in), "UTF-8");
		} catch (XmlPullParserException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getElementName() {
		try {
			return reader.getName();
		} catch (IllegalStateException ex) {
			throw new XmlException(ex);
		}
	}

	@Override public String getText() {
		try {
			int event = reader.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				event = reader.next();
				if (event == XmlPullParser.TEXT) {
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
			return reader.getAttributeName(index);
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
			int event = reader.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				event = reader.next();
				if (event == XmlPullParser.START_TAG)
					return true;
				else if (event == XmlPullParser.END_TAG) {
					cancelNextClose = true; //we already reached the end of the parent
					return false;
				}
			}
			return false;
		} catch (Exception ex) {
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
			int event = reader.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				event = reader.next();
				if (event == XmlPullParser.START_TAG) {
					openChildren++;
				}
				else if (event == XmlPullParser.END_TAG) {
					openChildren--;
					if (openChildren == 0)
						return;
				}	
			}
		} catch (Exception ex) {
			throw new XmlException(ex);
		}
	}
	
	private String getLocation() {
		return "line " + reader.getLineNumber() + ", column " + reader.getColumnNumber();
	}

	@Override public XmlDataException dataException() {
		throw new XmlDataException("(at " + getLocation() + ")");
	}

	@Override public XmlDataException dataException(String message) {
		throw new XmlDataException(message + " (at " + getLocation() + ")");
	}

	@Override public int getLine() {
		return reader.getLineNumber();
	}

}
