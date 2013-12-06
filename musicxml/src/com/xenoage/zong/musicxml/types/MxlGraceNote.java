package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlNoteContent;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML grace note content.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="tie,steal-time-previous,steal-time-following,make-time",
	children="full-note")
public final class MxlGraceNote
	implements MxlNoteContent
{
	
	@NeverNull private final MxlFullNote fullNote;
	@MaybeNull private final Boolean slash;
	
	
	public MxlGraceNote(MxlFullNote fullNote, Boolean slash)
	{
		this.fullNote = fullNote;
		this.slash = slash;
	}

	
	@Override @NeverNull public MxlFullNote getFullNote()
	{
		return fullNote;
	}
	
	
	@MaybeNull public Boolean getSlash()
	{
		return slash;
	}
	
	
	@Override public MxlNoteContentType getNoteContentType()
	{
		return MxlNoteContentType.Grace;
	}
	
	
	@NeverNull public static MxlGraceNote read(Element e)
	{
		return new MxlGraceNote(MxlFullNote.read(e),
			MxlYesNo.readNull(attribute(e, "slash"), e));
	}
	
	
	@Override public void write(Element e)
	{
		Element child = addElement("grace", e);
		if (slash != null)
			addAttribute(child, "slash", MxlYesNo.write(slash));
		fullNote.write(e);
	}
	

}
