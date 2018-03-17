package com.xenoage.zong.core.music

import com.xenoage.utils.math.lcm
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.group.BarlineGroup
import com.xenoage.zong.core.music.group.BracketGroup
import com.xenoage.zong.core.music.group.StavesRange
import com.xenoage.zong.core.music.util.Column
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atStaff
import com.xenoage.zong.utils.exceptions.IllegalMPException

/**
 * A [StavesList] manages the staves of a score
 * and all of its parts, and bracket and barline groups.
 */
class StavesList(
		/** The parent score. */
		val score: Score
) {

	/** The list of all staves. */
	val staves = mutableListOf<Staff>()

	/** The list of all parts. */
	val parts = mutableListOf<Part>()

	/** The groups of the staves with barlines.  */
	val barlineGroups = mutableListOf<BarlineGroup>()

	/** The groups of the staves with brackets.  */
	val bracketGroups = mutableListOf<BracketGroup>()

	/** Gets the measure column at the given measure index. */
	fun getColumn(measure: Int): Column =
			staves.map { it.measures[measure] }

	/** Gets the part the given staff belongs to, or null if not found. */
	fun getPartByStaffIndex(staffIndex: Int): Part? {
		var iStaff = 0
		for (part in this.parts) {
			iStaff += part.stavesCount
			if (staffIndex < iStaff)
				return part
		}
		return null
	}

	/** Gets the staves indices of the given part, or null if not found. */
	fun getPartStaffIndices(part: Part): StavesRange? {
		var iStaff = 0
		for (p in this.parts) {
			if (p == part)
				return StavesRange(iStaff, iStaff + part.stavesCount - 1)
			iStaff += p.stavesCount
		}
		return null
	}

	/**
	 * Gets the barline group the given staff belongs to.
	 * If no group is found, null is returned.
	 */
	fun getBarlineGroupByStaff(staffIndex: Int): BarlineGroup? =
		barlineGroups.find { staffIndex in it.staves }

	/**
	 * Returns the number of divisions of a quarter note. This is needed for
	 * Midi and MusicXML Export.
	 */
	fun computeDivisions(): Int {
		var actualdivision = 4
		staves.forEach { it.measures.forEach { it.voices.forEach { it.elements.forEach {
			if (it.duration != null)
				actualdivision = lcm(actualdivision, it.duration.denominator)
		} } } }
		return actualdivision / 4
	}

	/** Gets the [Staff] at the given [MP] (only the staff index is read). */
	fun getStaff(mp: MP): Staff =
			getStaff(mp.staff)

	/**
	 * Gets the [Staff] at the given global index.
	 */
	fun getStaff(staffIndex: Int): Staff =
			staves.getOrNull(staffIndex) ?: throw IllegalMPException(atStaff(staffIndex))

	/**
	 * Gets the global index of the given [Staff], or -1 if the staff
	 * is not part of this staves list.
	 */
	fun getStaffIndex(staff: Staff): Int =
			staves.indexOf(staff)

	/**
	 * Gets the [Measure] with the given index at the staff with the given
	 * global index.
	 */
	fun getMeasure(mp: MP): Measure =
		getStaff(mp.staff).getMeasure(mp.measure)

	/**
	 * Gets the [Voice] at the given [MP] (beat/element is ignored).
	 */
	fun getVoice(mp: MP): Voice =
			getMeasure(mp).getVoice(mp.voice)

	/**
	 * Adds a staff group for the given staves with the given style.
	 * Since a staff may only have one barline group, existing barline groups
	 * at the given positions are merged with the given group.
	 */
	fun addBarlineGroup(stavesRange: StavesRange, style: BarlineGroup.Style) {
		check(stavesRange.stop < this.staves.size, { "staves out of range" })

		//if the given group is within an existing one, ignore the new group
		//(we do not support nested barline groups)
		if (barlineGroups.find { stavesRange in it.staves } != null)
			return

		//delete existing groups intersecting the given range, but merge
		//the affected staves into the given group
		for (i in barlineGroups.size-1 downTo 0) {
			val group = barlineGroups[i]
			if (group.staves.intersects(stavesRange)) {
				barlineGroups.removeAt(i)
				stavesRange.extendBy(group.staves)
			}
		}

		//add new group at the right position
		//(the barline groups are sorted by start index)
		var i = barlineGroups.indexOfFirst { stavesRange.start < it.staves.start }
		if (i == -1) i == barlineGroups.size
		barlineGroups.add(i, BarlineGroup(stavesRange, style))
	}


	/**
	 * Adds a bracket group for the given staves with the given style.
	 */
	fun addBracketGroup(stavesRange: StavesRange, style: BracketGroup.Style) {
		check(stavesRange.stop < this.staves.size, { "staves out of range" })
		//add new group at the right position
		//(the bracket groups are sorted by start index)
		var i = bracketGroups.indexOfFirst { stavesRange.start < it.staves.start }
		if (i == -1) i == bracketGroups.size
		bracketGroups.add(i, BracketGroup(stavesRange, style))
	}

}
