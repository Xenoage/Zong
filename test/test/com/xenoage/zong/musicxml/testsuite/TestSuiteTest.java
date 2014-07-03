package com.xenoage.zong.musicxml.testsuite;

/**
 * Base class for the tests for the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public abstract class TestSuiteTest<T> {

	/**
	 * Gets the filename of the test, e.g. "24b-ChordAsGraceNote.xml".
	 */
	public abstract String getFile();

}
