package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopChange;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML pedal.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="line")
public final class MxlPedal
	implements MxlDirectionTypeContent
{
	
	public static final String ELEM_NAME = "pedal";
	
	@NeverNull private final MxlStartStopChange type;
	@NeverNull private final MxlPrintStyle printStyle;

	
	public MxlPedal(MxlStartStopChange type, MxlPrintStyle printStyle)
	{
		this.type = type;
		this.printStyle = printStyle;
	}

	
	@NeverNull public MxlStartStopChange getType()
	{
		return type;
	}
	
	
	@NeverNull public MxlPrintStyle getPrintStyle()
	{
		return printStyle;
	}
	
	
	@Override public MxlDirectionTypeContentType getDirectionTypeContentType()
	{
		return MxlDirectionTypeContentType.Pedal;
	}
	
	
	@NeverNull public static MxlPedal read(Element e)
	{
		return new MxlPedal(
			MxlStartStopChange.read(attribute(e, "type"), e),
			MxlPrintStyle.read(e));
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		e.setAttribute("type", type.write());
		printStyle.write(e);
	}

}
