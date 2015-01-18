package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readDuration;

import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlBackup;
import com.xenoage.zong.musicxml.types.MxlBarline;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlForward;
import com.xenoage.zong.musicxml.types.MxlInstrument;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPrint;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;

/**
 * Reads a {@link MxlMeasure} to the context.
 * 
 * @author Andreas Wenger
 */
public class MeasureReader {

	/**
	 * Reads the given measure element.
	 */
	public static void readToContext(MxlMeasure mxlMeasure, int measureIndex, Context context) {
		//begin a new measure
		context.beginNewMeasure(measureIndex);
		//list all elements
		List<MxlMusicDataContent> content = mxlMeasure.getMusicData().getContent();
		for (int i = 0; i < content.size(); i++) { //i may be modified within this loop
			MxlMusicDataContent mxlMDC = content.get(i);
			switch (mxlMDC.getMusicDataContentType()) {
				case Note: {
					MxlNote mxlNote = ((MxlNote) mxlMDC);
					//when it is a chord, ignore it, because we did already read it
					if (mxlNote.getContent().getFullNote().isChord()) {
						continue;
					}
					//instrument change?
					MxlInstrument mxlInstrument = mxlNote.getInstrument();
					if (mxlInstrument != null) {
						String instrumentID = mxlInstrument.getId();
						if (context.getInstrumentID() == null ||
							!context.getInstrumentID().equals(instrumentID)) {
							//instrument change detected!
							context.writeInstrumentChange(instrumentID);
						}
					}
					//collect all following notes which have a chord-element
					//inbetween there may be direction elements, so we collect the
					//notes until the first non-chord or non-direction element and after
					//that go on at the current position + 1
					List<MxlNote> mxlNotes = alist(mxlNote);
					for (int i2 = i + 1; i2 < content.size(); i2++) {
						MxlMusicDataContent mxlMDC2 = content.get(i2);
						boolean goOn = false;
						if (mxlMDC2.getMusicDataContentType() == MxlMusicDataContentType.Note) {
							MxlNote mxlNote2 = (MxlNote) mxlMDC2;
							if (mxlNote2.getContent().getFullNote().isChord()) {
								mxlNotes.add(mxlNote2);
								goOn = true;
							}
						}
						else if (mxlMDC2.getMusicDataContentType() == MxlMusicDataContentType.Direction) {
							goOn = true;
						}
						if (!goOn)
							break;
					}
					new ChordReader(mxlNotes).readToContext(context);
					break;
				}
				case Attributes:
					new AttributesReader((MxlAttributes) mxlMDC).readToContext(context);
					break;
				case Backup:
					readBackupToContext((MxlBackup) mxlMDC, context);
					break;
				case Forward:
					readForwardToContext((MxlForward) mxlMDC, context);
					break;
				case Print:
					new PrintReader((MxlPrint) mxlMDC).readToContext(context);
					break;
				case Direction:
					new DirectionReader((MxlDirection) mxlMDC).readToContext(context);
					break;
				case Barline:
					new BarlineReader((MxlBarline) mxlMDC).readToContext(context);
					break;
			}
		}
	}

	private static void readBackupToContext(MxlBackup mxlBackup, Context context) {
		//duration
		Fraction duration = readDuration(mxlBackup.getDuration(), context.getDivisions()).invert();
		//move cursor
		context.moveCurrentBeat(duration);
	}

	private static void readForwardToContext(MxlForward mxlForward, Context context) {
		//duration
		Fraction duration = readDuration(mxlForward.getDuration(), context.getDivisions());
		//move cursor
		context.moveCurrentBeat(duration);
	}
	
}
