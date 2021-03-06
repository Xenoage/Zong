package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.instrument.Transpose;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.musicxml.types.*;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent.MxlTimeContentType;
import com.xenoage.zong.musicxml.types.enums.MxlTimeSymbol;
import lombok.RequiredArgsConstructor;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static com.xenoage.utils.math.MathUtils.clamp;
import static com.xenoage.zong.core.music.time.TimeType.timeSenzaMisura;
import static com.xenoage.zong.core.music.time.TimeType.timeType;

/**
 * Reads divisions, key signature, time signature, clefs
 * and transposition changes from {@link MxlAttributes}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class AttributesReader {
	
	private final MxlAttributes mxlAttributes;
	
	
	/**
	 * Reads the given attributes element.
	 */
	public void readToContext(Context context) {

		//divisions
		Integer divisions = mxlAttributes.getDivisions();
		if (divisions != null)
			context.setDivisions(divisions);

		//key signature
		Key key = readKey(mxlAttributes.getKey());
		if (key != null)
			context.writeColumnElement(key);

		//time signature
		TimeSignature time = readTime(mxlAttributes.getTime());
		if (time != null) //TODO: attribute "number" for single staves
			context.writeColumnElement(time);

		//clefs
		if (mxlAttributes.getClefs() != null) {
			for (MxlClef mxlClef : mxlAttributes.getClefs()) {
				ClefReader clefReader = new ClefReader(mxlClef);
				Clef clef = clefReader.read();
				int staff = clefReader.readStaff();
				if (clef != null)
					context.writeMeasureElement(clef, staff);
			}
		}

		//transposition changes - TODO: clean solution for instrument changes
		PitchedInstrument instrument = readTransposedInstrument(mxlAttributes.getTranspose());
		if (instrument != null) {
			//write to all staves of this part
			for (int staff = 0; staff < context.getPartStaffIndices().getCount(); staff++)
				context.writeMeasureElement(new InstrumentChange(instrument), staff);
		}
	}
	
	private Key readKey(MxlKey mxlKey) {
		if (mxlKey == null)
			return null;
		//read fifths. currently, only -7 to 7 is supported (clamp, if needed)
		int mxlFifths = INSTANCE.clamp(mxlKey.fifths, -7, 7);
		//write to column header (TODO: attribute "number" for single staves)
		Mode mode = getEnumValue(mxlKey.mode, Mode.values());
		Key key = new TraditionalKey(mxlFifths, mode);
		return key;
	}

	private TimeSignature readTime(MxlTime mxlTime) {
		if (mxlTime == null)
			return null;
		TimeSignature time = null;
		MxlTimeContentType type = mxlTime.getContent().getTimeContentType();
		if (type == MxlTimeContentType.SenzaMisura) {
			//senza misura
			time = new TimeSignature(Companion.getTimeSenzaMisura());
		}
		else if (type == MxlTimeContentType.NormalTime) {
			//normal time
			MxlNormalTime mxlNormalTime = (MxlNormalTime) mxlTime.getContent();
			//common, cut or fractional?
			if (mxlTime.getSymbol() == MxlTimeSymbol.Cut)
				time = new TimeSignature(TimeType.Companion.getTimeAllaBreve());
			else if (mxlTime.getSymbol() == MxlTimeSymbol.Common)
				time = new TimeSignature(TimeType.Companion.getTimeCommon());
			else //otherwise, we currently support only normal fractional time signatures
				time = new TimeSignature(Companion.timeType(mxlNormalTime.getBeats(), mxlNormalTime.getBeatType()));
		}
		return time;
	}
	
	private PitchedInstrument readTransposedInstrument(MxlTranspose mxlTranspose) {
		if (mxlTranspose == null)
			return null;
		Transpose transpose = new TransposeReader(mxlTranspose).read();
		PitchedInstrument instrument = new PitchedInstrument("", 0);
		instrument.setTranspose(transpose);
		return instrument;
	}

}
