package com.xenoage.zong.core.music;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

import com.xenoage.zong.core.instrument.Instrument;


/**
 * In most cases a part is simply a single
 * staff assigned to a single instrument.
 * 
 * It can also be group of staves that belong
 * together, e.g. the two staves of a piano.
 * It can have more than one instrument,
 * e.g. in percussion staves where
 * several instruments are notated in a single staff.
 * 
 * @author Andreas Wenger
 */
@Data public final class Part {

	/** The name of the part. */
	@NonNull private String name;
	/** The abbreviation of the part, or null to use the name as the abbreviation. */
	private String abbreviation;
	/** The number of staves in this part. */
	private int stavesCount;
	/** The instruments belonging to this part, or null to use the default instrument. */
	private List<Instrument> instruments;
	
	
	public Part(String name, String abbreviation, int stavesCount, List<Instrument> instruments) {
		//check arguments
		checkArgsNotNull(name);
		if (stavesCount < 0)
			throw new IllegalArgumentException("at least 1 staff is required");
		this.name = name;
		this.abbreviation = abbreviation;
		this.stavesCount = stavesCount;
		this.instruments = instruments;
	}
	
	/**
	 * Gets the first instrument in this part.
	 * If unset, the default instrument is returned.
	 */
	@NonNull public Instrument getFirstInstrument() {
		if (instruments != null && instruments.size() > 0)
			return instruments.get(0);
		else
			return Instrument.defaultInstrument;
	}

}
