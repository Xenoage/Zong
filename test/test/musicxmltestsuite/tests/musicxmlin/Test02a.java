package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.ToDo;
import musicxmltestsuite.tests.base.Base02a;

import org.junit.Test;


public class Test02a
	implements Base02a, MusicXmlInTest {
	
	@ToDo("multirests are not supported yet")
	
	@Test public void test() {
		checkDurations(getFirstStaff(), expectedDurations);
	}

}
