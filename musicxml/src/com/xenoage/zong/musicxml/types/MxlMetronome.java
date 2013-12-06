package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseChildIntNullTry;
import static com.xenoage.utils.xml.XMLReader.elementText;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.XMLWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlNoteTypeValue;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML metronome.
 * 
 * Currently only the first type (e.g. "♩. = 160")
 * of metronome marks is supported. The right part must
 * be a number.
 * The "♩ = ♩" form is also not supported yet.
 * All other metronome elements are ignored.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="metronome-note,metronome-relation,parentheses")
public final class MxlMetronome
	implements MxlDirectionTypeContent
{
	
	public static final String ELEM_NAME = "metronome";
	
	@NeverNull public final MxlNoteTypeValue beatUnit;
	@NeverNull public final int dotsCount;
	@NeverNull public final int perMinute;
	@NeverNull public final MxlPrintStyle printStyle;
	

	private MxlMetronome(MxlNoteTypeValue beatUnit, int dotsCount, int perMinute,
		MxlPrintStyle printStyle)
	{
		this.beatUnit = beatUnit;
		this.dotsCount = dotsCount;
		this.perMinute = perMinute;
		this.printStyle = printStyle;
	}
	
	
	@Override public MxlDirectionTypeContentType getDirectionTypeContentType()
	{
		return MxlDirectionTypeContentType.Metronome;
	}
	
	
	/**
	 * Returns null, if the given element contains an unsupported metronome type.
	 */
	@MaybeNull public static MxlMetronome read(Element e)
	{
		String sBeatUnit = elementText(e, "beat-unit");
		int dotsCount = elements(e, "beat-unit-dot").size();
		Integer perMinute = parseChildIntNullTry(e, "per-minute");
		MxlPrintStyle printStyle = MxlPrintStyle.read(e);
		if (sBeatUnit != null && perMinute != null)
		{
			MxlNoteTypeValue beatUnit = MxlNoteTypeValue.read(sBeatUnit);
			return new MxlMetronome(beatUnit, dotsCount, perMinute, printStyle);
		}
		else
		{
			return null;
		}
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = XMLWriter.addElement(ELEM_NAME, parent);
		addElement("beat-unit", beatUnit.write(), e);
		for (int i = 0; i < dotsCount; i++)
			addElement("beat-unit-dot", e);
		addElement("per-minute", perMinute, e);
		printStyle.write(e);
	}

}
