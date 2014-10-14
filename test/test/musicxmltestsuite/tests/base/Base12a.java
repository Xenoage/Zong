package musicxmltestsuite.tests.base;


/**
 * Various clefs: G, C, F, percussion, TAB and none; some are also possible
 * with transposition and on other staff lines than their default (e.g.
 * soprano/alto/tenor/baritone C clefs); Each measure shows a different clef
 * (measure 17 has the "none" clef), only measure 18 has the same treble clef
 * as measure 1.
 * 
 * @author Andreas Wenger
 */
public interface Base12a
	extends Base {

	@Override default String getFileName() {
		return "12a-Clefs.xml";
	}
	
}
