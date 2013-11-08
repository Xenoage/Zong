package com.xenoage.zong.core.instrument;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.lang.VocByString.voc;
import static com.xenoage.zong.core.music.Pitch.pi;
import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.lang.Lang;

/**
 * Base class for an instrument.
 * 
 * There are pitched instruments, like piano or trumpet,
 * and unpitched instruments, like drums. If this information
 * is known, use the corresponding subclasses instead.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@Const @Data public abstract class Instrument {

	/** The ID of the instrument (e.g. "instrument_piano"). */
	private final String id;
	/** Basic data. */
	private final InstrumentData data;

	/** Default instrument: piano. */
	public static final Instrument defaultValue = new PitchedInstrument("default",
		new InstrumentData("Piano", "Pno", null, null, null), 0, Transpose.noTranspose, pi(6, 0, 1),
		pi(0, 0, 8), 0);


	/**
	 * Gets the localized name of this instrument. If it is undefined, the
	 * international name is returned.
	 */
	public String getLocalName() {
		String ret = Lang.getWithNull(voc(id + "_Name"));
		if (ret == null) {
			return data.getName();
		}
		else {
			return ret;
		}
	}

	/**
	 * Gets the localized abbreviation of this instrument. If it is undefined, the
	 * international name is returned.
	 */
	public String getLocalAbbreviation() {
		String ret = Lang.getWithNull(voc(id + "_Abbr"));
		if (ret == null) {
			return data.getAbbreviation();
		}
		else {
			return ret;
		}
	}

	/**
	 * Returns the groupnames in which the instrument is listed (e.g. woodwinds,
	 * percussion etc.). The returned list is never null.
	 */
	public IList<String> getGroupNames() {
		CList<String> ret = clist();
		if (data.getGroups() != null) {
			for (InstrumentGroup value : data.getGroups()) {
				ret.add(Lang.get(voc(value.getId())));
			}
		}
		return ret.close();
	}

}
