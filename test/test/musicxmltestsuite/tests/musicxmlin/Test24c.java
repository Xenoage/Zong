package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.Utils.checkGraceChords;
import musicxmltestsuite.tests.base.Base24c;

import org.junit.Test;


public class Test24c
	implements Base24c, MusicXmlInTest {

	@Test public void test() {
		checkGraceChords(getFirstStaff(), getExpectedChords());
	}

}
