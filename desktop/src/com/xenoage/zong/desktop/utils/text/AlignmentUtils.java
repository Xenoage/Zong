package com.xenoage.zong.desktop.utils.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

import com.xenoage.zong.core.text.Alignment;

/**
 * Working with {@link Alignment} in Java SE.
 * 
 * @author Andreas Wenger
 */
public class AlignmentUtils {

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
	 * Applies the given {@link Alignment} on the given {@link MutableAttributeSet}.
	 */
	public static void applyAlignmentToAttributeSet(Alignment alignment, MutableAttributeSet style) {
		if (alignment == Alignment.Left) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		}
		else if (alignment == Alignment.Center) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		}
		else if (alignment == Alignment.Right) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
		}
		else if (alignment == Alignment.Justified) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);
		}
	}

}
