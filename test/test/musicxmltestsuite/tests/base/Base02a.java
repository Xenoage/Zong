package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;

/**
 * All different rest lengths: A two-bar multi-measure rest, a whole rest,
 * a half, etc. until a 128th-rest; Then the same with dotted durations.  
 * 
 * @author Andreas Wenger
 */
public interface Base02a
	extends Base {

	@Override default String getFileName() {
		return "02a-Rests-Durations.xml";
	}
	
	Fraction[] expectedDurations = {
		//undotted
		fr(1), fr(1), fr(1), fr(1, 2), fr(1, 4), fr(1, 8), fr(1, 16), fr(1, 32), fr(1, 64),
		fr(1, 128), fr(1, 128),
		//dotted
		fr(3, 4), fr(1, 4), fr(1, 4), fr(3, 8), fr(3, 16), fr(3, 32), fr(3, 64), fr(3, 128),
		fr(3, 256), fr(3, 256) };

}
