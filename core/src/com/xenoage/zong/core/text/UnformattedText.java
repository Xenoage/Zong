package com.xenoage.zong.core.text;

import lombok.Data;

/**
 * The simplest implementation of {@link Text}:
 * Just an ordinary, unformatted {@link String}.
 * 
 * @author Andreas Wenger
 */
@Data public class UnformattedText
	implements Text {

	private final String text;


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
