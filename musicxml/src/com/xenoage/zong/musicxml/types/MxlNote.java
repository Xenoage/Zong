package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.Parse.parseInt;
import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLReader.text;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import java.util.List;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlNoteContent;
import com.xenoage.zong.musicxml.types.enums.MxlNoteTypeValue;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML note.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="accidental,time-modification,notehead," +
	"x-position,font,color,printout,dynamics,end-dynamics,attack,release,time-only,pizzicato",
	children="beam,editorial-voice,notations,lyric", partly="type,dot")
public final class MxlNote
	implements MxlMusicDataContent
{
	
	public static final String ELEM_NAME = "note";
	
	@NeverNull public final MxlNoteContent content;
	@MaybeNull public final MxlInstrument instrument;
	@NeverNull public final MxlEditorialVoice editorialVoice;
	@MaybeNull public final MxlNoteTypeValue noteType;
	public final int dots;
	@MaybeNull public final MxlStem stem;
	@MaybeNull public final Integer staff;
	@MaybeEmpty public final PVector<MxlBeam> beams;
	@MaybeEmpty public final PVector<MxlNotations> notations;
	@MaybeEmpty public final PVector<MxlLyric> lyrics;
	
	public static final PVector<MxlBeam> beamsEmpty = pvec();
	public static final PVector<MxlNotations> notationsEmpty = pvec();
	public static final PVector<MxlLyric> lyricsEmpty = pvec();
	
	
	public MxlNote(MxlNoteContent content, MxlInstrument instrument,
		MxlEditorialVoice editorialVoice, MxlNoteTypeValue noteType, int dots, MxlStem stem, Integer staff,
		PVector<MxlBeam> beams, PVector<MxlNotations> notations, PVector<MxlLyric> lyrics)
	{
		this.content = content;
		this.instrument = instrument;
		this.editorialVoice = editorialVoice;
		this.noteType = noteType;
		this.dots = dots;
		this.stem = stem;
		this.staff = staff;
		this.beams = beams;
		this.notations = notations;
		this.lyrics = lyrics;
	}
	
	
	@Override public MxlMusicDataContentType getMusicDataContentType()
	{
		return MxlMusicDataContentType.Note;
	}
	
	
	@NeverNull public static MxlNote read(Element e)
	{
		MxlNoteContent content = readNoteContent(e);
		MxlInstrument instrument = null;
		MxlEditorialVoice editorialVoice = MxlEditorialVoice.read(e);
		MxlNoteTypeValue noteType = null;
		int dots = 0;
		List<Element> children = elements(e);
		MxlStem stem = null;
		Integer staff = null;
		PVector<MxlBeam> beams = beamsEmpty;
		PVector<MxlNotations> notations = notationsEmpty;
		PVector<MxlLyric> lyrics = lyricsEmpty;
		for (Element child : children)
		{
			String n = child.getNodeName();
			switch (n.charAt(0)) //performance
			{
				case 's':
					if (n.equals(MxlStem.ELEM_NAME))
						stem = MxlStem.read(child);
					else if (n.equals("staff"))
						staff = parseInt(child);
					break;
				case 'b':
					if (n.equals(MxlBeam.ELEM_NAME))
						beams = beams.plus(MxlBeam.read(child));
					break;
				case 'i':
					if (n.equals(MxlInstrument.ELEM_NAME))
						instrument = MxlInstrument.read(child);
					break;
				case 'n':
					if (n.equals(MxlNotations.elemName))
						notations = notations.plus(MxlNotations.read(child));
					break;
				case 'l':
					if (n.equals(MxlLyric.ELEM_NAME))
						lyrics = lyrics.plus(MxlLyric.read(child));
					break;
				case 't':
					if (n.equals("type"))
						noteType = MxlNoteTypeValue.read(text(child));
					break;
				case 'd':
					if (n.equals("dot"))
						dots++;
					break;
			}
		}
		return new MxlNote(content, instrument, editorialVoice, noteType, dots,
			stem, staff, beams, notations, lyrics);
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		content.write(e);
		if (instrument != null)
			instrument.write(e);
		editorialVoice.write(e);
		if (noteType != null)
			addElement("type", noteType.write(), e);
		for (int i = 0; i < dots; i++)
			addElement("dot",  e);
		if (stem != null)
			stem.write(e);
		addElement("staff", staff, e);
		for (MxlBeam beam : beams)
			beam.write(e);
		for (MxlNotations n : notations)
			n.write(e);
		for (MxlLyric lyric : lyrics)
			lyric.write(e);
	}
	
	
	private static MxlNoteContent readNoteContent(Element e)
	{
		Element firstChild = element(e);
		String n = firstChild.getNodeName();
		if (n.equals("grace"))
			return MxlGraceNote.read(e);
		else if (n.equals("cue"))
			return MxlCueNote.read(e);
		else
			return MxlNormalNote.read(e);
	}

}
