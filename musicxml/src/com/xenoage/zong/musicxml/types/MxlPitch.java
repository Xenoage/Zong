package com.xenoage.zong.musicxml.types;

import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musicxml.util.InvalidCore.invalidCore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;

/**
 * MusicXML pitch.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlPitch
	implements MxlFullNoteContent {

	public static final String elemName = "pitch";

	@NonNull private final Pitch pitch;


	@Override public MxlFullNoteContentType getFullNoteContentType() {
		return MxlFullNoteContentType.Pitch;
	}

	@NonNull public static MxlPitch read(XmlReader reader) {
		int step = 0, alter = 0, octave = 0;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("alter"))
				alter = Math.round(reader.getTextFloatNotNull()); //microtones are not supported
			else if (n.equals("octave"))
				octave = reader.getTextIntNotNull();
			else if (n.equals("step"))
				step = readStep(reader);
			reader.closeElement();
		}
		return new MxlPitch(pi(step, alter, octave));
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("step", writeStep(pitch.getStep()));
		writer.writeElementText("alter", pitch.getAlter());
		writer.writeElementText("octave", pitch.getOctave());
		writer.writeElementEnd();
	}

	private static int readStep(XmlReader reader) {
		String s = reader.getTextNotNull();
		switch (s.charAt(0)) {
			case 'C':
				return 0;
			case 'D':
				return 1;
			case 'E':
				return 2;
			case 'F':
				return 3;
			case 'G':
				return 4;
			case 'A':
				return 5;
			case 'B':
				return 6;
		}
		throw reader.dataException("unknown step: " + s);
	}

	private static char writeStep(int step) {
		switch (step) {
			case 0:
				return 'C';
			case 1:
				return 'D';
			case 2:
				return 'E';
			case 3:
				return 'F';
			case 4:
				return 'G';
			case 5:
				return 'A';
			case 6:
				return 'B';
		}
		throw invalidCore(step);
	}

}
