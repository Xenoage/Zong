package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import musicxmltestsuite.ToDo;

import com.xenoage.utils.math.Fraction;

/**
 * Some "triplets" on the end of the first and in the second staff, using only
 * {@literal <time-modification>}, but not explicit tuplet bracket. Thus, the duration of the
 * notes in the second staff should be scaled properly in comparison to staff 1,
 * but no visual indication about the tuplets is given. 
 * 
 * @author Andreas Wenger
 */
@ToDo("add layout test for this case. notes must be properly aligned.")
public interface Base23f
	extends Base {

	@Override default String getFileName() {
		return "23f-Tuplets-DurationButNoBracket.xml";
	}
	
	Fraction[][] expectedDurations = {
		{ //staff 0
			fr(1, 4), fr(1, 4), fr(1, 6), fr(1, 6), fr(1, 6)
		},
		{ //staff 1
			fr(1, 8), fr(1, 8), fr(1, 12), fr(1, 12), fr(1, 12),
			fr(1, 16), fr(1, 16), fr(1, 16), fr(1, 16),
			fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24)
		}
	};

}
