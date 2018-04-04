package com.xenoage.zong.core

import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.io.selection.Cursor
import org.junit.Test

import com.xenoage.utils.kernel.Range.range
import com.xenoage.utils.math.Fraction._0
import com.xenoage.utils.math.Fraction.fr
import com.xenoage.zong.core.position.MP.atElement
import com.xenoage.zong.core.position.MP.mp
import org.junit.Assert.*

/**
 * Tests for [VoiceElementIterator].
 *
 * @author Andreas Wenger
 */
class VoiceElementIteratorTest {


	@Test
	fun test() {
		val it = VoiceElementIterator(createTestScore())
		for (staff in range(4)) {
			for (measure in range(4)) {
				if ((staff == 1 || staff == 3) && (measure == 1 || measure == 3)) {
					//expect two voices with each 4 quarter rests
					for (voice in range(2)) {
						for (element in range(4)) {
							assertTrue(it.hasNext())
							val e = it.next()
							assertEquals(mp(staff, measure, voice, Companion.fr(element, 4), element), it.getMp())
							assertEquals(Companion.fr(1, 4), e.duration)
						}
					}
				} else {
					//expect a full rest
					assertTrue(it.hasNext())
					val e = it.next()
					assertEquals(mp(staff, measure, 0, Companion.get_0(), 0), it.getMp())
					assertEquals(Companion.fr(1), e.duration)
				}
			}
		}
		assertFalse(it.hasNext())
	}

	companion object {

		/**
		 * Test score with 4 staves and 4 measures. In staves 1 and 3, the measures 1 and 3 have each
		 * two voices with 4 quarter rests. The other measures have a single voice and
		 * a full rest.
		 */
		fun createTestScore(): Score {
			val score = Score()
			val cursor = Cursor(score, MP.mp0, true)
			for (staff in range(4)) {
				for (measure in range(4)) {
					if ((staff == 1 || staff == 3) && (measure == 1 || measure == 3)) {
						//2 voices with each 4 quarter notes
						for (voice in range(2)) {
							cursor.mp = atElement(staff, measure, voice, 0)
							for (i in 0..3)
								cursor.write(Rest(Companion.fr(1, 4)))
						}
					} else {
						//full rest
						cursor.mp = atElement(staff, measure, 0, 0)
						cursor.write(Rest(Companion.fr(1)))
					}
				}
			}
			return score
		}
	}

}
