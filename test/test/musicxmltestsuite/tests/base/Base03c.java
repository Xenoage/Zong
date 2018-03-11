package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;


public interface Base03c
	extends Base {

	@Override default String getFileName() {
		return "03c-Rhythm-DivisionChange.xml";
	}
	
	Fraction[] expectedDurations = new Fraction[] {
		Companion.fr(1, 4), Companion.fr(1, 4), Companion.fr(1, 4), Companion.fr(1, 4), Companion.fr(1, 2), Companion.fr(1, 2) };
}
