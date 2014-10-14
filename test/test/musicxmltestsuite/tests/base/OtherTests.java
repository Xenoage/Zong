package musicxmltestsuite.tests.base;

import java.util.Map;

import musicxmltestsuite.report.TestStatus;

import com.xenoage.utils.collections.CollectionUtils;

/**
 * List of yet unsupported or unneeded tests.
 * 
 * @author Andreas Wenger
 */
public class OtherTests {
	
	public static Map<TestStatus, String[]> getOtherTests() {
		Map<TestStatus, String[]> ret = CollectionUtils.map();
		ret.put(TestStatus.UnsupportedToDo, unsupportedToDo);
		ret.put(TestStatus.SupportedButTestUnneeded, supportedButTestUnneeded);
		ret.put(TestStatus.SupportedButNotTestedYet, supportedButNotTestedYet);
		ret.put(TestStatus.Unsupported, unsupported);
		return ret;
	}
	
	public static final String[] unsupportedToDo = {
		"01d-Pitches-Microtones.xml",
		"01e-Pitches-ParenthesizedAccidentals.xml",
		"01f-Pitches-ParenthesizedMicrotoneAccidentals.xml",
		"02b-Rests-PitchedRests.xml",
		"02c-Rests-MultiMeasureRests.xml",
		"03d-Rhythm-DottedDurations-Factors.xml",
		"11c-TimeSignatures-CompoundSimple.xml",
		"11d-TimeSignatures-CompoundMultiple.xml",
		"11e-TimeSignatures-CompoundMixed.xml",
		"11g-TimeSignatures-SingleNumber.xml",
	};
	
	public static final String[] supportedButTestUnneeded = {
		"02e-Rests-NoType.xml", //we ignore the type attribute anyway
	};
	
	public static final String[] supportedButNotTestedYet = {
	};
		
	public static final String[] unsupported = {
		"11f-TimeSignatures-SymbolMeaning.xml", //3/8 time with cut symbol is not supported
	};

}
