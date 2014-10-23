package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.Utils.checkGraceChords;
import musicxmltestsuite.tests.base.Base24b;

import org.junit.Test;


public class Test24b
	implements Base24b, MusicXmlInTest {

	@Test public void test() {
		checkGraceChords(getFirstStaff(), expectedChords);
	}

}
