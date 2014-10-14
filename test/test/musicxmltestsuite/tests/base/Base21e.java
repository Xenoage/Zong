package musicxmltestsuite.tests.base;


/**
 * Check for proper chord detection after a pickup measure (i.e. the first beat of the measure is
 * not aligned with multiples of the time signature)! 
 * 
 * @author Andreas Wenger
 */
public interface Base21e
	extends Base {

	@Override default String getFileName() {
		return "21e-Chords-PickupMeasures.xml";
	}
	
}
