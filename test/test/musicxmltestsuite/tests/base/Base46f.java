package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;


public interface Base46f
	extends Base {

	@Override default String getFileName() {
		return "46f-IncompleteMeasures.xml";
	}
	
	Fraction[] expectedMeasureFilledBeats = { Companion.fr(2,4), Companion.fr(4,4), Companion.fr(2,4), Companion.fr(4,4) };

}
