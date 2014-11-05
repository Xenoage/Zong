package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base41h;

import org.junit.Test;

import com.xenoage.utils.exceptions.InvalidFormatException;

/**
 * The MusicXML documentation implies, that each score-part in the part-list
 * has a corresponding part and vice versa.
 * We do not support inconsistent MusicXML files. Loading should fail.
 * 
 * @author Andreas Wenger
 */
public class Test41h
	implements Base41h, MusicXmlInTest {
	
	@Test public void test() {
		assertLoadScoreFailure(InvalidFormatException.class);
	}
	
}
