package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintAttributes;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML print.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="measure-layout,measure-numbering,part-name-display," +
	"part-abbreviation-display", children="layout,print-attributes")
public final class MxlPrint
	implements MxlMusicDataContent
{
	
	public static final String ELEM_NAME = "print";
	public static final MxlPrint empty = new MxlPrint(MxlLayout.empty, MxlPrintAttributes.empty);
	
	@NeverNull private final MxlLayout layout;
	@NeverNull private final MxlPrintAttributes printAttributes;
	
	
	public MxlPrint(MxlLayout layout, MxlPrintAttributes printAttributes)
	{
		this.layout = layout;
		this.printAttributes = printAttributes;
	}

	
	@NeverNull public MxlLayout getLayout()
	{
		return layout;
	}

	
	@NeverNull public MxlPrintAttributes getPrintAttributes()
	{
		return printAttributes;
	}
	
	
	@Override public MxlMusicDataContentType getMusicDataContentType()
	{
		return MxlMusicDataContentType.Print;
	}
	
	
	@NeverNull public static MxlPrint read(Element e)
	{
		MxlLayout layout = MxlLayout.read(e);
		MxlPrintAttributes printAttributes = MxlPrintAttributes.read(e);
		if (layout != MxlLayout.empty || printAttributes != MxlPrintAttributes.empty)
			return new MxlPrint(layout, printAttributes);
		else
			return empty;
	}
	
	
	@Override public void write(Element parent)
	{
		if (this != empty)
		{
			Element e = addElement(ELEM_NAME, parent);
			layout.write(e);
			printAttributes.write(e);
		}
	}
	
	
	

}
