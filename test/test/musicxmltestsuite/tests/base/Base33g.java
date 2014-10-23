package musicxmltestsuite.tests.base;


/**
 * Slurs on chorded notes: Only the first note of the chord should get the
 * slur notation. Some applications print out the slur for all notes –
 * these should be ignored. 
 * 
 * @author Andreas Wenger
 */
public interface Base33g
	extends Base {

	@Override default String getFileName() {
		return "33g-Slur-ChordedNotes.xml";
	}
	
}
