package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlDataException;

/**
 * Reader and writer methods for MusicXML's yes-no,
 * since yes-no is represented by boolean values in this project.
 * 
 * @author Andreas Wenger
 */
public class MxlYesNo {

	@NonNull public static boolean read(String s) {
		if (s.equals("yes"))
			return true;
		else if (s.equals("no"))
			return false;
		throw new XmlDataException("yes-no = " + s);
	}

	@MaybeNull public static Boolean readNull(String s) {
		if (s == null)
			return null;
		else
			return read(s);
	}

	public static String write(boolean v) {
		return v ? "yes" : "no";
	}

}
