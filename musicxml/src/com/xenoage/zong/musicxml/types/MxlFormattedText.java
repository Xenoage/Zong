package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.textUntrimmed;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlVAlign;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML formatted-text.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="text-decoration,text-rotation," +
	"letter-spacing,line-height,xml:lang,text-direction,enclosure")
public final class MxlFormattedText
{
	
	@NeverNull private final String value;
	@MaybeNull private final MxlLeftCenterRight justify;
	@MaybeNull private final MxlLeftCenterRight hAlign;
	@MaybeNull private final MxlVAlign vAlign;
	@NeverNull private final MxlPrintStyle printStyle;
	
	
	public MxlFormattedText(String value, MxlLeftCenterRight justify,
		MxlLeftCenterRight hAlign, MxlVAlign vAlign, MxlPrintStyle printStyle)
	{
		this.value = value;
		this.justify = justify;
		this.hAlign = hAlign;
		this.vAlign = vAlign;
		this.printStyle = printStyle;
	}
	
	
	@NeverNull public String getValue()
	{
		return value;
	}
	
	
	@MaybeNull public MxlLeftCenterRight getJustify()
	{
		return justify;
	}
	
	
	@MaybeNull public MxlLeftCenterRight getHAlign()
	{
		return hAlign;
	}
	
	
	@MaybeNull public MxlVAlign getVAlign()
	{
		return vAlign;
	}
	
	
	@NeverNull public MxlPrintStyle getPrintStyle()
	{
		return printStyle;
	}
	
	
	@NeverNull public static MxlFormattedText read(Element e)
	{
		return new MxlFormattedText(textUntrimmed(e), MxlLeftCenterRight.read(e, "justify"),
			MxlLeftCenterRight.read(e, "halign"), MxlVAlign.read(e), MxlPrintStyle.read(e));
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(value);
		if (justify != null)
			justify.write(e, "justify");
		if (hAlign != null)
			hAlign.write(e, "halign");
		if (vAlign != null)
			vAlign.write(e);
		printStyle.write(e);
	}
	
	

}
