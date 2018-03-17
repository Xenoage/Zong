package com.xenoage.zong.core.music.direction


import com.xenoage.zong.core.music.direction.WedgeType.Crescendo

/**
 * Dynamic values: forte, piano, sforzando and so on.
 *
 * These are the same dynamics as supported in MusicXML.
 */
enum class DynamicValue {
	p,
	pp,
	ppp,
	pppp,
	ppppp,
	pppppp,
	f,
	ff,
	fff,
	ffff,
	fffff,
	ffffff,
	mp,
	mf,
	sf,
	sfp,
	sfpp,
	fp,
	rf,
	rfz,
	sfz,
	sffz,
	fz;

	/**
	 * When this is the start dynamic value of a diminuendo,
	 * the diminuendo ends by default at the returned value.
	 */
	val diminuendoEndValue: DynamicValue
		get() = getWedgeEndValue(-1)


	/**
	 * When this is the start dynamic value of a wedge (crescendo/diminuendo),
	 * the wedge ends by default at the returned value.
	 */
	fun getWedgeEndValue(type: WedgeType): DynamicValue {
		return getWedgeEndValue(if (type == Crescendo) 1 else -1)
	}

	/**
	 * Gets the end dynamic of a crescendo (dir = 1) or
	 * diminuendo (dir = -1), when this is the start value.
	 */
	private fun getWedgeEndValue(dir: Int): DynamicValue {
		val wedgeProgress = arrayOf(pp, p, mf, f, ff)
		val index = wedgeProgress.indexOf(rounded) + dir
		return when {
			index < 0 -> pp
			index >= wedgeProgress.size -> ff
			else -> wedgeProgress[index]
		}
	}

	/**
	 * Returns the nearest commonly used dynamic value (pp, p, mf, f, ff)
	 * to this value.
	 */
	private val rounded: DynamicValue
		get() = when (this) {
			pp, p, mf, f, ff -> this
			//everything lower than pp is treated as pp,
			//everything higher than ff as ff
			ppp, pppp, ppppp, pppppp -> pp
			fff, ffff, fffff, ffffff -> ff
			//mp is like mf
			mp -> mf
			//sXY-values are treated as Y
			sf, rf, rfz, sfz, fz -> f
			sfp, fp -> p
			sfpp -> pp
			sffz -> ff
		}

}
