package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.math.Fraction.fr;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.direction.Coda;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.NavigationMarker;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Pedal.Type;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlDirectionType;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlFormattedText;
import com.xenoage.zong.musicxml.types.MxlMetronome;
import com.xenoage.zong.musicxml.types.MxlPedal;
import com.xenoage.zong.musicxml.types.MxlSound;
import com.xenoage.zong.musicxml.types.MxlWedge;
import com.xenoage.zong.musicxml.types.MxlWords;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent.MxlDirectionTypeContentType;

/**
 * Reads a {@link Direction} from a {@link MxlDirection}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class DirectionReader {

	private final MxlDirection mxlDirection;
	
	private MxlDirectionTypeContent currentMxlDirType;
	private int staff;
	private StaffDetails staffDetails;
	//TODO private FontInfo defaultFont;
	
	//TODO: add support for multiple direction-types within a single MusicXML direction
	public void readToContext(Context context) {
		
		//staff
		staff = notNull(mxlDirection.getStaff(), 1) - 1;
		staffDetails = StaffDetails.fromContext(context, staff);
		
		//TODO defaultFont = context.getScore().getFormat().lyricFont;
		
		//direction-types
		Direction direction = null;
		for (MxlDirectionType mxlType : mxlDirection.getDirectionTypes()) {
			currentMxlDirType = mxlType.getContent();
			MxlDirectionTypeContentType mxlDTCType = currentMxlDirType.getDirectionTypeContentType();
			switch (mxlDTCType) {
				
				case Coda: {
					Coda coda = readCoda();
					context.writeColumnElement(coda);
					break;
				}
				
				case Dynamics: {
					Dynamics dynamics = readDynamics();
					context.writeMeasureElement(dynamics, staff);
					break;
				}
				
				case Metronome: {
					direction = readMetronome();
					break;
				}
				
				case Pedal: {
					Pedal pedal = readPedal();
					if (pedal != null)
						context.writeMeasureElement(pedal, staff);
					break;
				}
				
				case Segno: {
					Segno segno = readSegno();
					context.writeColumnElement(segno);
					break;
				}
				
				case Wedge: {
					//wedge
					readWedgeToContext(context);
					break;
				}
				
				case Words: {
					//words (currently only one element is supported)
					if (direction == null)
						direction = readWords(context.getSettings().getTextReader());
					break;
				}
				
				default:
			}
		}

		direction = readSound(direction);

		//write direction to score
		//TODO: find out if measure direction or column direction.
		//currently, we write a column element only for tempo or navigation markers
		if (direction != null) {
			if (direction instanceof Tempo || direction instanceof NavigationMarker)
				context.writeColumnElement((ColumnElement) direction);
			else
				context.writeMeasureElement(direction, staff);
		}
	}
	
	private Coda readCoda() {
		Coda coda = new Coda();
		coda.setPositioning(readPositioning());
		return coda;
	}
	
	private Dynamics readDynamics() {
		MxlDynamics mxlDynamics = (MxlDynamics) currentMxlDirType;
		DynamicsType type = mxlDynamics.getElement();
		Dynamics dynamics = new Dynamics(type);
		dynamics.setPositioning(readPositioning());
		return dynamics;
	}
	
	private Direction readMetronome() {
		MxlMetronome mxlMetronome = (MxlMetronome) currentMxlDirType;
		
		//compute base beat
		Fraction baseBeat = mxlMetronome.getBeatUnit().getDuration();
		baseBeat = DurationInfo.getDuration(baseBeat, mxlMetronome.getDotsCount());
		
		Tempo tempo = new Tempo(baseBeat, mxlMetronome.getPerMinute()); //text: TODO
		//TODO tempo.setFont(FontInfoReader.read(currentMxlDirection, defaultFont));
		tempo.setPositioning(readPositioning());
		return tempo;
	}
	
	@MaybeNull private Pedal readPedal() {
		MxlPedal mxlPedal = (MxlPedal) currentMxlDirType;
		Pedal.Type type = null;
		switch (mxlPedal.getType()) {
			case Start:
				type = Type.Start;
				break;
			case Stop:
				type = Type.Stop;
				break;
			default:
				return null;
		}
		Pedal pedal = new Pedal(type);
		pedal.setPositioning(readPositioning());
		return pedal;
	}
	
	private Segno readSegno() {
		Segno segno = new Segno();
		segno.setPositioning(readPositioning());
		return segno;
	}
	
	private void readWedgeToContext(Context context) {
		MxlWedge mxlWedge = (MxlWedge) currentMxlDirType;
		int number = mxlWedge.getNumber();
		switch (mxlWedge.getType()) {
			case Crescendo:
				Wedge crescendo = new Wedge(WedgeType.Crescendo);
				crescendo.setPositioning(readPositioning());
				context.writeMeasureElement(crescendo, staff);
				context.openWedge(number, crescendo);
				break;
			case Diminuendo:
				Wedge diminuendo = new Wedge(WedgeType.Diminuendo);
				diminuendo.setPositioning(readPositioning());
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
	}
	
	@MaybeNull private Words readWords(TextReader textReader) {
		MxlWords mxlWords = (MxlWords) currentMxlDirType;
		MxlFormattedText mxlFormattedText = mxlWords.getFormattedText();
		if (mxlFormattedText.getValue().length() == 0)
			return null;
		Words words = new Words(textReader.readText(mxlFormattedText));
		words.setPositioning(readPositioning());
		//TODO FontInfo fontInfo = FontInfoReader.read(currentMxlDirection, defaultFont); 
		//TODO words.setFont(fontInfo);
		return words;
	}
	
	private Positioning readPositioning() {
		return new PositioningReader(staffDetails).readFromAny(currentMxlDirType, mxlDirection);
	}
	
	private Direction readSound(Direction direction) {
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
		return direction;
	}
	
}
