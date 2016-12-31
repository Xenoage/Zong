package musicxmltestsuite.tests.base;


/**
 * Tests based on the
 * <a href="http://lilypond.org/doc/v2.18/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public interface Base {
	
	String onlinePath = "http://www.lilypond.org/doc/v2.18/input/regression/musicxml/collated-files.html";
	String dirPath = "data/test/scores/MusicXML-TestSuite-0.1/";
	
	/** The filename of the MusicXML test suite test, like "01a-Pitches-Pitches.xml". */
	String getFileName();
	
	/** The Zong! subproject which is tested with this test, like "musicxml" or "layout". */
	default String getProjectName() {
		return "";
	}

}
