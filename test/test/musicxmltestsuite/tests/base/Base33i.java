package musicxmltestsuite.tests.base;


/**
 * Several ties that have their end tag missing. 
 * 
 * @author Andreas Wenger
 */
public interface Base33i
	extends Base {

	@Override default String getFileName() {
		return "33i-Ties-NotEnded.xml";
	}
	
}
