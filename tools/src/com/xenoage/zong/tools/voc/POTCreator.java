package com.xenoage.zong.tools.voc;

import java.io.IOException;

import com.xenoage.zong.Voc;

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
