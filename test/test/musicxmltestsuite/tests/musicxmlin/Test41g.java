package musicxmltestsuite.tests.musicxmlin;

import com.xenoage.utils.exceptions.InvalidFormatException;
import musicxmltestsuite.tests.base.Base41g;
import org.junit.Test;

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
