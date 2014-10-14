package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base11b;

import org.junit.Test;


public class Test11b
	implements Base11b, MusicXmlInTest {
	
	@Test public void test() {
		getScore(); //just fine when it loads
	}

}
