package com.xenoage.zong.core.music.util;

import lombok.Data;
import lombok.NonNull;

import com.xenoage.zong.core.position.MP;


/**
 * Musically positioned element.
 * 
 * This is a wrapper class to combine any object with
 * the {@link MP} it belongs to.
 * 
 * @author Andreas Wenger
 */
@Data public class MPE<T> {

	@NonNull public final T element;
	@NonNull public final MP mp;

	
	public static <T> MPE<T> mpE(T element, MP mp) {
		return new MPE<T>(element, mp);
	}

	@Override public String toString() {
		return element.toString() + " at MP " + mp.toString();
	}
	
}
