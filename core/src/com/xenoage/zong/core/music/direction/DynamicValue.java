package com.xenoage.zong.core.music.direction;


import static com.xenoage.utils.collections.ArrayUtils.indexOf;
import static com.xenoage.zong.core.music.direction.WedgeType.Crescendo;

/**
 * Dynamic values: forte, piano, sforzando and so on.
 * 
 * These are the same dynamics as supported in MusicXML.
 * 
 * @author Andreas Wenger
 */
public enum DynamicValue {
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
	 * When this is the start dynamic value of a wedge (crescendo/diminuendo),
	 * the wedge ends by default at the returned value.
	 */
	public DynamicValue getWedgeEndValue(WedgeType type) {
		return getWedgeEndValue(type == Crescendo ? 1 : -1);
	}

	/**
	 * When this is the start dynamic value of a diminuendo,
	 * the diminuendo ends by default at the returned value.
	 */
	public DynamicValue getDiminuendoEndValue() {
		return getWedgeEndValue(-1);
	}

	/**
	 * Gets the end dynamic of a crescendo (dir = 1) or
	 * diminuendo (dir = -1), when this is the start value.
	 */
	private DynamicValue getWedgeEndValue(int dir) {
		DynamicValue[] wedgeProgress = {pp, p, mf, f, ff};
		int index = indexOf(wedgeProgress, round()) + dir;
		if (index < 0)
			return pp;
		else if (index >= wedgeProgress.length)
			return ff;
		else
			return wedgeProgress[index];
	}

	/**
	 * Returns the nearest commonly used dynamic value (pp, p, mf, f, ff)
	 * to this value.
	 */
	private DynamicValue round() {
		switch (this) {
			case pp: case p: case mf: case f: case ff:
				return this;
			//everything lower than pp is treated as pp,
			//everything higher than ff as ff
			case ppp: case pppp: case ppppp: case pppppp:
				return pp;
			case fff: case ffff: case fffff: case ffffff:
				return ff;
			//mp is like mf
			case mp:
				return mf;
			//sXY-values are treated as Y
			case sf: case rf: case rfz: case sfz:case fz:
				return f;
			case sfp: case fp:
				return p;
			case sfpp:
				return pp;
			case sffz:
				return ff;
		}
		return mf;
	}

}
