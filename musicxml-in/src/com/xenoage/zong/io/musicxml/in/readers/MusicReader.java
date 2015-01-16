package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.atStaff;
import static com.xenoage.zong.io.musicxml.in.util.CommandPerformer.execute;

import java.util.List;

import com.xenoage.utils.iterators.It;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureAddUpTo;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.VoiceElementWrite;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.musicxml.in.util.MusicReaderException;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlBackup;
import com.xenoage.zong.musicxml.types.MxlBarline;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlForward;
import com.xenoage.zong.musicxml.types.MxlInstrument;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPrint;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * This class reads the actual musical contents of
 * the given partwise MusicXML 2.0 document into a {@link Score}.
 * 
 * If possible, this reader works with the voice-element
 * to separate voices. TODO: if not existent or
 * used unreliably within a measure, implement this algorithm: 
 * http://archive.mail-list.com/musicxml/msg01673.html
 * 
 * TODO: Connect chords over staves, if they have the same
 * voice element but different staff element.
 *
 * @author Andreas Wenger
 */
public final class MusicReader {

	/**
	 * Reads the given MusicXML document and returns the score.
	 */
	public static void read(MxlScorePartwise doc, Score score, boolean ignoreErrors) {
		Context context = new Context(score, new MusicReaderSettings(ignoreErrors));
		
		//create the measures of the parts
		It<MxlPart> mxlParts = it(doc.getParts());
		for (MxlPart mxlPart : mxlParts) {
			//create measures
			execute(new MeasureAddUpTo(score, mxlPart.getMeasures().size()));
			//initialize each measure with a C clef
			Part part = score.getStavesList().getParts().get(mxlParts.getIndex());
			StavesRange stavesRange = score.getStavesList().getPartStaffIndices(part);
			for (int staff : stavesRange.getRange()) {
				execute(new MeasureElementWrite(new Clef(ClefType.clefTreble),
					score.getMeasure(MP.atMeasure(staff, 0)), _0));
			}
		}
		
		//write a 4/4 measure and C key signature in the first measure
		execute(new ColumnElementWrite(new Time(TimeType.time_4_4), score.getColumnHeader(0), _0, null));
		execute(new ColumnElementWrite(new TraditionalKey(0), score.getColumnHeader(0), _0, null));
			
		//read the parts
		mxlParts = it(doc.getParts());
		for (MxlPart mxlPart : mxlParts) {
			//clear part-dependent context values
			context.beginNewPart(mxlParts.getIndex());
			//read the measures
			It<MxlMeasure> mxlMeasures = it(mxlPart.getMeasures());
			for (MxlMeasure mxlMeasure : mxlMeasures) {
				try {
					context = readMeasure(context, mxlMeasure, mxlMeasures.getIndex());
				} catch (MusicReaderException ex) {
					throw new RuntimeException("Error at " + ex.getContext().toString(), ex);
				} catch (Exception ex) {
					throw new RuntimeException("Error (roughly) around " + context.toString(), ex);
				}
			}
		}

		//go through the whole score, and fill empty measures (that means, measures where
		//voice 0 has no single VoiceElement) with rests
		Fraction measureDuration = fr(1, 4);
		for (int iStaff = 0; iStaff < score.getStavesCount(); iStaff++) {
			Staff staff = score.getStaff(atStaff(iStaff));
			for (int iMeasure : range(staff.getMeasures())) {
				Measure measure = staff.getMeasure(iMeasure);
				Time newTime = score.getHeader().getColumnHeader(iMeasure).getTime();
				if (newTime != null) {
					//time signature has changed
					measureDuration = newTime.getType().getMeasureBeats();
				}
				if (measureDuration == null) { //senza misura
					measureDuration = fr(4, 4); //use whole rest
				}
				Voice voice0 = measure.getVoice(0);
				if (voice0.isEmpty()) {
					//TODO: "whole rests" or split. currently, also 3/4 rests are possible
					MP mp = atElement(iStaff, iMeasure, 0, 0);
					new VoiceElementWrite(score.getVoice(mp), mp, new Rest(measureDuration), null).execute();
				}
			}
		}
	}

	/**
	 * Reads the given measure element.
	 */
	private static Context readMeasure(Context context, MxlMeasure mxlMeasure,
		int measureIndex) {
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
					new ChordReader(mxlNotes).readIntoContext(context);
					break;
				}
				case Attributes:
					new AttributesReader((MxlAttributes) mxlMDC).readToContext(context);
					break;
				case Backup:
					readBackup(context, (MxlBackup) mxlMDC);
					break;
				case Forward:
					readForward(context, (MxlForward) mxlMDC);
					break;
				case Print:
					new PrintReader((MxlPrint) mxlMDC).readToContext(context);
					break;
				case Direction:
					new DirectionReader((MxlDirection) mxlMDC).readToContext(context);
					break;
				case Barline:
					new BarlineReader((MxlBarline) mxlMDC).readIntoContext(context);
					break;
			}
		}
		return context;
	}

	/**
	 * Reads the given backup element.
	 */
	private static void readBackup(Context context, MxlBackup mxlBackup) {
		//duration
		Fraction duration = readDuration(mxlBackup.getDuration(), context.getDivisions()).invert();
		//move cursor
		context.moveCurrentBeat(duration);
	}

	/**
	 * Reads the given forward element.
	 */
	private static void readForward(Context context, MxlForward mxlForward) {
		//duration
		Fraction duration = readDuration(mxlForward.getDuration(), context.getDivisions());
		//move cursor
		context.moveCurrentBeat(duration);
	}

	/**
	 * Returns the duration as a {@link Fraction} from the given duration in divisions.
	 */
	public static Fraction readDuration(int duration, int divisionsPerQuarter) {
		if (duration == 0) {
			throw new RuntimeException("Element has a duration of 0.");
		}
		Fraction ret = fr(duration, 4 * divisionsPerQuarter);
		return ret;
	}

}
