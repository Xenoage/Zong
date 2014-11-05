package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;


public interface Base23a
	extends Base {

	@Override default String getFileName() {
		return "23a-Tuplets.xml";
	}

	Fraction[] expectedDurations = { fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6),
		fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 8), fr(1, 8), fr(1, 8), fr(1, 8), fr(1, 16), fr(1, 16),
		fr(1, 16), fr(1, 16), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28),
		fr(3, 28), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12) };
}
