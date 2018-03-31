package com.xenoage.utils.jse;

import java.awt.event.KeyEvent;

/**
 * A keyboard key consists of a keycode (an integer for letters
 * like 'a', '$', Enter, ...; corresponding to the
 * {@link KeyEvent}'s VK_ constants and a combination of
 * modifiers (Alt, Shift, Ctrl).
 * 
 * @author Andreas Wenger
 */
public class Key {

	public final int code;
	public final char character;
	public final boolean shift, ctrl, alt;


	public Key(int code, boolean shift, boolean ctrl, boolean alt) {
		this.code = code;
		this.character = KeyEvent.VK_UNDEFINED;
		this.shift = shift;
		this.ctrl = ctrl;
		this.alt = alt;
	}

	public Key(KeyEvent awtKey) {
		this.code = awtKey.getKeyCode();
		this.character = awtKey.getKeyChar();
		this.shift = (awtKey.getModifiers() & KeyEvent.SHIFT_MASK) > 0;
		this.ctrl = (awtKey.getModifiers() & KeyEvent.CTRL_MASK) > 0;
		this.alt = (awtKey.getModifiers() & KeyEvent.ALT_MASK) > 0;
	}

	@Override public boolean equals(Object o) {
		if (o instanceof Key) {
			Key k = (Key) o;
			return k.code == code && k.shift == shift && k.ctrl == ctrl && k.alt == alt;
		}
		return false;
	}

	@Override public int hashCode() {
		return code + (shift ? 1000000 : 0) + (ctrl ? 1100000 : 0) + (alt ? 1200000 : 0);
	}

	@Override public String toString() {
		return (shift ? "SHIFT+" : "") + (ctrl ? "CTRL+" : "") + (alt ? "ALT+" : "") + "[" + character +
			" (code " + code + ")]";
	}

}
