package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;

/**
 * Although uncommon, the divisions of a quarter note can change somewhere in the middle of
 * a MusicXML file. Here, the first half measure uses a division of 1, which then changes
 * to 8 in the middle of the first measure and to 38 in the middle of the second measure. 
 * 
 * @author Andreas Wenger
 */
public interface Base03c
	extends Base {

	@Override default String getFileName() {
		return "03c-Rhythm-DivisionChange.xml";
	}
	
	Fraction[] expectedDurations = new Fraction[] {
		fr(1, 4), fr(1, 4), fr(1, 4), fr(1, 4), fr(1, 2), fr(1, 2) };
}
