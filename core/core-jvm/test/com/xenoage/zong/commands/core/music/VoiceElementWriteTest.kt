package com.xenoage.zong.commands.core.music

import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.commands.core.music.beam.BeamAdd
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step
import com.xenoage.zong.core.music.Step.*
import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.beam.BeamWaypoint
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.chord.Grace
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.time.TimeType
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.position.MP.Companion.atVoice
import com.xenoage.zong.core.score1Staff
import com.xenoage.zong.core.score1Staff4Measures
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue


/**
 * Tests for [VoiceElementWrite].
 */
class VoiceElementWriteTest {


	/**
	 * Write
	 *         xxxxxxx
	 * into  aabbccddeeffgghh
	 * =>    aaxxxxxxx_ffgghh
	 */
	@Test
	fun testOverwrite1() {
		val score = createTestScoreEighths()
		//in voice 1, write a 7/16 rest at 1/8
		val mp = atElement(0, 0, 1, 1)
		val voice = score.getVoice(mp)
		val cmd = VoiceElementWrite(voice, mp, Rest(fr(7, 16)))
		cmd.execute()
		//now, there must be the durations 1/8, 7/16, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.elements.size)
		assertEquals(fr(1, 8), getDur(voice, 0))
		assertEquals(fr(7, 16), getDur(voice, 1))
		assertEquals(fr(1, 8), getDur(voice, 2))
		assertEquals(fr(1, 8), getDur(voice, 3))
		assertEquals(fr(1, 8), getDur(voice, 4))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * Write
	 *         xxxxxxxx
	 * into  aabbccddeeffgghh
	 * =>    aaxxxxxxxxffgghh
	 */
	@Test
	fun testOverwrite2() {
		val score = createTestScoreEighths()
		//in voice 1, write a half rest at 1/8
		val mp = atElement(0, 0, 1, 1)
		val voice = score.getVoice(mp)
		val cmd = VoiceElementWrite(voice, mp, Rest(fr(1, 2)))
		cmd.execute()
		//now, there must be the durations 1/8, 1/2, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.elements.size)
		assertEquals(fr(1, 8), getDur(voice, 0))
		assertEquals(fr(1, 2), getDur(voice, 1))
		assertEquals(fr(1, 8), getDur(voice, 2))
		assertEquals(fr(1, 8), getDur(voice, 3))
		assertEquals(fr(1, 8), getDur(voice, 4))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * Write
	 *       xxxxxxxx
	 * into  aabbccddeeffgghh
	 * =>    xxxxxxxxeeffgghh
	 */
	@Test
	fun testOverwrite3() {
		val score = createTestScoreEighths()
		//in voice 1, write a half rest at 0/8
		val mp = atElement(0, 0, 1, 0)
		val voice = score.getVoice(mp)
		val cmd = VoiceElementWrite(voice, mp, Rest(fr(1, 2)))
		cmd.execute()
		//now, there must be the durations 1/2, 1/8, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.elements.size)
		assertEquals(fr(1, 2), getDur(voice, 0))
		assertEquals(fr(1, 8), getDur(voice, 1))
		assertEquals(fr(1, 8), getDur(voice, 2))
		assertEquals(fr(1, 8), getDur(voice, 3))
		assertEquals(fr(1, 8), getDur(voice, 4))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * Write
	 *               xxxxxxxx
	 * into  aabbccddeeffgghh
	 * =>    aabbccddxxxxxxxx
	 */
	@Test
	fun testOverwrite4() {
		val score = createTestScoreEighths()
		//in voice 1, write a half rest at 4/8
		val mp = atElement(0, 0, 1, 4)
		val voice = score.getVoice(mp)
		val cmd = VoiceElementWrite(voice, mp, Rest(fr(1, 2)))
		cmd.execute()
		//now, there must be the durations 1/8, 1/8, 1/8, 1/8, 1/2 in voice 1
		assertEquals(5, voice.elements.size)
		assertEquals(fr(1, 8), getDur(voice, 0))
		assertEquals(fr(1, 8), getDur(voice, 1))
		assertEquals(fr(1, 8), getDur(voice, 2))
		assertEquals(fr(1, 8), getDur(voice, 3))
		assertEquals(fr(1, 2), getDur(voice, 4))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}


	/**
	 * Write
	 *       x
	 * into  aabbccddeeffgghh
	 * =>    x_bbccddeeffgghh
	 */
	@Test
	fun testOverwrite5() {
		val score = createTestScoreEighths()
		//in voice 1, write a 1/16 rest at 4/8
		val mp = atElement(0, 0, 1, 0)
		val voice = score.getVoice(mp)
		val cmd = VoiceElementWrite(voice, mp, Rest(fr(1, 16)))
		cmd.execute()
		//now, there must be the durations 1/16, 1/8, 1/8, 1/8, 1/8, 1/8, 1/8, 1/8 in voice 1
		assertEquals(8, voice.elements.size)
		assertEquals(fr(1, 16), getDur(voice, 0))
		assertEquals(fr(1, 8), getDur(voice, 1))
		assertEquals(fr(1, 8), getDur(voice, 2))
		assertEquals(fr(1, 8), getDur(voice, 3))
		assertEquals(fr(1, 8), getDur(voice, 4))
		assertEquals(fr(1, 8), getDur(voice, 5))
		assertEquals(fr(1, 8), getDur(voice, 6))
		assertEquals(fr(1, 8), getDur(voice, 7))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * When no collisions happen, elements must stay untouched.
	 */
	@Test
	fun testElementReferences() {
		val score = score1Staff()
		val voice = score.getVoice(atVoice(0, 0, 0))
		val rest0 = Rest(fr(1, 4))
		VoiceElementWrite(voice, atElement(0, 0, 0, 0), rest0).execute()
		val rest1 = Rest(fr(1, 4))
		VoiceElementWrite(voice, atElement(0, 0, 0, 1), rest1).execute()
		val rest2 = Rest(fr(1, 4))
		VoiceElementWrite(voice, atElement(0, 0, 0, 2), rest2).execute()
		assertTrue(rest0 === voice.getElementAt(fr(0, 4)))
		assertTrue(rest1 === voice.getElementAt(fr(1, 4)))
		assertTrue(rest2 === voice.getElementAt(fr(2, 4)))
	}

	/**
	 * Writes grace notes in a voice.
	 *           ..
	 * into  aabb__ccddeeffgghh
	 * =>    aabb..ccddeeffgghh
	 */
	@Test
	fun testGraceAdd() {
		val score = createTestScoreEighths()
		val cp = score.commandPerformer
		//in voice 1, write two grace notes
		val voice = score.getVoice(atVoice(0, 0, 1))
		val g1 = grace(D)
		val g2 = grace(E)
		cp.execute(VoiceElementWrite(voice, atElement(0, 0, 1, 2), g1))
		cp.execute(VoiceElementWrite(voice, atElement(0, 0, 1, 3), g2))
		//now we must find the other elements unchanged but the grace notes inserted
		assertEquals(10, voice.elements.size)
		assertEquals(fr(1, 8), getDur(voice, 0))
		assertEquals(fr(1, 8), getDur(voice, 1))
		assertEquals(fr(0), getDur(voice, 2))
		assertEquals(fr(0), getDur(voice, 3))
		assertEquals(fr(1, 8), getDur(voice, 4))
		assertEquals(fr(1, 8), getDur(voice, 5))
		assertEquals(fr(1, 8), getDur(voice, 6))
		assertEquals(fr(1, 8), getDur(voice, 7))
		assertEquals(fr(1, 8), getDur(voice, 8))
		assertEquals(fr(1, 8), getDur(voice, 9))
		//right elements?
		assertEquals(g1, voice.getElement(2))
		assertEquals(g2, voice.getElement(3))
		//test undo
		cp.undoMultipleSteps(2)
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * Writes elements in a voice that contains grace notes.
	 *         xxxx
	 * into  ..aaaa...bbbb.cccc..
	 * =>    ..xxxx...bbbb.cccc..
	 */
	@Test
	fun testGraceInsert1() {
		val score = createTestScoreGraces()
		//in voice 0, write a 1/4 rest at position 2
		val mp = atElement(0, 0, 0, 2)
		val voice = score.getVoice(mp)
		val r = Rest(fr(1, 4))
		val cmd = VoiceElementWrite(voice, mp, r)
		cmd.execute()
		//check notes
		assertEquals(11, voice.elements.size)
		assertEquals(0, getStep(voice, 0)) //here 1st grace note has step 0, 2nd 1, ...
		assertEquals(1, getStep(voice, 1))
		assertEquals(r, voice.getElement(2))
		assertEquals(2, getStep(voice, 3))
		assertEquals(3, getStep(voice, 4))
		assertEquals(4, getStep(voice, 5))
		assertEquals(fr(1, 4), getDur(voice, 6))
		assertEquals(5, getStep(voice, 7))
		assertEquals(fr(1, 4), getDur(voice, 8))
		assertEquals(6, getStep(voice, 9))
		assertEquals(0, getStep(voice, 10))
		//test undo
		cmd.undo()
		assertTestScoreGracesOriginalState(score)
	}

	/**
	 * Writes elements in a voice that contains grace notes.
	 *         xxxx___xxxx
	 * into  ..aaaa...bbbb.cccc..
	 * =>    ..xxxx___xxxx.cccc..
	 */
	@Test
	fun testGraceInsert2() {
		val score = createTestScoreGraces()
		//in voice 0, write a 1/2 rest at position 2
		val mp = atElement(0, 0, 0, 2)
		val voice = score.getVoice(mp)
		val r = Rest(fr(1, 2))
		val cmd = VoiceElementWrite(voice, mp, r)
		cmd.execute()
		//check notes
		assertEquals(7, voice.elements.size)
		assertEquals(0, getStep(voice, 0)) //here 1st grace note has step 0, 2nd 1, ...
		assertEquals(1, getStep(voice, 1))
		assertEquals(r, voice.getElement(2))
		assertEquals(5, getStep(voice, 3))
		assertEquals(fr(1, 4), getDur(voice, 4))
		assertEquals(6, getStep(voice, 5))
		assertEquals(0, getStep(voice, 6))
		//test undo
		cmd.undo()
		assertTestScoreGracesOriginalState(score)
	}

	/**
	 * Writes elements in a voice that contains grace notes.
	 *         xxxx___xxxx_xx
	 * into  ..aaaa...bbbb.cccc..
	 * =>    ..xxxx___xxxx_xx__..
	 */
	@Test
	fun testGraceInsert3() {
		val score = createTestScoreGraces()
		//in voice 0, write a 5/8 rest at position 2
		val mp = atElement(0, 0, 0, 2)
		val voice = score.getVoice(mp)
		val r = Rest(fr(5, 8))
		val cmd = VoiceElementWrite(voice, mp, r)
		cmd.execute()
		//check notes
		assertEquals(5, voice.elements.size)
		assertEquals(0, getStep(voice, 0)) //here 1st grace note has step 0, 2nd 1, ...
		assertEquals(1, getStep(voice, 1))
		assertEquals(r, voice.getElement(2))
		assertEquals(6, getStep(voice, 3))
		assertEquals(0, getStep(voice, 4))
		//test undo
		cmd.undo()
		assertTestScoreGracesOriginalState(score)
	}

	/**
	 * Write at a beat.
	 *         xxxxxxx
	 * into  aabbccddeeffgghh
	 * =>    aaxxxxxxx_ffgghh
	 */
	@Test
	fun testWriteAtBeat() {
		val score = createTestScoreEighths()
		//in voice 1, write a 7/16 rest at 1/8
		val mp = atBeat(0, 0, 0, fr(1, 8))
		val voice = score.getVoice(mp)
		val r = Rest(fr(7, 16))
		val cmd = VoiceElementWrite(voice, mp, r)
		cmd.execute()
		//now, there must be the durations 1/18, 7/16, 1/8, 1/8, 1/8 in voice 1
		assertEquals(5, voice.elements.size)
		assertEquals(fr(1, 8), getDur(voice, 0))
		assertEquals(fr(7, 16), getDur(voice, 1))
		assertEquals(fr(1, 8), getDur(voice, 2))
		assertEquals(fr(1, 8), getDur(voice, 3))
		assertEquals(fr(1, 8), getDur(voice, 4))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * Write at a beat after the end of the elements.<pre>
	 *                         xxxx
	 * into  aabbccddeeffgghh
	 * =>    aabbccddeeffgghhrrxxxx  (r is a rest)
	 */
	@Test
	fun testWriteAfterEnd() {
		val score = createTestScoreEighths()
		//in voice 1, write a 1/4 rest at 9/8 (1/8 after the end)
		val mp = atBeat(0, 0, 0, fr(9, 8))
		val voice = score.getVoice(mp)
		val x = Rest(fr(1, 4))
		val cmd = VoiceElementWrite(voice, mp, x)
		cmd.execute()
		//check elements
		assertEquals(10, voice.elements.size)
		for (i in 0..7)
			assertEquals(fr(1, 8), getDur(voice, i))
		assertTrue(voice.getElement(8) is Rest)
		assertEquals(fr(1, 8), getDur(voice, 8))
		assertEquals(x, voice.getElement(9))
		assertEquals(fr(1, 4), getDur(voice, 9))
		//test undo
		cmd.undo()
		assertTestScoreEighthsOriginalState(score)
	}

	/**
	 * Obey the time signature. Fail is written element is too long.
	 */
	@Test
	fun testTimeAware() {
		val score = score1Staff4Measures()
		score.header.columnHeaders.get(1).setTime(TimeSignature(TimeType.time_3_4))
		//create options
		val options = VoiceElementWrite.Options()
		options.checkTimeSignature = true
		//measure 0: senza misura: write 100 1/4 chords
		val voice = score.getVoice(MP.atVoice(0, 0, 0))
		for (i in 0..99)
			VoiceElementWrite(voice, atElement(0, 0, 0, i), Rest(fr(1, 4)), options).execute()
		//measure 2: must work for 3/4, then fail
		for (i in 0..2)
			VoiceElementWrite(voice, atElement(0, 2, 0, i), Rest(fr(1, 4)), options).execute()
		assertFails { VoiceElementWrite(voice, atElement(0, 2, 0, 3), Rest(fr(1, 4)), options).execute() }
	}

	/**
	 * Creates a score with a single staff, a single measure,
	 * two voices with each 8 quarter notes which are beamed.
	 */
	private fun createTestScoreEighths(): Score {
		val score = score1Staff()
		VoiceAdd(score.getMeasure(atMeasure(0, 0)), 1).execute()
		for (iVoice in 0..1) {
			val voice = score.getVoice(atVoice(0, 0, iVoice))
			val beamChords = ArrayList<Chord>()
			for (i in 0..7) {
				val chord = Chord(fr(1, 8), pi(C, 4))
				//add elements by hand, since the corresonding command is tested itself in this class
				chord.parent = voice
				voice.elements.add(chord)
				beamChords.add(chord)
			}
			//create beam
			BeamAdd(Beam(beamChords.map { BeamWaypoint(it) })).execute()
		}
		//ensure that assert method works correctly. if not, fail now
		assertTestScoreEighthsOriginalState(score)
		return score
	}

	/**
	 * Asserts that the score created by [.createTestScoreEighths] is in its original
	 * state again. Useful after undo.
	 */
	private fun assertTestScoreEighthsOriginalState(score: Score) {
		for (iVoice in 0..1) {
			val voice = score.getVoice(atVoice(0, 0, iVoice))
			assertEquals(8, voice.elements.size)
			for (i in 0..7)
				assertEquals(fr(1, 8), getDur(voice, i))
		}
	}

	/**
	 * Creates a score with a single staff, one measure and one voice:
	 * ..aaaa...bbbb.cccc..
	 * (. are grace notes, a-c are rests)
	 */
	private fun createTestScoreGraces(): Score {
		val score = score1Staff()
		val voice = score.getVoice(atVoice(0, 0, 0))
		voice.addElement(grace(C))
		voice.addElement(grace(D))
		voice.addElement(Rest(fr(1, 4)))
		voice.addElement(grace(E))
		voice.addElement(grace(F))
		voice.addElement(grace(G))
		voice.addElement(Rest(fr(1, 4)))
		voice.addElement(grace(A))
		voice.addElement(Rest(fr(1, 4)))
		voice.addElement(grace(B))
		voice.addElement(grace(C))
		//ensure that assert method works correctly. if not, fail now
		assertTestScoreGracesOriginalState(score)
		return score
	}

	/**
	 * Asserts that the score created by [createTestScoreGraces] is in its original
	 * state again. Useful after undo.
	 */
	private fun assertTestScoreGracesOriginalState(score: Score) {
		val voice = score.getVoice(atVoice(0, 0, 0))
		assertEquals(11, voice.elements.size)
		assertEquals(0, getStep(voice, 0))
		assertEquals(1, getStep(voice, 1))
		assertEquals(fr(1, 4), getDur(voice, 2))
		assertEquals(2, getStep(voice, 3))
		assertEquals(3, getStep(voice, 4))
		assertEquals(4, getStep(voice, 5))
		assertEquals(fr(1, 4), getDur(voice, 6))
		assertEquals(5, getStep(voice, 7))
		assertEquals(fr(1, 4), getDur(voice, 8))
		assertEquals(6, getStep(voice, 9))
		assertEquals(0, getStep(voice, 10))
	}

	private fun getDur(voice: Voice, elementIndex: Int): Fraction =
			voice.getElement(elementIndex).duration

	private fun getStep(voice: Voice, elementIndex: Int): Int =
			(voice.getElement(elementIndex) as Chord).notes[0].pitch.step.ordinal

	private fun grace(step: Step): Chord {
		val chord = Chord(fr(0, 4), pi(step, 0))
		chord.grace = Grace(true, fr(1, 16))
		return chord
	}

}
