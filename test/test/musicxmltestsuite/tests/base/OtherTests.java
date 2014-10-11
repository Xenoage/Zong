package musicxmltestsuite.tests.base;

/**
 * List of yet unsupported or unneeded tests.
 * 
 * @author Andreas Wenger
 */
public class OtherTests {
	
	public static final String[] unsupported = {
		"01d-Pitches-Microtones.xml",
		"01e-Pitches-ParenthesizedAccidentals.xml",
		"01f-Pitches-ParenthesizedMicrotoneAccidentals.xml",
		"02b-Rests-PitchedRests.xml",
		"02c-Rests-MultiMeasureRests.xml"
	};
	
	public static final String[] unneeded = {
		"02e-Rests-NoType.xml", //we ignore the type attribute anyway
	};

}
