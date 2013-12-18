package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.MeasureSide.Left;
import static com.xenoage.zong.core.music.MeasureSide.Right;
import static com.xenoage.zong.core.music.barline.Barline.barline;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.time.TimeType.timeSenzaMisura;
import static com.xenoage.zong.core.music.time.TimeType.timeType;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.atStaff;
import static com.xenoage.zong.core.text.UnformattedText.ut;
import static com.xenoage.zong.io.musicxml.in.readers.ChordReader.readChord;
import static com.xenoage.zong.io.musicxml.in.readers.FontInfoReader.readFontInfo;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPosition;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPositioning;

import java.util.List;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.VoiceElementWrite;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Pedal.Type;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.layout.PageBreak;
import com.xenoage.zong.core.music.layout.SystemBreak;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.musicxml.in.util.MusicReaderException;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlBackup;
import com.xenoage.zong.musicxml.types.MxlBarline;
import com.xenoage.zong.musicxml.types.MxlClef;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlDirectionType;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlFormattedText;
import com.xenoage.zong.musicxml.types.MxlForward;
import com.xenoage.zong.musicxml.types.MxlInstrument;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.MxlMetronome;
import com.xenoage.zong.musicxml.types.MxlNormalTime;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPedal;
import com.xenoage.zong.musicxml.types.MxlPrint;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.MxlSound;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.types.MxlTime;
import com.xenoage.zong.musicxml.types.MxlWedge;
import com.xenoage.zong.musicxml.types.MxlWords;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintAttributes;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.attributes.MxlRepeat;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent.MxlDirectionTypeContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.choice.MxlNoteContent.MxlNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent.MxlTimeContentType;
import com.xenoage.zong.musicxml.types.enums.MxlBackwardForward;
import com.xenoage.zong.musicxml.types.enums.MxlRightLeftMiddle;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;
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
		MusicReaderContext context = new MusicReaderContext(score, new MusicReaderSettings(
			ignoreErrors));

		//read the parts
		It<MxlPart> mxlParts = it(doc.getParts());
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
					new VoiceElementWrite(score.getVoice(mp), mp, new Rest(measureDuration), false).execute();
				}
			}
		}
	}

	/**
	 * Reads the given measure element.
	 */
	private static MusicReaderContext readMeasure(MusicReaderContext context, MxlMeasure mxlMeasure,
		int measureIndex) {
		//begin a new measure
		context.beginNewMeasure(measureIndex);
		//list all elements
		List<MxlMusicDataContent> content = mxlMeasure.getMusicData().getContent();
		for (int i : range(content)) {
			MxlMusicDataContent mxlMDC = content.get(i);
			switch (mxlMDC.getMusicDataContentType()) {
				case Note: {
					MxlNote mxlNote = ((MxlNote) mxlMDC);
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
					//collect all directly following notes which have a chord-element
					List<MxlNote> mxlNotes = alist(mxlNote);
					for (int i2 = i + 1; i2 < content.size(); i2++) {
						boolean found = false;
						MxlMusicDataContent mxlMDC2 = content.get(i2);
						if (mxlMDC2.getMusicDataContentType() == MxlMusicDataContentType.Note) {
							MxlNote mxlNote2 = (MxlNote) mxlMDC2;
							if (mxlNote2.getContent().getNoteContentType() == MxlNoteContentType.Normal) {
								if (((MxlNormalNote) mxlNote2.getContent()).getFullNote().isChord()) {
									mxlNotes.add(mxlNote2);
									i++;
									found = true;
								}
							}
						}
						if (!found)
							break;
					}
					readChord(context, mxlNotes);
					break;
				}
				case Attributes:
					readAttributes(context, (MxlAttributes) mxlMDC);
					break;
				case Backup:
					readBackup(context, (MxlBackup) mxlMDC);
					break;
				case Forward:
					readForward(context, (MxlForward) mxlMDC);
					break;
				case Print:
					readPrint(context, (MxlPrint) mxlMDC);
					break;
				case Direction:
					readDirection(context, (MxlDirection) mxlMDC);
					break;
				case Barline:
					readBarline(context, (MxlBarline) mxlMDC);
					break;
			}
		}
		return context;
	}

	/**
	 * Reads the given attributes element.
	 */
	private static MusicReaderContext readAttributes(MusicReaderContext context,
		MxlAttributes mxlAttributes) {

		//divisions
		Integer divisions = mxlAttributes.getDivisions();
		if (divisions != null) {
			context.setDivisions(divisions);
		}

		//key signature
		MxlKey mxlKey = mxlAttributes.getKey();
		if (mxlKey != null) {
			//only the fifths element is supported
			int mxlFifths = mxlKey.fifths;
			//write to column header (TODO: attribute "number" for single staves)
			Mode mode = getEnumValue(mxlKey.mode, Mode.values());
			Key key = new TraditionalKey(mxlFifths, mode);
			context.writeColumnElement(key);
		}

		//time signature
		MxlTime mxlTime = mxlAttributes.getTime();
		if (mxlTime != null) {
			Time time = null;
			MxlTimeContentType type = mxlTime.getContent().getTimeContentType();
			if (type == MxlTimeContentType.SenzaMisura) {
				//senza misura
				time = new Time(timeSenzaMisura);
			}
			else if (type == MxlTimeContentType.NormalTime) {
				//at the moment we read only one beats/beat-type
				//currently we accept only integers > 0
				MxlNormalTime mxlNormalTime = (MxlNormalTime) mxlTime.getContent();
				time = new Time(timeType(mxlNormalTime.getBeats(), mxlNormalTime.getBeatType()));
			}
			//write to column header (TODO: attribute "number" for single staves)
			if (time != null) {
				context.writeColumnElement(time);
			}
		}

		//clefs
		for (MxlClef mxlClef : mxlAttributes.getClefs()) {
			ClefType clefType = getEnumValue(mxlClef.getSign(), ClefType.values());
			Clef clef = (clefType != null ? new Clef(clefType) : null);
			//staff (called "number" in MusicXML), first staff is default
			int staff = mxlClef.getNumber() - 1;
			//add to staff
			if (clef != null) {
				context.writeMeasureElement(clef, staff);
			}
		}

		/* TODO: transposition changes ~= instrument changes
		//transposition changes
		MxlTranspose mxlTranspose = mxlAttributes.getTranspose();
		if (mxlTranspose != null)
		{
			int chromatic = mxlTranspose.getChromatic();
			Transpose transpose = new Transpose(chromatic);
			//write to all staves of this part
			for (int staff = 0; staff < context.getPartStavesIndices().getCount(); staff++)
			{
				writeNoVoiceElement(transpose, staff);
		  }
		}
		*/

		return context;
	}

	/**
	 * Reads the given backup element.
	 */
	private static void readBackup(MusicReaderContext context, MxlBackup mxlBackup) {
		//duration
		Fraction duration = readDuration(context, mxlBackup.getDuration()).invert();
		//move cursor
		context.moveCurrentBeat(duration);
	}

	/**
	 * Reads the given forward element.
	 */
	private static void readForward(MusicReaderContext context, MxlForward mxlForward) {
		//duration
		Fraction duration = readDuration(context, mxlForward.getDuration());
		//move cursor
		context.moveCurrentBeat(duration);
	}

	/**
	 * Returns the duration as a {@link Fraction} from the given duration in divisions.
	 */
	public static Fraction readDuration(MusicReaderContext context, int duration) {
		if (duration == 0) {
			throw new RuntimeException("Element has a duration of 0.");
		}
		Fraction ret = fr(duration, 4 * context.getDivisions());
		return ret;
	}

	/**
	 * Reads the given print element.
	 */
	private static void readPrint(MusicReaderContext context, MxlPrint mxlPrint) {
		MxlPrintAttributes mxlPA = mxlPrint.getPrintAttributes();

		//system and page break
		Boolean newSystem = mxlPA.getNewSystem();
		SystemBreak systemBreak = (newSystem == null ? null : (newSystem ? SystemBreak.NewSystem
			: SystemBreak.NoNewSystem));
		Boolean newPage = mxlPA.getNewPage();
		PageBreak pageBreak = (newPage == null ? null : (newPage ? PageBreak.NewPage
			: PageBreak.NoNewPage));
		if (systemBreak != null || pageBreak != null) {
			context.writeColumnElement(new Break(pageBreak, systemBreak));
		}

		//we assume that custom system layout information is just used in combination with
		//forced system/page breaks. so we ignore system-layout elements which are not combined
		//with system/page breaks.
		//the first measure of a score is also ok.
		if (context.getMp().measure == 0 || systemBreak == SystemBreak.NewSystem ||
			pageBreak == PageBreak.NewPage) {

			//first page or new page?
			boolean isPageBreak = pageBreak == PageBreak.NewPage;
			boolean isPageStarted = (context.getMp().measure == 0 || isPageBreak);
			if (isPageBreak) {
				//increment page index
				context.incPageIndex();
			}

			//first system or new system?
			boolean isSystemBreak = isPageBreak || systemBreak == SystemBreak.NewSystem;
			if (isSystemBreak) {
				//increment system index 
				context.incSystemIndex();
			}

			//read system layout, if there
			MxlLayout mxlLayout = mxlPrint.getLayout();
			if (mxlLayout != null) {
				MxlSystemLayout mxlSystemLayout = mxlPrint.getLayout().getSystemLayout();
				if (mxlSystemLayout != null) {
					SystemLayoutReader.Value sl = SystemLayoutReader
						.read(mxlSystemLayout, context.getTenthMm());
					SystemLayout systemLayout = sl.systemLayout;
	
					//for first systems on a page, use top-system-distance
					if (isPageStarted && sl.topSystemDistance != null) {
						systemLayout = systemLayout.withDistance(sl.topSystemDistance);
					}
	
					//apply values
					context.getScore().getHeader().setSystemLayout(context.getSystemIndex(), systemLayout);
				}
			}

		}

		//GOON
		/*
		//staff layouts
		for (MxlStaffLayout mxlStaffLayout : mxlPrint.getLayout().getStaffLayouts()) {
			int staffIndex = mxlStaffLayout.getNumberNotNull() - 1;
			context = context.withScore(ScoreController.withSystemStaffLayout(context.getScore(),
				context.getSystemIndex(), context.getPartStavesIndices().getStart() + staffIndex,
				readStaffLayout(mxlStaffLayout, context.getTenthMm()).staffLayout));
		}
		*/
	}

	/**
	 * Reads the given barline element.
	 * Currently only left and right barlines are supported.
	 */
	private static void readBarline(MusicReaderContext context, MxlBarline mxlBarline) {
		MxlRightLeftMiddle location = mxlBarline.getLocation();
		MxlRepeat repeat = mxlBarline.getRepeat();
		int measureIndex = context.getMp().measure;
		BarlineStyle style = null;
		if (mxlBarline.getBarStyle() != null)
			style = BarlineStyleReader.read(mxlBarline.getBarStyle().getBarStyle());
		if (repeat != null) {
			//repeat barline
			if (location == MxlRightLeftMiddle.Left) {
				//left barline
				if (repeat.getDirection() == MxlBackwardForward.Forward) {
					style = notNull(style, BarlineStyle.HeavyLight);
					new ColumnElementWrite(barlineForwardRepeat(style),
						context.getScore().getColumnHeader(measureIndex), null, Left).execute();
				}
			}
			else if (location == MxlRightLeftMiddle.Right) {
				//right barline
				if (repeat.getDirection() == MxlBackwardForward.Backward) {
					style = notNull(style, BarlineStyle.LightHeavy);
					int times = notNull(repeat.getTimes(), 1).intValue();
					new ColumnElementWrite(barlineBackwardRepeat(style, times),
						context.getScore().getColumnHeader(measureIndex), null, Right).execute();
				}
			}
		}
		else {
			//regular barline
			style = notNull(style, BarlineStyle.Regular);
			if (location == MxlRightLeftMiddle.Left) {
				//left barline
				new ColumnElementWrite(barline(style), context.getScore().getColumnHeader(measureIndex),
					null, Left).execute();
			}
			else if (location == MxlRightLeftMiddle.Right) {
				//right barline
				new ColumnElementWrite(barline(style), context.getScore().getColumnHeader(measureIndex),
					null, Right).execute();
			}
		}
	}

	/**
	 * Reads the given direction element.
	 */
	private static void readDirection(MusicReaderContext context,
		MxlDirection mxlDirection) {
		//staff
		int staff = notNull(mxlDirection.getStaff(), 1) - 1;

		//direction-types
		Direction direction = null;
		FontInfo defaultFont = context.getScore().getFormat().lyricFont;
		for (MxlDirectionType mxlType : mxlDirection.getDirectionTypes()) {
			MxlDirectionTypeContent mxlDTC = mxlType.getContent();
			MxlDirectionTypeContentType mxlDTCType = mxlDTC.getDirectionTypeContentType();
			switch (mxlDTCType) {
				case Dynamics: {
					//dynamics
					MxlDynamics mxlDynamics = (MxlDynamics) mxlDTC;
					DynamicsType type = mxlDynamics.getElement();
					Positioning positioning = readPositioning(mxlDynamics.getPrintStyle().getPosition(),
						mxlDynamics.getPlacement(), mxlDirection.getPlacement(), context.getTenthMm(),
						context.getStaffLinesCount(staff));
					Dynamics dynamics = new Dynamics(type);
					dynamics.setPositioning(positioning);
					context.writeMeasureElement(dynamics, staff);
					break;
				}
				case Metronome: {
					//metronome
					MxlMetronome mxlMetronome = (MxlMetronome) mxlDTC;
					FontInfo fontInfo = null;
					Position position = null;
					MxlPrintStyle mxlPrintStyle = mxlMetronome.getPrintStyle();
					if (mxlPrintStyle != null) {
						fontInfo = readFontInfo(mxlPrintStyle.getFont(), defaultFont);
						position = readPosition(mxlPrintStyle.getPosition(),
							context.getTenthMm(), context.getStaffLinesCount(staff));
					}
					direction = new Tempo(mxlMetronome.getBeatUnit().getDuration(), mxlMetronome.getPerMinute()); //text: TODO
					//direction.setFont(fontInfo); //TODO
					direction.setPositioning(position);
					break;
				}
				case Pedal: {
					//pedal
					MxlPedal mxlPedal = (MxlPedal) mxlDTC;
					Pedal.Type type = null;
					switch (mxlPedal.getType()) {
						case Start:
							type = Type.Start;
							break;
						case Stop:
							type = Type.Stop;
							break;
					}
					if (type != null) {
						Pedal pedal = new Pedal(type);
						pedal.setPositioning(readPosition(mxlPedal.getPrintStyle(),
							context.getTenthMm(), context.getStaffLinesCount(staff)));
						context.writeMeasureElement(pedal, staff);
					}
					break;
				}
				case Wedge: {
					//wedge
					MxlWedge mxlWedge = (MxlWedge) mxlDTC;
					int number = mxlWedge.getNumber();
					Position pos = readPosition(mxlWedge.getPosition(), context.getTenthMm(),
						context.getStaffLinesCount(staff));
					switch (mxlWedge.getType()) {
						case Crescendo:
							Wedge crescendo = new Wedge(WedgeType.Crescendo);
							crescendo.setPositioning(pos);
							context.writeMeasureElement(crescendo, staff);
							context.openWedge(number, crescendo);
							break;
						case Diminuendo:
							Wedge diminuendo = new Wedge(WedgeType.Diminuendo);
							diminuendo.setPositioning(pos);
							context.writeMeasureElement(diminuendo, staff);
							context.openWedge(number, diminuendo);
							break;
						case Stop:
							Wedge wedge = context.closeWedge(number);
							if (wedge == null) {
								if (false == context.getSettings().isIgnoringErrors())
									throw new RuntimeException("Wedge " + (number + 1) + " is not open!");
							}
							else
								context.writeMeasureElement(wedge.getWedgeEnd(), staff);
							break;
					}
					break;
				}
				case Words: {
					//words (currently only one element is supported)
					if (direction == null) {
						MxlWords mxlWords = (MxlWords) mxlDTC;
						MxlFormattedText mxlFormattedText = mxlWords.getFormattedText();
						FontInfo fontInfo = readFontInfo(mxlFormattedText.getPrintStyle().getFont(),
							defaultFont);
						Position position = readPosition(mxlFormattedText.getPrintStyle().getPosition(),
							context.getTenthMm(), context.getStaffLinesCount(staff));
						direction = new Words(ut(mxlFormattedText.getValue()));
						//direction.setFont(fontInfo); //TODO
						direction.setPositioning(position);
					}
					break;
				}
			}
		}

		//sound for words: tempo
		MxlSound mxlSound = mxlDirection.getSound();
		if (mxlSound != null && mxlSound.getTempo() != null && direction instanceof Words) {
			Words words = (Words) direction;
			//always expressed in quarter notes per minute
			int quarterNotesPerMinute = mxlSound.getTempo().intValue();
			//convert words into a tempo direction
			direction = new Tempo(fr(1, 4), quarterNotesPerMinute); //TODO: words.getText()
			//direction.setFontInfo(words.getFontInfo()); //TODO
			direction.setPositioning(words.getPositioning());
		}

		//write direction to score
		if (direction != null) {
			if (direction instanceof ColumnElement) {
				context.writeColumnElement((ColumnElement) direction);
			}
			else {
				context.writeMeasureElement(direction, staff);
			}
		}
	}

}
