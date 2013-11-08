package com.xenoage.zong.core.instrument;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.IList;


/**
 * Basic information for each type of instrument.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@Const @Data public class InstrumentData
{

	/** The international name of this instrument. */
	@NonNull private final String name;
	/** The international abbreviation of this instrument. */
	@MaybeNull private final String abbreviation;
	/** The groups in which the instrument is listed (e.g. woodwinds,
	 * percussion etc.), or null if undefined. */
	@MaybeNull private final IList<InstrumentGroup> groups;
	/** The volume between 0 (silent) and 1 (full). */
	@MaybeNull private final Float volume;
	/** The panning between -1 (left) and 1 (right). */
	@MaybeNull private final Float pan;


	public InstrumentData(String name, String abbreviation, IList<InstrumentGroup> groups, Float volume, Float pan) {
		if (volume != null && (volume < 0 || volume > 1))
			throw new IllegalArgumentException("Illegal volume value: " + volume);
		if (pan != null && (pan < -1 || pan > 1))
			throw new IllegalArgumentException("Illegal pan value: " + pan);
		this.name = name;
		this.abbreviation = abbreviation;
		this.groups = groups;
		this.volume = volume;
		this.pan = pan;
	}


}
