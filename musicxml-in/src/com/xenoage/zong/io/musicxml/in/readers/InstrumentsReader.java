package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.CheckUtils;
import com.xenoage.utils.NullUtils;
import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.instrument.Instrument;
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
@RequiredArgsConstructor
public class InstrumentsReader {
	
	private final MxlScorePart mxlScorePart;
	private final MxlPart mxlPart;
	
	
	private class Info {
		String id;
		String name;
		String abbreviation;
		MxlTranspose transpose;
		MxlMidiInstrument midiInstrument = new MxlMidiInstrument(null, null, null, null, ""); //GOON
	}
	
	private List<MxlScoreInstrument> mxlScoreInstruments;
	private List<MxlMidiInstrument> mxlMidiInstruments;
	private HashMap<String, Info> infos = map();
	private MxlTranspose partTranspose;
	
	
	/**
	 * Reads the instruments from the given {@link ScorePart}.
	 * Not only the header ({@link MxlScorePart})
	 * must be given, but also the contents ({@link MxlPart}),
	 * which is needed to find transposition information.
	 */
	@MaybeEmpty public List<Instrument> read() {
		mxlScoreInstruments = mxlScorePart.getScoreInstruments();
		mxlMidiInstruments = mxlScorePart.getMidiInstruments();

		readScoreInstruments();
		readTranspositions();
		readMidiInstruments();
		
		List<Instrument> ret = createInstruments();
		
		//when no instrument was created, but a transposition was found, create
		//a default instrument with this transposition
		if (ret.size() == 0 && partTranspose != null) {
			PitchedInstrument instrument = new PitchedInstrument(mxlPart.getId());
			instrument.setTranspose(readTranspose(partTranspose));
			ret.add(instrument);
		}
		return ret;
	}
	
	private Info getInfo(String id) {
		return checkNotNull(infos.get(id), "Unknown instrument: " + id);
	}

	private void readScoreInstruments() {
		for (MxlScoreInstrument mxlScoreInstr : mxlScoreInstruments) {
			String id = mxlScoreInstr.getId();
			Info info = new Info();
			info.id = id;
			info.name = checkNotNull(mxlScoreInstr.getInstrumentName());
			info.abbreviation = mxlScoreInstr.getInstrumentAbbreviation();
			infos.put(id, info);
		}
	}
	
	private void readTranspositions() {
		if (mxlScoreInstruments.size() == 0) {
			//no instrument defined, but maybe we have a transposition anyway
			partTranspose = findFirstTranspose();
		}
		if (mxlScoreInstruments.size() == 1) {
			//only one instrument: find transposition (if any) in first attributes of first measure
			getInfo(mxlScoreInstruments.get(0).getId()).transpose = findFirstTranspose();
		}
		else if (mxlScoreInstruments.size() > 1) {
			//more than one instrument in this part:
			//for each instrument, find its first note and the last transposition change before that note
			for (MxlScoreInstrument mxlScoreInstr : mxlScoreInstruments) {
				getInfo(mxlScoreInstr.getId()).transpose =
					findLastTransposeBeforeFirstNote(mxlScoreInstr.getId());
			}
		}
	}

	/**
	 * Returns the {@link MxlTranspose} of the first measure of this part,
	 * or null if there is none.
	 */
	private MxlTranspose findFirstTranspose() {
		List<MxlMeasure> mxlMeasures = mxlPart.getMeasures();
		if (mxlMeasures.size() > 0) {
			MxlMeasure mxlMeasure = mxlMeasures.get(0);
			for (MxlMusicDataContent c : mxlMeasure.getMusicData().getContent()) {
				if (c.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					MxlAttributes a = (MxlAttributes) c;
					return a.getTranspose();
				}
			}
		}
		return null;
	}

	/**
	 * Returns the last {@link MxlTranspose} of this part that can be found
	 * before the first note that is played by the instrument with the given ID,
	 * or null if there is none.
	 */
	private MxlTranspose findLastTransposeBeforeFirstNote(String instrumentID) {
		for (MxlMeasure mxlMeasure : mxlPart.getMeasures()) {
			MxlAttributes lastAttributes = null;
			for (MxlMusicDataContent c : mxlMeasure.getMusicData().getContent()) {
				if (c.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					lastAttributes = (MxlAttributes) c;
				}
				else if (c.getMusicDataContentType() == MxlMusicDataContentType.Note) {
					MxlNote n = (MxlNote) c;
					if (n.getInstrument() != null && n.getInstrument().getId().equals(instrumentID) &&
						lastAttributes != null)
						return lastAttributes.getTranspose();
				}
			}
		}
		return null;
	}
	
	private void readMidiInstruments() {
		for (MxlMidiInstrument mxlMidiInstr : mxlScorePart.getMidiInstruments()) {
			getInfo(mxlMidiInstr.id).midiInstrument = mxlMidiInstr;
		}
	}
	
	private List<Instrument> createInstruments() {
		List<Instrument> ret = alist();
		for (MxlScoreInstrument mxlScoreInstr : mxlScorePart.getScoreInstruments()) {
			Instrument instrument = readInstrument(getInfo(mxlScoreInstr.getId()));
			ret.add(instrument);
		}
		return ret;
	}
	
	private Instrument readInstrument(Info info) {
		
		Integer midiChannel = info.midiInstrument.getMidiChannel();

		//global volume
		Float volume = info.midiInstrument.getVolume();
		if (volume != null)
			volume /= 100f; //to 0..1

		//global panning
		Float pan = info.midiInstrument.getPan();
		if (pan != null) {
			if (pan > 90)
				pan = 90 - (pan - 90); //e.g. convert 120° to 60°
			else if (pan < -90)
				pan = -90 - (pan + 90); //e.g. convert -120° to -60°
			pan /= 90f; //to -1..1
		}

		Instrument instrument = null;
		if (midiChannel != null && midiChannel.equals(10)) {
			//unpitched instrument
			instrument = new UnpitchedInstrument(info.id);
		}
		else {
			//pitched instrument
			PitchedInstrument pitchedInstrument;
			instrument = pitchedInstrument = new PitchedInstrument(info.id);
			//midi-program is 1-based in MusicXML but 0-based in MIDI
			int midiProgram = notNull(info.midiInstrument.getMidiProgram(), 1) - 1; //TODO: find value that matches instrument name
			midiProgram = MathUtils.clamp(midiProgram, 0, 127);
			pitchedInstrument.setMidiProgram(midiProgram);
			Transpose transpose = readTranspose(info.transpose);
			pitchedInstrument.setTranspose(transpose);
		}
		instrument.setName(info.name);
		instrument.setAbbreviation(info.abbreviation);
		instrument.setVolume(volume);
		instrument.setPan(pan);
		return instrument;
	}
	
	/**
	 * Returns the Transpose for the given MxlTranspose, or an instance with
	 * no transposition if the argument was null.
	 */
	public static Transpose readTranspose(MxlTranspose mxlTranspose) {
		if (mxlTranspose != null) {
			return new Transpose(mxlTranspose.getChromatic(), mxlTranspose.getDiatonic(), notNull(
				mxlTranspose.getOctaveChange(), 0), mxlTranspose.isDoubleValue());
		}
		return Transpose.noTranspose;
	}

}
