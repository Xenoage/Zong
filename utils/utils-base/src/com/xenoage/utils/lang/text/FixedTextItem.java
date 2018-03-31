package com.xenoage.utils.lang.text;

/**
 * One piece of text which is independent of the language.
 * 
 * @author Andreas Wenger
 */
public class FixedTextItem
	implements TextItem {

	private final String text;


	public FixedTextItem(String text) {
		this.text = text;
	}

	@Override public String getText() {
		return text;
	}

}
