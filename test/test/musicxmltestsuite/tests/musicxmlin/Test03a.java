package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.Utils.checkDurations;
import musicxmltestsuite.ToDo;
import musicxmltestsuite.tests.base.Base03a;

import org.junit.Test;


public class Test03a
	implements Base03a, MusicXmlInTest {
	
	@ToDo("multiple-rest not yet supported yet")
	
	@Test public void test() {
		checkDurations(getFirstStaff(), expectedDurations);
	}

}
