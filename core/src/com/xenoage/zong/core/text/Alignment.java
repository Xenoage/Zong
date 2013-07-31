package com.xenoage.zong.core.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * Text alignment.
 * 
 * @author Andreas Wenger
 */
public enum Alignment {

	Left,
	Center,
	Right,
	Justified;


	/**
	 * Gets the {@link Alignment} of the given {@link AttributeSet},
	 * or Left, if unknown.
	 */
	public static Alignment fromAttributeSet(AttributeSet attr) {
		if (attr == null) {
			return Alignment.Left;
		}
		int align = StyleConstants.getAlignment(attr);
		if (align == StyleConstants.ALIGN_LEFT)
			return Alignment.Left;
		if (align == StyleConstants.ALIGN_CENTER)
			return Alignment.Center;
		if (align == StyleConstants.ALIGN_RIGHT)
			return Alignment.Right;
		if (align == StyleConstants.ALIGN_JUSTIFIED)
			return Alignment.Justified;
		return Alignment.Left;
	}


	/**
	 * Sets this {@link Alignment} to the given {@link AttributeSet}.
	 */
	public void applyOnAttributeSet(SimpleAttributeSet style) {
		if (this == Alignment.Left) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		}
		else if (this == Alignment.Center) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		}
		else if (this == Alignment.Right) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
		}
		else if (this == Alignment.Justified) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);
		}
	}

}
