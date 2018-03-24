package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.Part
import com.xenoage.zong.core.music.Staff
import java.util.*


/**
 * Adds a given part to a score.
 *
 * The staves for the part are either given (the number of measures must
 * match the number of measures in the score) or they are created with
 * default settings and filled with empty measures, according to the number of measures in the score.
 */
class PartAdd(
		private val score: Score,
		private val part: Part,
		private val partIndex: Int,
		private var staves: MutableList<Staff>? = null
) : UndoableCommand() {

	//backup data
	private var staffStartIndex: Int = 0

	init {
		if (staves != null && staves!!.size != part.stavesCount)
			throw IllegalArgumentException("number of staves is not equal")
	}

	override fun execute() {
		//prepare staves
		val measuresCount = score.measuresCount
		if (staves == null) {
			//staves are not given. create them with default settings
			staves = ArrayList(part.stavesCount)
			for (i in 0 until part.stavesCount) {
				val staff = Staff(linesCount = 5)
				staff.parentScore = score
				staff.addEmptyMeasures(measuresCount) //fill with empty measures
				this.staves!!.add(staff)
			}
		} else {
			//check staves and measures
			for (i in staves!!.indices) {
				if (staves!![i].measures.size != measuresCount)
					throw IllegalStateException("number of measures in score and staff $i is not equal")
			}
		}

		//add part
		val stavesList = score.stavesList
		stavesList.parts.add(partIndex, part)

		//add staves
		staffStartIndex = 0
		for (iPart in 0 until partIndex)
			staffStartIndex += stavesList.parts[iPart].stavesCount
		stavesList.staves.addAll(staffStartIndex, staves!!)

		//shift the staff indexes of the groups
		//beginning at the start index by the given number of staves
		for (group in stavesList.bracketGroups)
			group.staves.insert(staffStartIndex, part.stavesCount)
		for (group in stavesList.barlineGroups)
			group.staves.insert(staffStartIndex, part.stavesCount)
	}


	override fun undo() {
		//remove part
		val stavesList = score.stavesList
		stavesList.parts.removeAt(partIndex)
		//remove staves
		for (i in (staffStartIndex + part.stavesCount - 1) downTo staffStartIndex)
			stavesList.staves.removeAt(i)
		//shift staff indexes of the groups. we ignore the boolean return value of .remove
		//because the staves we remove can not cross group borders in this context
		for (group in stavesList.bracketGroups)
			group.staves.remove(staffStartIndex, part.stavesCount)
		for (group in stavesList.barlineGroups)
			group.staves.remove(staffStartIndex, part.stavesCount)
	}

}
