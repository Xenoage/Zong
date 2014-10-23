package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.Utils.checkDurations;
import musicxmltestsuite.tests.base.Base23a;
import musicxmltestsuite.tests.utils.ToDo;

import org.junit.Test;

public class Test23a
	implements Base23a, MusicXmlInTest {

	@ToDo("add support for tuplet notation and test it")
	
	@Test public void test() {
		checkDurations(getFirstStaff(), expectedDurations);
	}

}
