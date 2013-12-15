package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.musicxml.types.MxlScorePart;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


/**
 * Reads a {@link Part} from a given {@link MxlScorePart}.
 * 
 * @author Andreas Wenger
 */
public final class PartReader
{
	
	/**
	 * Reads part information. Not only the header ({@link MxlScorePart})
	 * must be given, but also the contents ({@link MxlPart}),
	 * which is needed to find transposition information.
	 */
	public static Part readPart(MxlScorePart mxlScorePart, MxlPart mxlPart)
	{
		PVector<Instrument> instruments = InstrumentsReader.read(mxlScorePart, mxlPart);
		return new Part(mxlScorePart.getName(), mxlScorePart.getAbbreviation(),
			1, instruments.size() > 0 ? instruments : null);
	}

}
