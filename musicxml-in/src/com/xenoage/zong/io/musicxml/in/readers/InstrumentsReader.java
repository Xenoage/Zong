package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.base.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.utils.pdlib.PVector.pvec;

import java.util.HashMap;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.instrument.InstrumentBase;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.instrument.UnpitchedInstrument;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlMidiInstrument;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlScoreInstrument;
import com.xenoage.zong.musicxml.types.MxlScorePart;
import com.xenoage.zong.musicxml.types.MxlTranspose;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


/**
 * This class reads the {@link Instrument}s from a
 * given {@link MxlScorePart}.
 * 
 * TODO: more information like midi channel, bank etc.
 * 
 * TODO: include better guesses about the instruments,
 * e.g. which MIDI program to take if nothing is specified,
 * use localized names and so on.
 * 
 * @author Andreas Wenger
 */
public final class InstrumentsReader
{
	
	/**
	 * Reads the instruments from the given {@link ScorePart}.
	 * Not only the header ({@link MxlScorePart})
	 * must be given, but also the contents ({@link MxlPart}),
	 * which is needed to find transposition information.
	 */
	@MaybeEmpty public static PVector<Instrument> read(MxlScorePart mxlScorePart,
		MxlPart mxlPart)
	{
		PVector<Instrument> ret = pvec();
		PVector<MxlScoreInstrument> mxlInstr = mxlScorePart.getScoreInstruments();
    //score-instrument elements
    HashMap<String, Tuple2<String, String>> instrumentBaseValues = map();
    for (MxlScoreInstrument mxlScoreInstr : mxlInstr)
    {
    	String id = mxlScoreInstr.getID();
    	String name = mxlScoreInstr.getInstrumentName(); //never null
    	String abbr = mxlScoreInstr.getInstrumentAbbreviation();
    	instrumentBaseValues.put(id, t(name, abbr));
    }
    //find transposition information
    HashMap<String, MxlTranspose> instrumentTranspositions = map();
    if (mxlInstr.size() == 1)
    {
    	//only one instrument: find transposition (if any) in first attributes of first measure
    	instrumentTranspositions.put(mxlInstr.getFirst().getID(), findFirstTranspose(mxlPart));
    }
    else if (mxlInstr.size() > 1)
    {
    	//more than one instrument in this part:
    	//for each instrument, find its first note and the last transposition change before
    	//that note
    	for (MxlScoreInstrument mxlScoreInstr : mxlInstr)
    	{
    		instrumentTranspositions.put(mxlScoreInstr.getID(),
    			findLastTransposeBeforeFirstNote(mxlPart, mxlScoreInstr.getID()));
    	}
    }
    //midi-instrument elements
    for (MxlMidiInstrument mxlMidiInstr : mxlScorePart.getMidiInstruments())
    {
      String id = mxlMidiInstr.id;
      Tuple2<String, String> baseValues = instrumentBaseValues.get(id);
      if (baseValues == null)
      {
      	log(warning("Unknown midi-instrument: " + id));
      	continue;
      }
      Integer midiChannel = mxlMidiInstr.midiChannel;
      
      //global volume
      Float volume = mxlMidiInstr.volume;
      if (volume != null)
      	volume /= 100f; //to 0..1
      
      //global panning
      Float pan = mxlMidiInstr.pan;
      if (pan != null)
      {
      	if (pan > 90)
      		pan = 90 - (pan - 90); //e.g. convert 120° to 60°
      	else if (pan < -90)
      		pan = -90 - (pan + 90); //e.g. convert -120° to -60°
      	pan /= 90f; //to -1..1
      }
      
      InstrumentBase base = new InstrumentBase(id, baseValues.get1(),
      	baseValues.get2(), null, volume, pan);
      if (midiChannel != null && midiChannel.equals(10))
      {
      	//unpitched instrument
      	ret = ret.plus(new UnpitchedInstrument(base));
      }
      else
      {
      	//pitched instrument
      	//midi-program is 1-based in MusicXML but 0-based in MIDI
      	int midiProgram = notNull(mxlMidiInstr.midiProgram, 1) - 1; //TODO: find value that matches instrument name
      	midiProgram = MathUtils.clamp(midiProgram, 0, 127);
      	Transpose transpose = readTranspose(instrumentTranspositions.get(id));
      	ret = ret.plus(new PitchedInstrument(
      		base, midiProgram, transpose, null, null, 0));
      }
    }
    return ret;
	}
	
	
	/**
	 * Returns the {@link MxlTranspose} of the first measure of the given part,
	 * or null if there is none.
	 */
	private static MxlTranspose findFirstTranspose(MxlPart mxlPart)
	{
		if (mxlPart.getMeasures().size() > 0)
		{
			MxlMeasure mxlMeasure = mxlPart.getMeasures().getFirst();
			for (MxlMusicDataContent c : mxlMeasure.getMusicData().getContent())
			{
				if (c.getMusicDataContentType() == MxlMusicDataContentType.Attributes)
				{
					MxlAttributes a = (MxlAttributes) c;
					return a.transpose;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the last {@link MxlTranspose} of the given part that can be found
	 * before the first note that is played by the instrument with the given ID,
	 * or null if there is none.
	 */
	private static MxlTranspose findLastTransposeBeforeFirstNote(MxlPart mxlPart,
		String instrumentID)
	{
		for (MxlMeasure mxlMeasure : mxlPart.getMeasures())
		{
			MxlAttributes lastAttributes = null;
			for (MxlMusicDataContent c : mxlMeasure.getMusicData().getContent())
			{
				if (c.getMusicDataContentType() == MxlMusicDataContentType.Attributes)
				{
					lastAttributes = (MxlAttributes) c;
				}
				else if (c.getMusicDataContentType() == MxlMusicDataContentType.Note)
				{
					MxlNote n = (MxlNote) c;
					if (n.instrument != null && n.instrument.getID().equals(instrumentID)
						&& lastAttributes != null)
						return lastAttributes.transpose;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the Transpose for the given MxlTranspose, or an instance with
	 * no transposition if the argument was null.
	 */
	private static Transpose readTranspose(MxlTranspose mxlTranspose)
	{
		if (mxlTranspose != null)
		{
			return new Transpose(
				mxlTranspose.getChromatic(), mxlTranspose.getDiatonic(),
				notNull(mxlTranspose.getOctaveChange(), 0), mxlTranspose.getDouble());
		}
		return Transpose.none();
	}
	

}
