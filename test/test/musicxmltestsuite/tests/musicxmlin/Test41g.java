package musicxmltestsuite.tests.musicxmlin;

import musicxmltestsuite.tests.base.Base41g;

import org.junit.Test;

import com.xenoage.utils.exceptions.InvalidFormatException;

/**
 * ID for part is required by MusicXML schema.
 * We do not support inconsistent MusicXML files. Loading should fail.
 * 
 * @author Andreas Wenger
 */
public class Test41g
	implements Base41g, MusicXmlInTest {
	
	@Test public void test() {
		assertLoadScoreFailure(InvalidFormatException.class);
	}
	
}
