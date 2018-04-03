package material.beam.fragments

import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.C
import com.xenoage.zong.core.music.Step.F
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.chord.Chord

/**
 * These test examples are from Chlapik, page 45, rule 6.
 */
class ChlapikBeamFragments {

	fun exampleRow1Col1() = Beam(listOf(
			chordC(fr(1, 16)),
			chordC(fr(1, 8)),
			chordC(fr(1, 16))))

	fun exampleRow1Col2() = Beam(listOf(
			chordF(fr(1, 16)),
			chordF(fr(1, 8)),
			chordF(fr(1, 16))))

	fun exampleRow1Col3() = Beam(listOf(
			chordC(fr(1, 16)),
			chordC(fr(1, 8)),
			chordC(fr(1, 8)),
			chordC(fr(1, 16))))


	fun exampleRow1Col4() = Beam(listOf(
			chordF(fr(1, 16)),
			chordF(fr(1, 8)),
			chordF(fr(1, 8)),
			chordF(fr(1, 16))))

	fun exampleRow2Col1() = Beam(listOf(
			chordC(fr(3, 16)),
			chordC(fr(1, 16))))

	fun exampleRow2Col2() = Beam(listOf(
			chordF(fr(3, 16)),
			chordF(fr(1, 16))))

	fun exampleRow2Col3() = Beam(listOf(
			chordC(fr(3, 32)),
			chordC(fr(1, 32)),
			chordC(fr(3, 32)),
			chordC(fr(1, 32))))

	fun exampleRow2Col4() = Beam(listOf(
			chordF(fr(3, 32)),
			chordF(fr(1, 32)),
			chordF(fr(3, 32)),
			chordF(fr(1, 32))))

	fun exampleRow2Col5() = Beam(listOf(
			chordC(fr(1, 8)),
			chordC(fr(1, 32)),
			chordC(fr(3, 32))))

	fun exampleRow2Col6() = Beam(listOf(
			chordF(fr(1, 8)),
			chordF(fr(1, 32)),
			chordF(fr(3, 32))))

	fun exampleRow3Col2() = Beam(listOf(
			chordC(fr(1, 16)),
			chordC(fr(1, 8)),
			chordC(fr(1, 16)),
			chordC(fr(1, 8))))

	fun exampleRow3Col4() = Beam(listOf(
			chordC(fr(1, 8)),
			chordC(fr(1, 16)),
			chordC(fr(3, 16))))

	fun exampleRow3Col6() = Beam(listOf(
			chordC(fr(7, 32)),
			chordC(fr(1, 32)),
			chordC(fr(3, 32)),
			chordC(fr(1, 32))))

	private fun chordC(duration: Fraction): Chord =
			Chord(duration, pi(C, 5))

	private fun chordF(duration: Fraction): Chord =
			Chord(duration, pi(F, 4))

}
