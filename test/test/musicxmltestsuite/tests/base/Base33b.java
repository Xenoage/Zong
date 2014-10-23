package musicxmltestsuite.tests.base;


/**
 * Two simple tied whole notes.
 * 
 * @author Andreas Wenger
 */
public interface Base33b
	extends Base {

	@Override default String getFileName() {
		return "33b-Spanners-Tie.xml";
	}
	
}
