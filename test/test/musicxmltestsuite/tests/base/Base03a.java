package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;


public interface Base03a
	extends Base {

	@Override default String getFileName() {
		return "03a-Rhythm-Durations.xml";
	}
	
	Fraction[] expectedDurations = {
		//undotted
		Companion.fr(4), Companion.fr(2), Companion.fr(1), Companion.fr(1, 2), Companion.fr(1, 4), Companion.fr(1, 8), Companion.fr(1, 16), Companion.fr(1, 32), Companion.fr(1, 64),
		Companion.fr(1, 128),
		Companion.fr(1, 128),
		//single dotted
		Companion.fr(6), Companion.fr(3), Companion.fr(3, 2), Companion.fr(3, 4), Companion.fr(3, 8), Companion.fr(3, 16), Companion.fr(3, 32), Companion.fr(3, 64), Companion.fr(3, 128),
		Companion.fr(3, 256), Companion.fr(3, 256),
		//double dotted
		Companion.fr(7), Companion.fr(7, 2), Companion.fr(7, 4), Companion.fr(7, 8), Companion.fr(7, 16), Companion.fr(7, 32), Companion.fr(7, 64), Companion.fr(7, 128), Companion.fr(7, 256),
		Companion.fr(7, 256) };

}
