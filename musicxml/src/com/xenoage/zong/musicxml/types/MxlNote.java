package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.*;
import com.xenoage.zong.musicxml.types.enums.MxlNoteTypeValue;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
			//but, be tolerant for errors, and also accept late grace or cue elements
			if (n.equals(MxlGraceNote.elemName)) {
				MxlGraceNote graceNote = MxlGraceNote.read(reader);
				if (content instanceof MxlNormalNote) //grace element too late, but accept it
					graceNote.setFullNote(((MxlNormalNote) content).getFullNote());
				content = graceNote;
			}
			else if (n.equals(MxlCueNote.elemName)) {
				MxlCueNote cueNote = MxlCueNote.read();
				if (content instanceof MxlNormalNote) //cue element too late, but accept it
					cueNote.setFullNote(((MxlNormalNote) content).getFullNote());
				content = cueNote;
			}
			else if (content == null) {
				content = MxlNormalNote.read();
			}
			//read content of child elements
			switch (n) {
				case MxlStem.elemName:
					stem = MxlStem.read(reader);
					break;
				case "staff":
					staff = reader.getTextIntNotNull();
					break;
				case MxlBeam.elemName:
					if (beams == null)
						beams = new ArrayList<>();
					beams.add(MxlBeam.read(reader));
					break;
				case MxlInstrument.elemName:
					instrument = MxlInstrument.read(reader);
					break;
				case MxlNotations.elemName:
					if (notations == null)
						notations = new ArrayList<>();
					notations.add(MxlNotations.read(reader));
					break;
				case MxlLyric.elemName:
					if (lyrics == null)
						lyrics = new ArrayList<>();
					lyrics.add(MxlLyric.read(reader));
					break;
				case "type":
					noteType = MxlNoteTypeValue.read(reader.getText());
					break;
				case "dot":
					dots++;
					break;
				default:
					boolean read = content.readElement(reader);
					if (!read)
						editorialVoice.readElement(reader);
					break;
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
