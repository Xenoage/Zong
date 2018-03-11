package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.utils.math.Fraction;

@ToDo("implicit measures not supported yet")
public interface Base46d
	extends Base {

	@Override default String getFileName() {
		return "46d-PickupMeasure-ImplicitMeasures.xml";
	}
	
	Fraction[] expectedMeasureBeats = { Companion.fr(3,8), Companion.fr(2,4), Companion.fr(2,4), Companion.fr(3,4) };
}
