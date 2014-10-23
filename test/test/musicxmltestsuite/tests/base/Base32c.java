package musicxmltestsuite.tests.base;


/**
 * It should not make any difference whether two articulations are given
 * inside two different notation elements, inside two different articulations
 * children of the same notation element or inside the same articulations
 * element. Thus, all three notes should have a staccato and an accent.  
 * 
 * @author Andreas Wenger
 */
public interface Base32c
	extends Base {

	@Override default String getFileName() {
		return "32c-MultipleNotationChildren.xml";
	}
	
}
