package com.xenoage.utils.xml;





/**
 * Platform independent base class of a XML stream writer,
 * inspired by the StAX Cursor API.
 * 
 * Implementations for different platforms (Java SE, Android, GWT, ...)
 * are provided in the corresponding platform libraries.
 * 
 * @author Andreas Wenger
 */
public abstract class XmlWriter {
	
	/**
   * Writes the XML declaration, with version to 1.0 and the encoding utf-8.
   */
  public abstract void writeStartDocument();
  
  /**
   * Writes a DTD section. This string represents the entire doctypedecl production
   * from the XML 1.0 specification.
   */
  public abstract void writeDTD(String dtd);
	
	/**
   * Writes an element start tag with the given local name.
   */
  public abstract void writeElementStart(String name);
  
  /**
   * Writes an element end tag.
   */
  public abstract void writeElementEnd();
  
  /**
   * Writes an empty element with the given local name.
   */
  public abstract void writeElementEmpty(String name);
	
  /**
   * Writes an element with the given local name and text content.
   * If the given text null, nothing is written.
   */
  public void writeElementText(String name, String text) {
  	if (text == null)
  		return;
  	writeElementStart(name);
  	writeText(text);
  	writeElementEnd();
  }
  
  /**
   * Writes an element with the given local name and text content.
   * If the given text null, nothing is written.
   * The text is converted to a {@link String} using {@link Object#toString()}.
   */
  public void writeElementText(String name, Object text) {
  	if (text == null)
  		return;
  	writeElementText(name, text.toString());
  }
  
  /**
   * Writes the given text content to this element.
   */
  public abstract void writeText(String text);
  
	/**
   * Writes an attribute with the given local name and value at this position.
   * Nothing is written if the given value is null.
   */
  public void writeAttribute(String name, String value) {
  	if (value == null)
  		return;
  	writeAttributeInternal(name, value);
  }
  
  /**
   * Writes an attribute with the given local name and value at this position.
   */
  protected abstract void writeAttributeInternal(String name, String value);
  
  /**
   * Writes an attribute with the given local name and value at this position.
   * Nothing is written if the given value is null.
   * The value is converted to a {@link String} using {@link Object#toString()}.
   */
  public void writeAttribute(String name, Object value) {
  	if (value == null)
  		return;
  	writeAttributeInternal(name, value.toString());
  }
  
  /**
   * Writes an XML comment with the given text.
   */
  public abstract void writeComment(String text);
  
  /**
   * Writes a line break.
   */
  public abstract void writeLineBreak();

}
