package musicxmltestsuite.tests.base;

/**
 * Between the individual notes of a chord there can be direction or harmony elements,
	 * which should be properly assigned to the chord (or the position of the chord).
 * 
 * @author Andreas Wenger
 */
public interface Base21f
	extends Base {

	@Override default String getFileName() {
		return "21f-Chord-ElementInBetween.xml";
	}

}
