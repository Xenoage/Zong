package musicxmltestsuite.tests.base;

import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;


@ToDo("Currently only -7 to +7 is supported")
public interface Base13a
	extends Base {
	
	@Override default String getFileName() {
		return "13a-KeySignatures.xml";
	}
	
	default TraditionalKey[] getExpectedKeys() {
		TraditionalKey[] expectedKeys = new TraditionalKey[15 * 2];
		for (int fifths = -7; fifths <= 7; fifths++) {
			expectedKeys[(fifths + 7) * 2 + 0] = new TraditionalKey(fifths, Mode.Major);
			expectedKeys[(fifths + 7) * 2 + 1] = new TraditionalKey(fifths, Mode.Minor);
		}
		return expectedKeys;
	}
	
}
