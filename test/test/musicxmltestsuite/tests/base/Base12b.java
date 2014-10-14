package musicxmltestsuite.tests.base;

/**
 * A score without any key or clef defined. The default (4/4 in treble clef) should be used. 
 * 
 * @author Andreas Wenger
 */
public interface Base12b
	extends Base {

	@Override default String getFileName() {
		return "12b-Clefs-NoKeyOrClef.xml";
	}

}
