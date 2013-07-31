package com.xenoage.zong.core.text;


/**
 * The simplest implementation of {@link Text}:
 * Just an ordinary, unformatted {@link String}.
 * 
 * @author Andreas Wenger
 */
public class UnformattedText
	implements Text {

	private final String text;


	public UnformattedText(String text) {
		this.text = text;
	}


	public static UnformattedText ut(String text) {
		return new UnformattedText(text);
	}


	@Override public int getLength() {
		return text.length();
	}


	@Override public String toString() {
		return text;
	}

}
