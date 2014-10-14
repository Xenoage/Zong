package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.Utils.checkGraceChords;
import musicxmltestsuite.tests.base.Base24d;

import org.junit.Test;


public class Test24d
	implements Base24d, MusicXmlInTest {

	@Test public void test() {
		checkGraceChords(getFirstStaff(), getExpectedChords());
	}

}
