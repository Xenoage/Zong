package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeEmpty;
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
		Transpose transpose = Transpose.noTranspose;
		Integer midiProgram;
		Integer midiChannel;
		Float volume;
		Float pan;
	}
	
	private HashMap<String, Info> infos = map();
	private Transpose partTranspose = Transpose.noTranspose;
	
	
	/**
	 * Reads the instruments from the given {@link ScorePart}.
	 * Not only the header ({@link MxlScorePart})
	 * must be given, but also the contents ({@link MxlPart}),
	 * which is needed to find transposition information.
	 */
	@MaybeEmpty public List<Instrument> read() {
		readScoreInstruments();
		readTranspositions();
		readMidiInstruments();
		List<Instrument> ret = createInstruments();
		return ret;
	}
	
	private Info getInfo(String id) {
		return checkNotNull(infos.get(id), "Unknown instrument: " + id);
	}

	private void readScoreInstruments() {
		for (MxlScoreInstrument mxlScoreInstr : mxlScorePart.getScoreInstruments()) {
			String id = mxlScoreInstr.getId();
			Info info = new Info();
			info.id = id;
			info.name = checkNotNull(mxlScoreInstr.getInstrumentName());
			info.abbreviation = mxlScoreInstr.getInstrumentAbbreviation();
			infos.put(id, info);
		}
	}
	
	private void readTranspositions() {
		List<MxlScoreInstrument> mxlScoreInstruments = mxlScorePart.getScoreInstruments();
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
	 * Returns the {@link Transpose} of the first measure of this part,
	 * or {@link Transpose#noTranspose} if there is none.
	 */
	private Transpose findFirstTranspose() {
		List<MxlMeasure> mxlMeasures = mxlPart.getMeasures();
		if (mxlMeasures.size() > 0) {
			MxlMeasure mxlMeasure = mxlMeasures.get(0);
			for (MxlMusicDataContent c : mxlMeasure.getMusicData().getContent()) {
				if (c.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					MxlAttributes a = (MxlAttributes) c;
					return new TransposeReader(a.getTranspose()).read();
				}
			}
		}
		return Transpose.noTranspose;
	}

	/**
	 * Returns the last {@link Transpose} of this part that can be found
	 * before the first note that is played by the instrument with the given ID,
	 * or {@link Transpose#noTranspose} if there is none.
	 */
	private Transpose findLastTransposeBeforeFirstNote(String instrumentID) {
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
						return new TransposeReader(lastAttributes.getTranspose()).read();
				}
			}
		}
		return Transpose.noTranspose;
	}
	
	private void readMidiInstruments() {
		for (MxlMidiInstrument mxlMidiInstr : mxlScorePart.getMidiInstruments()) {
			Info info = getInfo(mxlMidiInstr.id);
			//midi program
			info.midiProgram = mxlMidiInstr.getMidiProgram();
			//midi channel
			info.midiChannel = mxlMidiInstr.getMidiChannel();
			//global volume
			info.volume = mxlMidiInstr.getVolume();
			if (info.volume != null)
				info.volume /= 100; //to 0..1
			//global panning
			info.pan = mxlMidiInstr.getPan();
			if (info.pan != null) {
				if (info.pan > 90)
					info.pan = 90 - (info.pan - 90); //e.g. convert 120° to 60°
				else if (info.pan < -90)
					info.pan = -90 - (info.pan + 90); //e.g. convert -120° to -60°
				info.pan /= 90f; //to -1..1
			}
		}
	}
	
	private List<Instrument> createInstruments() {
		List<Instrument> ret = alist();
		for (MxlScoreInstrument mxlScoreInstr : mxlScorePart.getScoreInstruments()) {
			Instrument instrument = readInstrument(getInfo(mxlScoreInstr.getId()));
			ret.add(instrument);
		}
		//when no instrument was created, but a transposition was found, create
		//a default instrument with this transposition
		if (ret.size() == 0 && partTranspose != Transpose.noTranspose) {
			PitchedInstrument instrument = new PitchedInstrument(mxlPart.getId());
			instrument.setTranspose(partTranspose);
			ret.add(instrument);
		}
		return ret;
	}
	
	private Instrument readInstrument(Info info) {
		Instrument instrument = null;
		if (info.midiChannel != null && info.midiChannel == 10) {
			//unpitched instrument
			instrument = new UnpitchedInstrument(info.id);
		}
		else {
			//pitched instrument
			PitchedInstrument pitchedInstrument;
			instrument = pitchedInstrument = new PitchedInstrument(info.id);
			//midi-program is 1-based in MusicXML but 0-based in MIDI
			int midiProgram = notNull(info.midiProgram, 1) - 1; //TODO: find value that matches instrument name
			midiProgram = MathUtils.clamp(midiProgram, 0, 127);
			pitchedInstrument.setMidiProgram(midiProgram);
			pitchedInstrument.setTranspose(info.transpose);
		}
		instrument.setName(info.name);
		instrument.setAbbreviation(info.abbreviation);
		instrument.setVolume(info.volume);
		instrument.setPan(info.pan);
		return instrument;
	}

}
