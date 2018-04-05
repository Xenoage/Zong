package com.xenoage.zong.core.music

import com.xenoage.zong.commands.core.music.PartAdd
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.group.BarlineGroup
import com.xenoage.zong.core.music.group.StavesRange
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [StavesList].
 */
class StavesListTest {

	@Test
	fun addBarlineGroupTest() {
		val stavesList = create5Staves()
		//add barline groups from staves 0 to 2 and 1 to 3. should merge to 0 to 3
		stavesList.addBarlineGroup(StavesRange(0, 2), BarlineGroup.Style.Common)
		stavesList.addBarlineGroup(StavesRange(1, 3), BarlineGroup.Style.Common)
		assertEquals(1, stavesList.barlineGroups.size)
		assertEquals(BarlineGroup(StavesRange(0, 3), BarlineGroup.Style.Common),
				stavesList.barlineGroups[0])
	}

	private fun create5Staves(): StavesList {
		val score = Score()
		repeat(5) { PartAdd(score, Part("part $it"), it).execute() }
		return score.stavesList
	}

}
