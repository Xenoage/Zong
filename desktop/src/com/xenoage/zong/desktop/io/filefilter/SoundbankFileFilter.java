package com.xenoage.zong.desktop.io.filefilter;

import lombok.Getter;

/**
 * Filter for soundbank files.
 * 
 * @author Andreas Wenger
 */
public class SoundbankFileFilter {

	@Getter private static final String[] extensions = { "*.sf2", "*.dls", "*.pat", "*.cfg", "*.wav",
		"*.au", "*.aif" };


	public static String getDescription() {
		StringBuilder s = new StringBuilder();
		s.append("Soundbank (");
		for (int i = 0; i < extensions.length; i++) {
			s.append(extensions[i]);
			if (i < extensions.length - 1)
				s.append(",");
		}
		s.append(")");
		return s.toString();
	}

}
