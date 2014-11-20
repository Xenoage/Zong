package com.xenoage.zong.core.instrument;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.lang.VocByString.voc;
import static com.xenoage.zong.core.music.Pitch.pi;
import static java.util.Collections.emptyList;

import java.util.List;

import lombok.Data;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
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
@Data public abstract class Instrument {

	/** The ID of the instrument (e.g. "instrument_piano"). */
	private final String id;
	/** The international name of this instrument. */
	@NonNull private String name = "";
	/** The international abbreviation of this instrument. */
	@MaybeNull private String abbreviation;
	/** The groups in which the instrument is listed (e.g. woodwinds,
	 * percussion etc.), or empty if undefined. The empty list may be immutable. */
	@NonNull private List<InstrumentGroup> groups = emptyList();
	/** The volume between 0 (silent) and 1 (full), or null for default. */
	@MaybeNull private Float volume;
	/** The panning between -1 (left) and 1 (right), or null for default */
	@MaybeNull private Float pan;

	/** Default instrument: piano. */
	public static final Instrument defaultInstrument = createDefaultInstrument();

	
	public void setVolume(Float volume) {
		if (volume != null && (volume < 0 || volume > 1))
			throw new IllegalArgumentException("Illegal volume value: " + volume);
		this.volume = volume;
	}
	
	public void setPan(Float pan) {
		if (pan != null && (pan < -1 || pan > 1))
			throw new IllegalArgumentException("Illegal pan value: " + pan);
		this.pan = pan;
	}

	/**
	 * Gets the localized name of this instrument. If it is undefined, the
	 * international name is returned.
	 */
	public String getLocalName() {
		String ret = Lang.getWithNull(voc(id + "_Name"));
		if (ret == null)
			return name;
		return ret;
	}

	/**
	 * Gets the localized abbreviation of this instrument. If it is undefined, the
	 * international name is returned.
	 */
	public String getLocalAbbreviation() {
		String ret = Lang.getWithNull(voc(id + "_Abbr"));
		if (ret == null)
			return abbreviation;
		return ret;
	}

	/**
	 * Returns the groupnames in which the instrument is listed (e.g. woodwinds,
	 * percussion etc.).
	 */
	public List<String> getGroupNames() {
		List<String> ret = alist();
		for (InstrumentGroup value : groups) {
			ret.add(Lang.get(voc(value.getId())));
		}
		return ret;
	}
	
	private static Instrument createDefaultInstrument() {
		PitchedInstrument ret = new PitchedInstrument("default");
		ret.setName("Piano");
		ret.setAbbreviation("Pno");
		ret.setBottomPitch(pi(6, 0, 1));
		ret.setTopPitch(pi(0, 0, 8));
		return ret;
	}

}
