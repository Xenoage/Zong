package com.xenoage.zong.musicxml.types;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlCueNote;
import com.xenoage.zong.musicxml.types.choice.MxlGraceNote;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.choice.MxlNoteContent;
import com.xenoage.zong.musicxml.types.enums.MxlNoteTypeValue;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML note.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "accidental,time-modification,notehead,"
	+ "x-position,font,color,printout,dynamics,end-dynamics,attack,release,time-only,pizzicato",
	children = "beam,editorial-voice,notations,lyric", partly = "type,dot")
@AllArgsConstructor @Getter @Setter
public final class MxlNote
	implements MxlMusicDataContent {

	public static final String elemName = "note";

	@NonNull private MxlNoteContent content;
	@MaybeNull private MxlInstrument instrument;
	@MaybeNull private MxlEditorialVoice editorialVoice;
	@MaybeNull private MxlNoteTypeValue noteType;
	private int dots;
	@MaybeNull private MxlStem stem;
	@MaybeNull private Integer staff;
	@MaybeNull private List<MxlBeam> beams;
	@MaybeNull private List<MxlNotations> notations;
	@MaybeNull private List<MxlLyric> lyrics;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Note;
	}

	@NonNull public static MxlNote read(XmlReader reader) {
		MxlNoteContent content = null;
		MxlInstrument instrument = null;
		MxlEditorialVoice editorialVoice = new MxlEditorialVoice();
		MxlNoteTypeValue noteType = null;
		int dots = 0;
		MxlStem stem = null;
		Integer staff = null;
		List<MxlBeam> beams = null;
		List<MxlNotations> notations = null;
		List<MxlLyric> lyrics = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			//first element determines note content
			if (content == null) {
				if (n.equals(MxlGraceNote.elemName))
					content = MxlGraceNote.read(reader);
				else if (n.equals(MxlCueNote.elemName))
					content = MxlCueNote.read();
				else
					content = MxlNormalNote.read();
			}
			//read content of child elements
			if (n.equals(MxlStem.elemName))
				stem = MxlStem.read(reader);
			else if (n.equals("staff"))
				staff = reader.getTextIntNotNull();
			else if (n.equals(MxlBeam.elemName)) {
				if (beams == null)
					beams = new ArrayList<MxlBeam>();
				beams.add(MxlBeam.read(reader));
			}
			else if (n.equals(MxlInstrument.elemName))
				instrument = MxlInstrument.read(reader);
			else if (n.equals(MxlNotations.elemName)) {
				if (notations == null)
					notations = new ArrayList<MxlNotations>();
				notations.add(MxlNotations.read(reader));
			}
			else if (n.equals(MxlLyric.elemName)) {
				if (lyrics == null)
					lyrics = new ArrayList<MxlLyric>();
				lyrics.add(MxlLyric.read(reader));
			}
			else if (n.equals("type"))
				noteType = MxlNoteTypeValue.read(reader.getText());
			else if (n.equals("dot"))
				dots++;
			else {
				boolean read = content.readElement(reader);
				if (!read)
					editorialVoice.readElement(reader);
			}
			reader.closeElement();
		}
		content.check(reader);
		if (false == editorialVoice.isUsed())
			editorialVoice = null;
		return new MxlNote(content, instrument, editorialVoice, noteType, dots, stem, staff, beams,
			notations, lyrics);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		content.write(writer);
		if (instrument != null)
			instrument.write(writer);
		if (editorialVoice != null)
			editorialVoice.write(writer);
		if (noteType != null)
			writer.writeElementText("type", noteType.write());
		for (int i = 0; i < dots; i++)
			writer.writeElementEmpty("dot");
		if (stem != null)
			stem.write(writer);
		writer.writeElementText("staff", staff);
		if (beams != null) {
			for (MxlBeam beam : beams)
				beam.write(writer);
		}
		if (notations != null) {
			for (MxlNotations n : notations)
				n.write(writer);
		}
		if (lyrics != null) {
			for (MxlLyric lyric : lyrics)
				lyric.write(writer);
		}
		writer.writeElementEnd();
	}

}
