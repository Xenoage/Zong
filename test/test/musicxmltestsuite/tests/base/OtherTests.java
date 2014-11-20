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
		"01e-Pitches-ParenthesizedAccidentals.xml",
		"01f-Pitches-ParenthesizedMicrotoneAccidentals.xml",
		"02b-Rests-PitchedRests.xml",
		"02c-Rests-MultiMeasureRests.xml",
		"03d-Rhythm-DottedDurations-Factors.xml",
		"11c-TimeSignatures-CompoundSimple.xml",
		"11d-TimeSignatures-CompoundMultiple.xml",
		"11e-TimeSignatures-CompoundMixed.xml",
		"11g-TimeSignatures-SingleNumber.xml",
		"13c-KeySignatures-NonTraditional.xml",
		"14a-StaffDetails-LineChanges.xml",
		"22a-Noteheads.xml",
		"22b-Staff-Notestyles.xml",
		"22c-Noteheads-Chords.xml",
		"22d-Parenthesized-Noteheads.xml",
		"23b-Tuplets-Styles.xml",
		"23c-Tuplet-Display-NonStandard.xml",
		"23d-Tuplets-Nested.xml",
		"23e-Tuplets-Tremolo.xml",
		"32d-Arpeggio.xml",
		"33a-Spanners.xml",
		"33d-Spanners-OctaveShifts.xml",
		"33e-Spanners-OctaveShifts-InvalidSize.xml",
		"33f-Trill-EndingOnGraceNote.xml",
		"33h-Spanners-Glissando.xml",
		"41i-PartNameDisplay-Override.xml",
		"43b-MultiStaff-DifferentKeys.xml",
		"43c-MultiStaff-DifferentKeysAfterBackup.xml",
		"43d-MultiStaff-StaffChange.xml",
		"46g-PickupMeasure-Chordnames-FiguredBass.xml",
		"61g-Lyrics-NameNumber.xml",
		"61j-Lyrics-Elisions.xml",
		"61k-Lyrics-SpannersExtenders.xml",
		"71a-Chordnames.xml",
		"71c-ChordsFrets.xml",
		"71d-ChordsFrets-Multistaff.xml",
		"71e-TabStaves.xml",
		"71f-AllChordTypes.xml",
		"71g-MultipleChordnames.xml",
		"73a-Percussion.xml",
		"74a-FiguredBass.xml",
		"75a-AccordionRegistrations.xml",
	};
	
	public static final String[] supportedButTestUnneeded = {
		"02e-Rests-NoType.xml", //we ignore the type attribute anyway
	};
	
	public static final String[] supportedButNotTestedYet = {
	};
	
	public static final String[] supportedButTestLater = {
		"51d-EmptyTitle.xml", //add layout test
		"52a-PageLayout.xml", //add layout test
		
	};
		
	public static final String[] unsupported = {
		"01d-Pitches-Microtones.xml", //microtones are not supported
		"11f-TimeSignatures-SymbolMeaning.xml", //3/8 time with cut symbol is not supported
		"13d-KeySignatures-Microtones.xml", //microtones are not supported
		"99a-Sibelius5-IgnoreBeaming.xml", //irrelevant
		"99b-Lyrics-BeamsMelismata-IgnoreBeams.xml", //irrelevant
	};

}
