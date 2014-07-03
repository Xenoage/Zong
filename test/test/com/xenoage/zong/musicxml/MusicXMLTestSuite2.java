package com.xenoage.zong.musicxml;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.zong.SubclassCollector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.musicxml.testsuite.T24b;
import com.xenoage.zong.musicxml.testsuite.TestSuiteTest;

/**
 * Abstract tests for the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * For specific projects (core, musicxml-in, layout, ...), subclasses of this
 * class can be created.
 * 
 * @param <T> musicxml-in tests may test a {@link MusicXMLDocument}, core tests
 *            may test a {@link Score}, and so on.
 * 
 * @author Andreas Wenger
 */
@RunWith(Parameterized.class)
@AllArgsConstructor
public abstract class MusicXMLTestSuite2<T> {
	
	/**
	 * The available tests.
	 */
	@Parameters(name= "{index}: {0}")
	public static List<TestSuiteTest> getTests()
		throws Exception {
		//collect all subclasses of TestSuiteTest
		List<Class<?>> classes = SubclassCollector.getClassesImplementing(TestSuiteTest.class);
		//implement them
		ArrayList<TestSuiteTest> ret = alist();
		for (Class<?> cls : classes) {
			TestSuiteTest test = (TestSuiteTest) cls.newInstance();
			ret.add(test);
		}
		//sort by test name
		Collections.sort(ret, new Comparator<TestSuiteTest>() {
			@Override public int compare(TestSuiteTest t1, TestSuiteTest t2) {
				return t1.getFile().compareTo(t2.getFile());
			}
		});
		return ret;
  }
	
	private TestSuiteTest test;
	
	/**
	 * Loads the given file or fails in JUnit.
	 */
	public abstract T load(String file);
	
	/**
	 * Runs the current test.
	 */
	@Test public void test() {
		//load file
		T data = load(test.getFile());
		//run the rest
	}
	
	public abstract void test24b(T24b data);
	
	//TODO: 24b-ChordAsGraceNote.xml (30 min) 
	//TODO: 24c-GraceNote-MeasureEnd.xml (30 min) 
	//TODO: 24d-AfterGrace.xml (30 min) 
	
	//TODO: not supported yet: 24e-GraceNote-StaffChange.xml
	
	//TODO: 24f-GraceNote-Slur.xml (30 min) 
	/* //[s]lur chords [s]tart and [e]nd
	  Chord s1s, s1e;
		Chord[] expectedChords = new Chord[] {
			s1s = gr(fr(1, 16), true, pi('D', 0, 5)),
			s1e = ch(fr(1, 4), pi('C', 0, 5)),
		};
		new SlurAdd(sl(s1s, s1e)).execute(); */
	/**
	 * Creates a slur for the given chords (to the first notes).
	 */
	private Slur sl(Chord start, Chord end) {
		return new Slur(SlurType.Slur, new SlurWaypoint(start, 0, null),
			new SlurWaypoint(end, 0, null), null);
	}
	
	//TODO: 31a-Directions.xml (60 min) (partly supported) 
	
	//TODO: not supported yet: 31c-MetronomeMarks.xml
	
	//TODO: 32a-Notations.xml (60 min) (partly supported) 
	//TODO: 32b-Articulations-Texts.xml (30 min) (partly supported) 
	//TODO: 32c-MultipleNotationChildren.xml (30 min) 
	
	//TODO: not supported yet: 32d-Arpeggio.xml
	//TODO: not supported yet: 33a-Spanners.xml
	
	//TODO: 33b-Spanners-Tie.xml (15 min) 
	//TODO: 33c-Spanners-Slurs.xml (30 min)
	
	//TODO: not supported yet: 33d-Spanners-OctaveShifts.xml
	//TODO: not supported yet: 33e-Spanners-OctaveShifts-InvalidSize.xml
	//TODO: not supported yet: 33f-Trill-EndingOnGraceNote.xml
	
	//TODO: 33g-Slur-ChordedNotes.xml (30 min) 
	
	//TODO: not supported yet: 33h-Spanners-Glissando.xml
	
	//TODO: 33i-Ties-NotEnded.xml (45 min) 
	//TODO: 41a-MultiParts-Partorder.xml (30 min) 
	//TODO: 41b-MultiParts-MoreThan10.xml (15 min) 
	//TODO: 41c-StaffGroups.xml (60 min) 
	//TODO: 41d-StaffGroups-Nested.xml (15 min) 
	//TODO: 41e-StaffGroups-InstrumentNames-Linebroken.xml (30 min) 
	//TODO: 41f-StaffGroups-Overlapping.xml (30 min) 
	//TODO: 41g-PartNoId.xml (15 min) 
	//TODO: 41h-TooManyParts.xml (15 min) 
	//TODO: 41i-PartNameDisplay-Override.xml (30 min) 
	//TODO: 42a-MultiVoice-TwoVoicesOnStaff-Lyrics.xml (60 min) 
	//TODO: 42b-MultiVoice-MidMeasureClefChange.xml (60 min) 
	//TODO: 43a-PianoStaff.xml (15 min) 
	
	//TODO: not supported yet: 43b-MultiStaff-DifferentKeys.xml
	//TODO: not supported yet: 43c-MultiStaff-DifferentKeysAfterBackup.xml
	
	//TODO: 43d-MultiStaff-StaffChange.xml (60 min) 
	//TODO: 43e-Multistaff-ClefDynamics.xml (60 min) 
	//TODO: 45a-SimpleRepeat.xml (30 min) 
	//TODO: 45b-RepeatWithAlternatives.xml (30 min) 
	//TODO: 45c-RepeatMultipleTimes.xml (30 min) 
	//TODO: 45d-Repeats-Nested-Alternatives.xml (30 min) 
	//TODO: 45e-Repeats-Nested-Alternatives.xml (45 min) 
	//TODO: 45f-Repeats-InvalidEndings.xml (30 min) 
	//TODO: 45g-Repeats-NotEnded.xml (15 min) 
	//TODO: 46a-Barlines.xml (30 min) 
	//TODO: 46b-MidmeasureBarline.xml (30 min) 
	//TODO: 46c-Midmeasure-Clef.xml (15 min) 
	//TODO: 46d-PickupMeasure-ImplicitMeasures.xml (15 min) 
	//TODO: 46e-PickupMeasure-SecondVoiceStartsLater.xml (15 min) 
	//TODO: 46f-IncompleteMeasures.xml (15 min)
	
	//TODO: not supported yet: 46g-PickupMeasure-Chordnames-FiguredBass.xml
	
	//TODO: 51b-Header-Quotes.xml (15 min) 
	//TODO: 51c-MultipleRights.xml (15 min) 
	//TODO: 51d-EmptyTitle.xml (15 min) 
	//TODO: 52a-PageLayout.xml (30 min) 
	//TODO: 52b-Breaks.xml (15 min) 
	//TODO: 61a-Lyrics.xml (30 min) 
	//TODO: 61b-MultipleLyrics.xml (30 min)
	//TODO: 61c-Lyrics-Pianostaff.xml (30 min) 
	//TODO: 61d-Lyrics-Melisma.xml (30 min) 
	//TODO: 61e-Lyrics-Chords.xml (15 min) 
	//TODO: 61f-Lyrics-GracedNotes.xml (30 min) 
	//TODO: 61g-Lyrics-NameNumber.xml (30 min) 
	//TODO: 61h-Lyrics-BeamsMelismata.xml (30 min) 
	//TODO: 61i-Lyrics-Chords.xml (15 min) 
	//TODO: 61j-Lyrics-Elisions.xml (30 min) 
	//TODO: 61k-Lyrics-SpannersExtenders.xml (30 min)
	
	//TODO: not supported yet: 71a-Chordnames.xml
	//TODO: not supported yet: 71c-ChordsFrets.xml
	//TODO: not supported yet: 71d-ChordsFrets-Multistaff.xml
	//TODO: not supported yet: 71e-TabStaves.xml
	//TODO: not supported yet: 71f-AllChordTypes.xml
	//TODO: not supported yet: 71g-MultipleChordnames.xml
	
	//TODO: 72a-TransposingInstruments.xml (60 min)
	//TODO: 72b-TransposingInstruments-Full.xml (30 min)
	//TODO: 72c-TransposingInstruments-Change.xml (60 min)
	//TODO: 73a-Percussion.xml (60 min)
	
	//TODO: not supported yet: 74a-FiguredBass.xml
	//TODO: not supported yet: 75a-AccordionRegistrations.xml
	
	//TODO: 90a-Compressed-MusicXML.mxl (30 min)
	
	//irrelevant: 99a-Sibelius5-IgnoreBeaming.xml
	//irrelevant: 99b-Lyrics-BeamsMelismata-IgnoreBeams.xml
	

}
