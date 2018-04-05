package com.xenoage.zong.core.music

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.utils.math._0
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.*
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.chord.Grace
import com.xenoage.zong.core.music.chord.Note
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.music.util.FirstOrLast
import com.xenoage.zong.core.music.util.FirstOrLast.First
import com.xenoage.zong.core.music.util.FirstOrLast.Last
import com.xenoage.zong.core.music.util.Interval
import com.xenoage.zong.core.music.util.Interval.*
import com.xenoage.zong.core.music.util.StartOrStop
import com.xenoage.zong.core.music.util.StartOrStop.Start
import com.xenoage.zong.core.music.util.StartOrStop.Stop
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 * Tests for [Voice].
 */
class VoiceTest {

	@Test
	fun getElementTest() {
		//our test example: (g: grace note)
		//Beats:       0        1     2           3     4
		//Elements     |g1|-----a-----|g2|g3|--b--|--c--|g4|
		//Checked:     x  x           x  x  x     x     x  x
		val n: Rest? = null
		val a = Rest(fr(2))
		val b = Rest(fr(1))
		val c = Rest(fr(1))
		val g1 = grace(D)
		val g2 = grace(E)
		val g3 = grace(F)
		val g4 = grace(G)
		val elementsPool = listOf(a, b, c, g1, g2, g3, g4)
		val voice = Voice()
		voice.elements.addAll(listOf(g1, a, g2, g3, b, c, g4))
		//expected solutions
		val sideCount = FirstOrLast.values().size
		val borderCount = StartOrStop.values().size
		val inCount = Interval.values().size
		val expected = Array(sideCount) {
			Array(borderCount) { Array(inCount) { emptyList<VoiceElement?>() } } }
		//first/start
		expected[o(First)][o(Start)][o(Before)] = listOf(n, g1, g1, g1, g1, g1, g1, g1)
		expected[o(First)][o(Start)][o(BeforeOrAt)] = listOf(g1, g1, g1, g1, g1, g1, g1, g1)
		expected[o(First)][o(Start)][o(At)] = listOf(g1, a, g2, g3, b, c, g4, n)
		expected[o(First)][o(Start)][o(AtOrAfter)] = listOf(g1, a, g2, g3, b, c, g4, n)
		expected[o(First)][o(Start)][o(After)] = listOf(a, g2, g3, b, c, g4, n, n)
		//first/stop
		expected[o(First)][o(Stop)][o(Before)] = listOf(n, n, g1, g1, g1, g1, g1, g1)
		expected[o(First)][o(Stop)][o(BeforeOrAt)] = listOf(n, g1, g1, g1, g1, g1, g1, g1)
		expected[o(First)][o(Stop)][o(At)] = listOf(n, g1, a, g2, g3, b, c, g4)
		expected[o(First)][o(Stop)][o(AtOrAfter)] = listOf(g1, g1, a, g2, g3, b, c, g4)
		expected[o(First)][o(Stop)][o(After)] = listOf(g1, a, g2, g3, b, c, g4, n)
		//last/start
		expected[o(Last)][o(Start)][o(Before)] = listOf(n, g1, a, g2, g3, b, c, g4)
		expected[o(Last)][o(Start)][o(BeforeOrAt)] = listOf(g1, a, g2, g3, b, c, g4, g4)
		expected[o(Last)][o(Start)][o(At)] = listOf(g1, a, g2, g3, b, c, g4, n)
		expected[o(Last)][o(Start)][o(AtOrAfter)] = listOf(g4, g4, g4, g4, g4, g4, g4, n)
		expected[o(Last)][o(Start)][o(After)] = listOf(g4, g4, g4, g4, g4, g4, n, n)
		//last/stop
		expected[o(Last)][o(Stop)][o(Before)] = listOf(n, n, g1, a, g2, g3, b, c)
		expected[o(Last)][o(Stop)][o(BeforeOrAt)] = listOf(n, g1, a, g2, g3, b, c, g4)
		expected[o(Last)][o(Stop)][o(At)] = listOf(n, g1, a, g2, g3, b, c, g4)
		expected[o(Last)][o(Stop)][o(AtOrAfter)] = listOf(g4, g4, g4, g4, g4, g4, g4, g4)
		expected[o(Last)][o(Stop)][o(After)] = listOf(g4, g4, g4, g4, g4, g4, g4, n)
		//test
		for (iSide in 0 until sideCount) {
			for (iBorder in 0 until borderCount) {
				for (iIn in 0 until inCount) {
					assertGetElement(voice, elementsPool, FirstOrLast.values()[iSide], StartOrStop.values()[iBorder],
							Interval.values()[iIn], expected[iSide][iBorder][iIn])
				}
			}
		}
	}


	private fun assertGetElement(voice: Voice, elementsPool: List<VoiceElement>,
	                             side: FirstOrLast, border: StartOrStop, interval: Interval,
	                             expected: List<VoiceElement?>) {
		for (i in voice.elements.indices) {
			val p = voice.getElement(side, border, interval, i)
			val pe = p?.element
			assertEquals(expected[i], pe, "Expected: " + getElementName(elementsPool.indexOf(expected[i])) + ", but was " +
					getElementName(elementsPool.indexOf(pe)) + " for " + side + "/" + border + "/" +
					interval + " at index " + i + ".")
		}
	}
	
	@Test
	fun getElementIndexTest() {
		//our test example: (g: grace note)
		//Beats:       0        1     2           3     4    5
		//Elements     |g1|-----a-----|g2|g3|--b--|--c--|g4|
		//Indices      0  1           2  3  4     5     6  7
		//Checked:     x        x     x           x     x    x
		val a = Rest(fr(2))
		val b = Rest(fr(1))
		val c = Rest(fr(1))
		val g1 = grace(D)
		val g2 = grace(E)
		val g3 = grace(F)
		val g4 = grace(G)
		val voice = Voice()
		voice.elements.addAll(listOf(g1, a, g2, g3, b, c, g4))
		assertEquals(1, voice.getElementIndex(fr(0)))
		assertEquals(1, voice.getElementIndex(fr(1)))
		assertEquals(4, voice.getElementIndex(fr(2)))
		assertEquals(5, voice.getElementIndex(fr(3)))
		assertEquals(7, voice.getElementIndex(fr(4)))
		assertEquals(7, voice.getElementIndex(fr(5)))
	}

	private fun o(e: FirstOrLast): Int = e.ordinal
	private fun o(e: StartOrStop): Int = e.ordinal
	private fun o(e: Interval): Int = e.ordinal

	private fun getElementName(index: Int): String {
		when (index) {
			0 -> return "a"
			1 -> return "b"
			2 -> return "c"
			3 -> return "g1"
			4 -> return "g2"
			5 -> return "g3"
			6 -> return "g4"
		}
		return "n"
	}

	@Test
	fun getElementAtTest() {
		//our test example: (g: grace note)
		//Beats:       0        1     2           3     4
		//Elements     |g1|-----a-----|g2|g3|--b--|--c--|g4|
		//Checked:     x  x           x  x  x     x     x  x
		val a = Rest(fr(2))
		val b = Rest(fr(1))
		val c = Rest(fr(1))
		val g1 = grace(D)
		val g2 = grace(E)
		val g3 = grace(F)
		val g4 = grace(G)
		val voice = Voice()
		voice.elements.addAll(listOf(g1, a, g2, g3, b, c, g4))
		//test
		assertEquals(a, voice.getElementAt(fr(0)))
		assertEquals(null, voice.getElementAt(fr(1)))
		assertEquals(b, voice.getElementAt(fr(2)))
		assertEquals(c, voice.getElementAt(fr(3)))
		assertEquals(g4, voice.getElementAt(fr(4)))
	}

	private fun grace(step: Step): Chord {
		val chord = Chord(_0, Note(pi(step, 0)))
		chord.grace = Grace(true, fr(1, 16))
		return chord
	}

}
