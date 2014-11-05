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
		fr(4), fr(2), fr(1), fr(1, 2), fr(1, 4), fr(1, 8), fr(1, 16), fr(1, 32), fr(1, 64),
		fr(1, 128),
		fr(1, 128),
		//single dotted
		fr(6), fr(3), fr(3, 2), fr(3, 4), fr(3, 8), fr(3, 16), fr(3, 32), fr(3, 64), fr(3, 128),
		fr(3, 256), fr(3, 256),
		//double dotted
		fr(7), fr(7, 2), fr(7, 4), fr(7, 8), fr(7, 16), fr(7, 32), fr(7, 64), fr(7, 128), fr(7, 256),
		fr(7, 256) };

}
