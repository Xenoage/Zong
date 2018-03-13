package com.xenoage.zong.commands.core.music;

import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.utils.exceptions.MeasureFullException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChords;
import static com.xenoage.zong.core.position.MP.*;
import static org.junit.Assert.*;


/**
 * Tests for {@link VoiceElementWrite}.
 * 
 * @author Andreas Wenger
 */
public class VoiceElementWriteTest {


	/**
	 * Write<pre>
	 *         xxxxxxx
	 * into  aabbccddeeffgghh
	 *  =>   aaxxxxxxx_ffgghh</pre>
	 */
	@Test public void testOverwrite1() {
		Score score = createTestScoreEighths();
		//in voice 1, write a 7/16 rest at 1/8
		MP mp = atElement(0, 0, 1, 1);
		Voice voice = score.getVoice(mp);
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, new Rest(Companion.fr(7, 16)), null);
		cmd.execute();
		//now, there must be the durations 1/8, 7/16, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.getElements().size());
		assertEquals(Companion.fr(1, 8), getDur(voice, 0));
		assertEquals(Companion.fr(7, 16), getDur(voice, 1));
		assertEquals(Companion.fr(1, 8), getDur(voice, 2));
		assertEquals(Companion.fr(1, 8), getDur(voice, 3));
		assertEquals(Companion.fr(1, 8), getDur(voice, 4));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}


	/**
	 * Write<pre>
	 *         xxxxxxxx
	 * into  aabbccddeeffgghh
	 *  =>   aaxxxxxxxxffgghh</pre>
	 */
	@Test public void testOverwrite2() {
		Score score = createTestScoreEighths();
		//in voice 1, write a half rest at 1/8
		MP mp = atElement(0, 0, 1, 1);
		Voice voice = score.getVoice(mp);
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, new Rest(Companion.fr(1, 2)), null);
		cmd.execute();
		//now, there must be the durations 1/8, 1/2, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.getElements().size());
		assertEquals(Companion.fr(1, 8), getDur(voice, 0));
		assertEquals(Companion.fr(1, 2), getDur(voice, 1));
		assertEquals(Companion.fr(1, 8), getDur(voice, 2));
		assertEquals(Companion.fr(1, 8), getDur(voice, 3));
		assertEquals(Companion.fr(1, 8), getDur(voice, 4));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}


	/**
	 * Write<pre>
	 *       xxxxxxxx
	 * into  aabbccddeeffgghh
	 *  =>   xxxxxxxxeeffgghh</pre>
	 */
	@Test public void testOverwrite3() {
		Score score = createTestScoreEighths();
		//in voice 1, write a half rest at 0/8
		MP mp = atElement(0, 0, 1, 0);
		Voice voice = score.getVoice(mp);
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, new Rest(Companion.fr(1, 2)), null);
		cmd.execute();
		//now, there must be the durations 1/2, 1/8, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.getElements().size());
		assertEquals(Companion.fr(1, 2), getDur(voice, 0));
		assertEquals(Companion.fr(1, 8), getDur(voice, 1));
		assertEquals(Companion.fr(1, 8), getDur(voice, 2));
		assertEquals(Companion.fr(1, 8), getDur(voice, 3));
		assertEquals(Companion.fr(1, 8), getDur(voice, 4));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}


	/**
	 * Write<pre>
	 *               xxxxxxxx
	 * into  aabbccddeeffgghh
	 *  =>   aabbccddxxxxxxxx</pre>
	 */
	@Test public void testOverwrite4() {
		Score score = createTestScoreEighths();
		//in voice 1, write a half rest at 4/8
		MP mp = atElement(0, 0, 1, 4);
		Voice voice = score.getVoice(mp);
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, new Rest(Companion.fr(1, 2)), null);
		cmd.execute();
		//now, there must be the durations 1/8, 1/8, 1/8, 1/8, 1/2 in voice 1
		assertEquals(5, voice.getElements().size());
		assertEquals(Companion.fr(1, 8), getDur(voice, 0));
		assertEquals(Companion.fr(1, 8), getDur(voice, 1));
		assertEquals(Companion.fr(1, 8), getDur(voice, 2));
		assertEquals(Companion.fr(1, 8), getDur(voice, 3));
		assertEquals(Companion.fr(1, 2), getDur(voice, 4));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}
	
	
	/**
	 * Write<pre>
	 *       x
	 * into  aabbccddeeffgghh
	 *  =>   x_bbccddeeffgghh</pre>
	 */
	@Test public void testOverwrite5() {
		Score score = createTestScoreEighths();
		//in voice 1, write a 1/16 rest at 4/8
		MP mp = atElement(0, 0, 1, 0);
		Voice voice = score.getVoice(mp);
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, new Rest(Companion.fr(1, 16)), null);
		cmd.execute();
		//now, there must be the durations 1/16, 1/8, 1/8, 1/8, 1/8, 1/8, 1/8, 1/8 in voice 1
		assertEquals(8, voice.getElements().size());
		assertEquals(Companion.fr(1, 16), getDur(voice, 0));
		assertEquals(Companion.fr(1, 8), getDur(voice, 1));
		assertEquals(Companion.fr(1, 8), getDur(voice, 2));
		assertEquals(Companion.fr(1, 8), getDur(voice, 3));
		assertEquals(Companion.fr(1, 8), getDur(voice, 4));
		assertEquals(Companion.fr(1, 8), getDur(voice, 5));
		assertEquals(Companion.fr(1, 8), getDur(voice, 6));
		assertEquals(Companion.fr(1, 8), getDur(voice, 7));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}


	/**
	 * When no collisions happen, elements must stay untouched.
	 */
	@Test public void testElementReferences() {
		Score score = ScoreFactory.create1Staff();
		Voice voice = score.getVoice(atVoice(0, 0, 0));
		Rest rest0 = new Rest(Companion.fr(1, 4));
		new VoiceElementWrite(voice, atElement(0, 0, 0, 0), rest0, null).execute();
		Rest rest1 = new Rest(Companion.fr(1, 4));
		new VoiceElementWrite(voice, atElement(0, 0, 0, 1), rest1, null).execute();
		Rest rest2 = new Rest(Companion.fr(1, 4));
		new VoiceElementWrite(voice, atElement(0, 0, 0, 2), rest2, null).execute();
		assertTrue(rest0 == voice.getElementAt(Companion.fr(0, 4)));
		assertTrue(rest1 == voice.getElementAt(Companion.fr(1, 4)));
		assertTrue(rest2 == voice.getElementAt(Companion.fr(2, 4)));
	}


	/**
	 * Writes grace notes in a voice.
	 * <pre>
	 *           ..
	 * into  aabb__ccddeeffgghh
	 *  =>   aabb..ccddeeffgghh</pre>
	 */
	@Test public void testGraceAdd() {
		Score score = createTestScoreEighths();
		CommandPerformer cp = score.getCommandPerformer();
		//in voice 1, write two grace notes
		Voice voice = score.getVoice(atVoice(0, 0, 1));
		Chord g1 = grace(1), g2 = grace(2);
		cp.execute(new VoiceElementWrite(voice, atElement(0, 0, 1, 2), g1, null));
		cp.execute(new VoiceElementWrite(voice, atElement(0, 0, 1, 3), g2, null));
		//now we must find the other elements unchanged but the grace notes inserted
		assertEquals(10, voice.getElements().size());
		assertEquals(Companion.fr(1, 8), getDur(voice, 0));
		assertEquals(Companion.fr(1, 8), getDur(voice, 1));
		assertEquals(Companion.fr(0), getDur(voice, 2));
		assertEquals(Companion.fr(0), getDur(voice, 3));
		assertEquals(Companion.fr(1, 8), getDur(voice, 4));
		assertEquals(Companion.fr(1, 8), getDur(voice, 5));
		assertEquals(Companion.fr(1, 8), getDur(voice, 6));
		assertEquals(Companion.fr(1, 8), getDur(voice, 7));
		assertEquals(Companion.fr(1, 8), getDur(voice, 8));
		assertEquals(Companion.fr(1, 8), getDur(voice, 9));
		//right elements?
		assertEquals(g1, voice.getElement(2));
		assertEquals(g2, voice.getElement(3));
		//test undo
		cp.undoMultipleSteps(2);
		assertTestScoreEighthsOriginalState(score);
	}


	/**
	 * Writes elements in a voice that contains grace notes.
	 * <pre>
	 *         xxxx
	 * into  ..aaaa...bbbb.cccc..</pre>
	 *  =>   ..aaaa...bbbb.cccc..</pre>
	 */
	@Test public void testGraceInsert1() {
		Score score = createTestScoreGraces();
		//in voice 0, write a 1/4 rest at position 2
		MP mp = atElement(0, 0, 0, 2);
		Voice voice = score.getVoice(mp);
		Rest r = new Rest(Companion.fr(1, 4));
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, r, null);
		cmd.execute();
		//check notes
		assertEquals(11, voice.getElements().size());
		assertEquals(0, getStep(voice, 0)); //here 1st grace note has step 0, 2nd 1, ...
		assertEquals(1, getStep(voice, 1));
		assertEquals(r, voice.getElement(2));
		assertEquals(2, getStep(voice, 3));
		assertEquals(3, getStep(voice, 4));
		assertEquals(4, getStep(voice, 5));
		assertEquals(Companion.fr(1, 4), getDur(voice, 6));
		assertEquals(5, getStep(voice, 7));
		assertEquals(Companion.fr(1, 4), getDur(voice, 8));
		assertEquals(6, getStep(voice, 9));
		assertEquals(0, getStep(voice, 10));
		//test undo
		cmd.undo();
		assertTestScoreGracesOriginalState(score);
		
	}


	/**
	 * Writes elements in a voice that contains grace notes.
	 * <pre>
	 *         xxxx___xxxx
	 * into  ..aaaa...bbbb.cccc..
	 *  =>   ..xxxx___xxxx.cccc..</pre>
	 */
	@Test public void testGraceInsert2() {
		Score score = createTestScoreGraces();
		//in voice 0, write a 1/2 rest at position 2
		MP mp = atElement(0, 0, 0, 2);
		Voice voice = score.getVoice(mp);
		Rest r = new Rest(Companion.fr(1, 2));
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, r, null);
		cmd.execute();
		//check notes
		assertEquals(7, voice.getElements().size());
		assertEquals(0, getStep(voice, 0)); //here 1st grace note has step 0, 2nd 1, ...
		assertEquals(1, getStep(voice, 1));
		assertEquals(r, voice.getElement(2));
		assertEquals(5, getStep(voice, 3));
		assertEquals(Companion.fr(1, 4), getDur(voice, 4));
		assertEquals(6, getStep(voice, 5));
		assertEquals(0, getStep(voice, 6));
		//test undo
		cmd.undo();
		assertTestScoreGracesOriginalState(score);
	}


	/**
	 * Writes elements in a voice that contains grace notes.
	 * <pre>
	 *         xxxx___xxxx_xx
	 * into  ..aaaa...bbbb.cccc..
	 *  =>   ..xxxx___xxxx_xx__..</pre>
	 */
	@Test public void testGraceInsert3() {
		Score score = createTestScoreGraces();
		//in voice 0, write a 5/8 rest at position 2
		MP mp = atElement(0, 0, 0, 2);
		Voice voice = score.getVoice(mp);
		Rest r = new Rest(Companion.fr(5, 8));
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, r, null);
		cmd.execute();
		//check notes
		assertEquals(5, voice.getElements().size());
		assertEquals(0, getStep(voice, 0)); //here 1st grace note has step 0, 2nd 1, ...
		assertEquals(1, getStep(voice, 1));
		assertEquals(r, voice.getElement(2));
		assertEquals(6, getStep(voice, 3));
		assertEquals(0, getStep(voice, 4));
		//test undo
		cmd.undo();
		assertTestScoreGracesOriginalState(score);
	}


	/**
	 * Write at a beat.<pre>
	 *         xxxxxxx
	 * into  aabbccddeeffgghh
	 *  =>   aaxxxxxxx_ffgghh</pre>
	 */
	@Test public void testWriteAtBeat() {
		Score score = createTestScoreEighths();
		//in voice 1, write a 7/16 rest at 1/8
		MP mp = atBeat(0, 0, 0, Companion.fr(1, 8));
		Voice voice = score.getVoice(mp);
		Rest r = new Rest(Companion.fr(7, 16));
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, r, null);
		cmd.execute();
		//now, there must be the durations 1/18, 7/16, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.getElements().size());
		assertEquals(Companion.fr(1, 8), getDur(voice, 0));
		assertEquals(Companion.fr(7, 16), getDur(voice, 1));
		assertEquals(Companion.fr(1, 8), getDur(voice, 2));
		assertEquals(Companion.fr(1, 8), getDur(voice, 3));
		assertEquals(Companion.fr(1, 8), getDur(voice, 4));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}


	/**
	 * Write at a beat after the end of the elements.<pre>
	 *                         xxxx
	 * into  aabbccddeeffgghh
	 *  =>   aabbccddeeffgghhrrxxxx</pre> (r is a rest)
	 */
	@Test public void testWriteAfterEnd() {
		Score score = createTestScoreEighths();
		//in voice 1, write a 1/4 rest at 9/8 (1/8 after the end)
		MP mp = atBeat(0, 0, 0, Companion.fr(9, 8));
		Voice voice = score.getVoice(mp);
		Rest x = new Rest(Companion.fr(1, 4));
		VoiceElementWrite cmd = new VoiceElementWrite(voice, mp, x, null);
		cmd.execute();
		//check elements
		assertEquals(10, voice.getElements().size());
		for (int i : range(8))
			assertEquals(Companion.fr(1, 8), getDur(voice, i));
		assertTrue(voice.getElement(8) instanceof Rest);
		assertEquals(Companion.fr(1, 8), getDur(voice, 8));
		assertEquals(x, voice.getElement(9));
		assertEquals(Companion.fr(1, 4), getDur(voice, 9));
		//test undo
		cmd.undo();
		assertTestScoreEighthsOriginalState(score);
	}
	
	
	/**
	 * Obey the time signature. Fail is written element is too long.
	 */
	@Test public void testTimeAware() {
		Score score = ScoreFactory.create1Staff4Measures();
		score.getHeader().getColumnHeaders().get(1).setTime(new TimeSignature(TimeType.time_3_4));
		//create options
		VoiceElementWrite.Options options = new VoiceElementWrite.Options();
		options.checkTimeSignature = true;
		//measure 0: senza misura: write 100 1/4 chords
		Voice voice = score.getVoice(MP.atVoice(0, 0, 0));
		for (int i : range(100))
			new VoiceElementWrite(voice, atElement(0, 0, 0, i), new Rest(Companion.fr(1, 4)), options).execute();
		//measure 2: must work for 3/4, then fail
		for (int i : range(3))
			new VoiceElementWrite(voice, atElement(0, 2, 0, i), new Rest(Companion.fr(1, 4)), options).execute();
		try {
			new VoiceElementWrite(voice, atElement(0, 2, 0, 3), new Rest(Companion.fr(1, 4)), options).execute();
			fail();
		} catch (MeasureFullException ex) {
		}
	}


	/**
	 * Creates a score with a single staff, a single measure,
	 * two voices with each 8 quarter notes which are beamed.
	 */
	private Score createTestScoreEighths() {
		Score score = ScoreFactory.create1Staff();
		new VoiceAdd(score.getMeasure(atMeasure(0, 0)), 1).execute();
		for (int iVoice : range(2)) {
			Voice voice = score.getVoice(atVoice(0, 0, iVoice));
			List<Chord> beamChords = new ArrayList<>();
			for (int i = 0; i < 8; i++) {
				Chord chord = new Chord(new Note(pi(0, 0, 4)), Companion.fr(1, 8));
				//add elements by hand, since the corresonding command is tested itself in this class
				chord.setParent(voice);
				voice.getElements().add(chord);
				beamChords.add(chord);
			}
			//create beam
			Companion.beamFromChords(beamChords);
		}
		//ensure that assert method works correctly. if not, fail now
		assertTestScoreEighthsOriginalState(score);
		return score;
	}
	
	
	/**
	 * Asserts that the score created by {@link #createTestScoreEighths()} is in its original
	 * state again. Useful after undo.
	 */
	private void assertTestScoreEighthsOriginalState(Score score) {
		for (int iVoice : range(2)) {
			Voice voice = score.getVoice(atVoice(0, 0, iVoice));
			assertEquals(8, voice.getElements().size());
			for (int i : range(8))
				assertEquals(Companion.fr(1, 8), getDur(voice, i));
		}
	}
	
	
	/**
	 * Creates a score with a single staff, one measure and one voice:
	 * <pre>..aaaa...bbbb.cccc..</pre> (. are grace notes, a-c are rests)
	 */
	private Score createTestScoreGraces()
	{
		Score score = ScoreFactory.create1Staff();
		Voice voice = score.getVoice(atVoice(0, 0, 0));
		voice.addElement(grace(0));
		voice.addElement(grace(1));
		voice.addElement(new Rest(Companion.fr(1, 4)));
		voice.addElement(grace(2));
		voice.addElement(grace(3));
		voice.addElement(grace(4));
		voice.addElement(new Rest(Companion.fr(1, 4)));
		voice.addElement(grace(5));
		voice.addElement(new Rest(Companion.fr(1, 4)));
		voice.addElement(grace(6));
		voice.addElement(grace(0));
		//ensure that assert method works correctly. if not, fail now
		assertTestScoreGracesOriginalState(score);
		return score;
	}
	
	
	/**
	 * Asserts that the score created by {@link #createTestScoreGraces()} is in its original
	 * state again. Useful after undo.
	 */
	private void assertTestScoreGracesOriginalState(Score score) {
		Voice voice = score.getVoice(atVoice(0, 0, 0));
		assertEquals(11, voice.getElements().size());
		assertEquals(0, getStep(voice, 0));
		assertEquals(1, getStep(voice, 1));
		assertEquals(Companion.fr(1, 4), getDur(voice, 2));
		assertEquals(2, getStep(voice, 3));
		assertEquals(3, getStep(voice, 4));
		assertEquals(4, getStep(voice, 5));
		assertEquals(Companion.fr(1, 4), getDur(voice, 6));
		assertEquals(5, getStep(voice, 7));
		assertEquals(Companion.fr(1, 4), getDur(voice, 8));
		assertEquals(6, getStep(voice, 9));
		assertEquals(0, getStep(voice, 10));
	}
	
	
	private Fraction getDur(Voice voice, int elementIndex) {
		return voice.getElement(elementIndex).getDuration();
	}
	
	
	private int getStep(Voice voice, int elementIndex) {
		return ((Chord)voice.getElement(elementIndex)).getNotes().get(0).getPitch().getStep();
	}
	
	
	private Chord grace(int step) {
		Chord chord = new Chord(new Note(pi(step, 0)), Companion.fr(0, 4));
		chord.setGrace(new Grace(true, Companion.fr(1, 16)));
		return chord;
	}

}
