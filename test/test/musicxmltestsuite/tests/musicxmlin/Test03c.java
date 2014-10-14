package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base03c;

import org.junit.Test;

public class Test03c
	implements Base03c, MusicXmlInTest {

	@Test public void test() {
		checkDurations(getFirstStaff(), expectedDurations);
	}

}
