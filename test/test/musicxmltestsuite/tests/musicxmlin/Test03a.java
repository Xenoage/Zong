package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.Utils.checkDurations;
import musicxmltestsuite.tests.base.Base03a;
import musicxmltestsuite.tests.utils.ToDo;

import org.junit.Test;


public class Test03a
	implements Base03a, MusicXmlInTest {
	
	@ToDo("multiple-rest not yet supported yet")
	
	@Test public void test() {
		checkDurations(getFirstStaff(), expectedDurations);
	}

}
