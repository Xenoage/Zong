package musicxmltestsuite.tests.base;


/**
 * Tests based on the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public interface Base {
	
	default String getDirPath() {
		return "data/test/scores/MusicXML-TestSuite-0.1/";
	}
	
	String getFileName();
	
	String getProjectName();

}
