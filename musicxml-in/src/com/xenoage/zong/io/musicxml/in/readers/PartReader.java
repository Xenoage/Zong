package com.xenoage.zong.io.musicxml.in.readers;

import java.util.List;

import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.musicxml.types.MxlScorePart;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * Reads a {@link Part} from a given {@link MxlScorePart}.
 * 
 * @author Andreas Wenger
 */
public class PartReader {

	/**
	 * Reads part information. Not only the header ({@link MxlScorePart})
	 * must be given, but also the contents ({@link MxlPart}),
	 * which is needed to find transposition information.
	 */
	public static Part read(MxlScorePart mxlScorePart, MxlPart mxlPart) {
		List<Instrument> instruments = new InstrumentsReader(mxlScorePart, mxlPart).read();
		return new Part(mxlScorePart.getPartName(), mxlScorePart.getPartAbbreviation(), 1,
			instruments.size() > 0 ? instruments : null);
	}

}
