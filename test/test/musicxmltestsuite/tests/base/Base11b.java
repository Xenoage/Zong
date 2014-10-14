package musicxmltestsuite.tests.base;


/**
 * A score without a time signature (but with a key and clefs).
 * 
 * @author Andreas Wenger
 */
public interface Base11b
	extends Base {

	@Override default String getFileName() {
		return "11b-TimeSignatures-NoTime.xml";
	}

}
