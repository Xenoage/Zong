package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;


public interface Base13b
	extends Base {

	@Override default String getFileName() {
		return "13b-KeySignatures-ChurchModes.xml";
	}

	default TraditionalKey[] getExpectedKeys() {
		TraditionalKey[] expectedKeys = new TraditionalKey[9];
		for (int i : range(expectedKeys)) {
			expectedKeys[i] = new TraditionalKey(2, Mode.values()[i]);
		}
		return expectedKeys;
	}
}
