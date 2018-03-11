package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;


public interface Base23a
	extends Base {

	@Override default String getFileName() {
		return "23a-Tuplets.xml";
	}

	Fraction[] expectedDurations = { Companion.fr(1, 6), Companion.fr(1, 6), Companion.fr(1, 6), Companion.fr(1, 6), Companion.fr(1, 6), Companion.fr(1, 6),
		Companion.fr(1, 6), Companion.fr(1, 6), Companion.fr(1, 6), Companion.fr(1, 8), Companion.fr(1, 8), Companion.fr(1, 8), Companion.fr(1, 8), Companion.fr(1, 16), Companion.fr(1, 16),
		Companion.fr(1, 16), Companion.fr(1, 16), Companion.fr(3, 28), Companion.fr(3, 28), Companion.fr(3, 28), Companion.fr(3, 28), Companion.fr(3, 28), Companion.fr(3, 28),
		Companion.fr(3, 28), Companion.fr(1, 12), Companion.fr(1, 12), Companion.fr(1, 12), Companion.fr(1, 12), Companion.fr(1, 12), Companion.fr(1, 12) };
}
