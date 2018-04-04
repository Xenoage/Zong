package com.xenoage.zong.core

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.utils.math._0
import com.xenoage.zong.commands.core.music.*
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.*
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.clef.ClefType
import com.xenoage.zong.core.music.clef.clefBass
import com.xenoage.zong.core.music.clef.clefTreble
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.key.TraditionalKey.Mode
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.time.TimeType
import com.xenoage.zong.core.music.util.Interval.Before
import com.xenoage.zong.core.music.util.Interval.BeforeOrAt
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.position.MP.Companion.atVoice
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * Tests for [Score].
 */
class ScoreTest {

	@Test
	fun getMeasureBeatsTest() {
		val score = score1Staff4Measures()
		//first measure: no time, 1/4 used
		score.getVoice(atVoice(0, 0, 0)) += Rest(fr(1, 4))
		//second measure: 3/4 time, 2/4 used
		score.getColumnHeader(1).setTime(TimeSignature(TimeType.time_3_4))
		score.getVoice(atVoice(0, 1, 0)) += Rest(fr(1, 4))
		score.getVoice(atVoice(0, 1, 0)) += Rest(fr(1, 4))
		//third measure: still 3/4 time, 1/4 used
		score.getVoice(atVoice(0, 2, 0)) += Rest(fr(1, 4))
		//check
		assertEquals(fr(1, 4), score.getMeasureBeats(0))
		assertEquals(fr(3, 4), score.getMeasureBeats(1))
		assertEquals(fr(3, 4), score.getMeasureBeats(2))
	}

	@Test
	fun getMeasureFilledBeatsTest() {
		//create a little test score, where the first measure only
		//uses 2/4, the second 4/4, the third 0/4, the fourth 3/4.
		val score = score1Staff4Measures()
		score.getVoice(atVoice(0, 0, 0)) += Rest(fr(1, 4))
		score.getVoice(atVoice(0, 0, 0)) += Rest(fr(1, 4))
		score.getVoice(atVoice(0, 1, 0)) += Rest(fr(2, 4))
		score.getVoice(atVoice(0, 1, 0)) += Rest(fr(2, 4))
		score.getVoice(atVoice(0, 3, 0)) += Rest(fr(3, 4))
		//test method
		assertEquals(fr(2, 4), score.getMeasureFilledBeats(0))
		assertEquals(fr(4, 4), score.getMeasureFilledBeats(1))
		assertEquals(fr(0, 4), score.getMeasureFilledBeats(2))
		assertEquals(fr(3, 4), score.getMeasureFilledBeats(3))
	}

	@Test
	fun getKeyTest() {
		val (score, _, keys) = createTestScoreClefsKeys()
		assertTrue(keys[0] !== score.getKey(atBeat(0, 0, 0, fr(0, 4)), Before).element)
		assertTrue(keys[0] === score.getKey(atBeat(0, 0, 0, fr(0, 4)), BeforeOrAt).element)
		assertTrue(keys[0] === score.getKey(atBeat(0, 0, 0, fr(1, 4)), Before).element)
		assertTrue(keys[0] === score.getKey(atBeat(0, 0, 0, fr(1, 4)), BeforeOrAt).element)
		assertTrue(keys[0] === score.getKey(atBeat(0, 0, 0, fr(2, 4)), Before).element)
		assertTrue(keys[1] === score.getKey(atBeat(0, 0, 0, fr(2, 4)), BeforeOrAt).element)
		assertTrue(keys[1] === score.getKey(atBeat(0, 0, 0, fr(3, 4)), Before).element)
		assertTrue(keys[1] === score.getKey(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt).element)
		assertTrue(keys[1] === score.getKey(atBeat(0, 1, 0, fr(1, 4)), Before).element)
		assertTrue(keys[2] === score.getKey(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt).element)
		assertTrue(keys[2] === score.getKey(atBeat(0, 1, 0, fr(2, 4)), Before).element)
	}

	/**
	 * Creates a score with a single staff, two measures,
	 * and a number of clefs, keys and chords.
	 * The score, the list of clefs and the list of keys is returned.
	 */
	private fun createTestScoreClefsKeys(): Triple<Score, List<ClefType>, List<Key>> {
		//create two measures:
		//clef-g, key-Gmaj, C#1/4, clef-f, Db1/4, clef-g, key-Fmaj, Cnat1/4 |
		//C#1/4, key-Cmaj, clef-f, C2/4.
		val score = score1Staff()
		MeasureAdd(score, 1).execute()
		val clefs = mutableListOf<ClefType>()
		val keys = mutableListOf<Key>()
		//measure 0
		var measure = score.getMeasure(atMeasure(0, 0))
		var column = score.getColumnHeader(0)
		MeasureElementWrite(Clef(clefs.a(clefTreble)), measure, fr(0, 4)).execute()
		ColumnElementWrite(keys.a(TraditionalKey(1, Mode.Major)), column, fr(0, 4)).execute()
		VoiceElementWrite(measure.getVoice(0), atElement(0, 0, 0, 0),
				Chord(fr(1, 4), pi(C, 1, 4))).execute()
		MeasureElementWrite(Clef(clefs.a(clefBass)), measure, fr(1, 4)).execute()
		VoiceElementWrite(measure.getVoice(0), atElement(0, 0, 0, 1),
				Chord(fr(1, 4), pi(D, -1, 4))).execute()
		MeasureElementWrite(Clef(clefs.a(clefTreble)), measure, fr(2, 4)).execute()
		ColumnElementWrite(keys.a(TraditionalKey(-1, Mode.Major)), column, fr(2, 4)).execute()
		VoiceElementWrite(measure.getVoice(0), atElement(0, 0, 0, 2),
				Chord(fr(1, 4), pi(C, 0, 4))).execute()
		//measure 1
		measure = score.getMeasure(atMeasure(0, 1))
		column = score.getColumnHeader(1)
		VoiceElementWrite(measure.getVoice(0), atElement(0, 1, 0, 0),
				Chord(fr(1, 4), pi(C, 1, 4))).execute()
		ColumnElementWrite(keys.a(TraditionalKey(0, Mode.Major)), column, fr(1, 4)).execute()
		MeasureElementWrite(Clef(clefs.a(clefBass)), measure, fr(1, 4)).execute()
		VoiceElementWrite(measure.getVoice(0), atElement(0, 1, 0, 1),
				Chord(fr(2, 4), pi(C, 0, 4))).execute()
		return Triple(score, clefs, keys)
	}
	
	@Test
	fun getAccidentalsTest() {
		val score = createTestScoreAccidentals()
		var acc: Map<Pitch, Int>
		//measure 0
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(0, 4)), Before)
		assertEquals(0, acc.size)
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(0, 4)), BeforeOrAt)
		assertEquals(1, acc.size)
		assertEquals(0, acc[pi(3, 0, 4)])
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(1, 4)), Before)
		assertEquals(1, acc.size)
		acc = score.getAccidentals(atBeat(0, 0, 0, fr(1, 4)), BeforeOrAt)
		assertEquals(1, acc.size)
		//measure 1
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(0, 4)), Before)
		assertEquals(0, acc.size)
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt)
		assertEquals(1, acc.size)
		assertEquals(1, acc[pi(3, 0, 4)])
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(1, 4)), Before)
		assertEquals(0, acc.size) //0, because key changed
		acc = score.getAccidentals(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt)
		assertEquals(2, acc.size)
		assertEquals(1, acc[pi(3, 0, 4)])
		assertEquals(-1, acc[pi(6, 0, 4)])
	}

	/**
	 * Creates a score with a single staff, two measures with each two voices,
	 * with key changes and chords with accidentals.
	 */
	private fun createTestScoreAccidentals(): Score {
		//create two measures with two voices:
		//           measure 0    | measure 1
		//beat:      0      1     | 0        1
		//key:       Gmaj         | Fmaj     Cmaj
		//voice 0:   C4           | F#4      Bb4 Bb4
		//voice 1:   Fnat4        | Bb4      F#4 F#4
		val score = score1Staff()
		MeasureAdd(score, 1).execute()
		VoiceAdd(score.getMeasure(atMeasure(0, 0)), 1).execute()
		VoiceAdd(score.getMeasure(atMeasure(0, 1)), 1).execute()
		//keys
		ColumnElementWrite(TraditionalKey(1, Mode.Major), score.getColumnHeader(0), fr(0, 4)).execute()
		ColumnElementWrite(TraditionalKey(-1, Mode.Major), score.getColumnHeader(1), fr(0, 4)).execute()
		ColumnElementWrite(TraditionalKey(0, Mode.Major), score.getColumnHeader(1), fr(1, 4)).execute()
		//measure 0, voice 0
		var voice = score.getVoice(MP.atVoice(0, 0, 0))
		voice += Chord(fr(2, 4), pi(C, 0, 4))
		//measure 1, voice 0
		voice = score.getVoice(MP.atVoice(0, 1, 0))
		voice += Chord(fr(1, 4), pi(F, 1, 4))
		voice += Chord(fr(1, 8), pi(B, -1, 4))
		voice += Chord(fr(1, 8), pi(B, -1, 4))
		//measure 0, voice 1
		voice = score.getVoice(MP.atVoice(0, 0, 1))
		voice += Chord(fr(2, 4), pi(F, 0, 4))
		//measure 1, voice 1
		voice = score.getVoice(MP.atVoice(0, 1, 1))
		voice += Chord(fr(1, 4), pi(B, -1, 4))
		voice += Chord(fr(1, 8), pi(F, 1, 4))
		voice += Chord(fr(1, 8), pi(F, 1, 4))
		return score
	}

	@Test
	fun getClefTest() {
		val (score, clefs) = createTestScoreClefsKeys()
		assertTrue(clefs[0] == score.getClef(atBeat(0, 0, 0, fr(0, 4)), BeforeOrAt))
		assertTrue(clefs[0] == score.getClef(atBeat(0, 0, 0, fr(1, 4)), Before))
		assertTrue(clefs[1] == score.getClef(atBeat(0, 0, 0, fr(1, 4)), BeforeOrAt))
		assertTrue(clefs[1] == score.getClef(atBeat(0, 0, 0, fr(2, 4)), Before))
		assertTrue(clefs[2] == score.getClef(atBeat(0, 0, 0, fr(2, 4)), BeforeOrAt))
		assertTrue(clefs[2] == score.getClef(atBeat(0, 0, 0, fr(3, 4)), Before))
		assertTrue(clefs[2] == score.getClef(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt))
		assertTrue(clefs[2] == score.getClef(atBeat(0, 1, 0, fr(1, 4)), Before))
		assertTrue(clefs[3] == score.getClef(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt))
		assertTrue(clefs[3] == score.getClef(atBeat(0, 1, 0, fr(2, 4)), Before))
	}

	@Test
	fun getMusicContextTest() {
		val (score, clefs, keys) = createTestScoreClefsKeys()
		//measure 1: before 0/4: no accidentals
		var musicContext = score.getMusicContext(atBeat(0, 1, 0, fr(0, 4)), BeforeOrAt, Before)
		assertEquals(0, musicContext.accidentals.size)
		//measure 1: at 1/8: G clef, F key and C#4 accidental
		musicContext = score.getMusicContext(atBeat(0, 1, 0, fr(1, 8)), BeforeOrAt, Before)
		assertEquals(clefs[2], musicContext.clef)
		assertEquals(keys[1], musicContext.key)
		assertEquals(1, musicContext.accidentals.size)
		assertEquals(1, musicContext.accidentals[pi(C, 0, 4)])
		//measure 1: at 1/4: F clef, C key and no accidentals
		musicContext = score.getMusicContext(atBeat(0, 1, 0, fr(1, 4)), BeforeOrAt, Before)
		assertEquals(clefs[3], musicContext.clef)
		assertEquals(keys[2], musicContext.key)
		assertEquals(0, musicContext.accidentals.size)
	}

	@Test
	fun getGapBetweenTest() {
		val score = score1Staff4Measures()
		val e = mutableListOf<VoiceElement>()
		//4/4 measure
		score.getColumnHeader(0).setTime(TimeSignature(TimeType.time_4_4))
		//first measure (filled only with 7/8)
		//  voice 0: 1/4, 1/4, 1/4  (1/4 is missing)
		//  voice 1: 3/4, 1/8       (1/8 is missing)
		score.getVoice(atVoice(0, 0, 0)) += e.a(Rest(fr(1, 4)))
		score.getVoice(atVoice(0, 0, 0)) += e.a(Rest(fr(1, 4)))
		score.getVoice(atVoice(0, 0, 0)) += e.a(Rest(fr(1, 4)))
		VoiceAdd(score.getMeasure(atMeasure(0, 0)), 1).execute()
		score.getVoice(atVoice(0, 0, 1)) += Rest(fr(3, 4))
		score.getVoice(atVoice(0, 0, 1)) += Rest(fr(1, 8))
		//second measure: 1/4, 3/4
		score.getVoice(atVoice(0, 1, 0)) += e.a(Rest(fr(1, 4)))
		score.getVoice(atVoice(0, 1, 0)) += e.a(Rest(fr(3, 4)))
		//third measure: 4/4
		score.getVoice(atVoice(0, 1, 0)) += e.a(Rest(fr(4, 4)))
		//test gaps between adjacent elements
		assertEquals(_0, score.getGapBetween(e[0], e[1]))
		assertEquals(_0, score.getGapBetween(e[1], e[2]))
		assertEquals(fr(1, 8), score.getGapBetween(e[2], e[3])) //1/8 from second voice
		assertEquals(_0, score.getGapBetween(e[3], e[4]))
		assertEquals(_0, score.getGapBetween(e[4], e[5]))
		//test some gaps between non-adjacent elements
		assertEquals(fr(1, 4), score.getGapBetween(e[0], e[2]))
		assertEquals(fr(3, 8), score.getGapBetween(e[1], e[3])) //includes 1/8 from second voice
		assertEquals(fr(3, 8), score.getGapBetween(e[2], e[4])) //includes 1/8 from second voice
		assertEquals(fr(5, 8) + fr(4, 4), score.getGapBetween(e[0], e[5])) //includes 1/8 from second voice
		assertEquals(fr(3, 8) + fr(4, 4), score.getGapBetween(e[1], e[5])) //includes 1/8 from second voice
		//test in reverse direction
		assertEquals(fr(-2, 4), score.getGapBetween(e[1], e[0]))
		assertEquals(fr(-2, 4), score.getGapBetween(e[2], e[1]))
		assertEquals(fr(-5, 8), score.getGapBetween(e[3], e[2])) //includes 1/8 from second voice
		assertEquals(fr(-23, 8), score.getGapBetween(e[5], e[0])) //includes 1/8 from second voice
		assertEquals(fr(-21, 8), score.getGapBetween(e[5], e[1])) //includes 1/8 from second voice
	}

	private fun <T> MutableList<T>.a(element: T): T {
		add(element)
		return element
	}

}
