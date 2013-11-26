package com.xenoage.zong;

import java.io.IOException;

/**
 * Creates a POT file (GNU gettext template) with all vocab from {@link Voc}.
 * 
 * @author Andreas Wenger
 */
public class POTCreator {

	public static void main(String... args)
		throws IOException {
		POCreator.write(Voc.values(), null, null, "default.pot");
	}

}
