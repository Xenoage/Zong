package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent;
import com.xenoage.zong.musicxml.types.enums.MxlTimeSymbol;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML time.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="number,print-style,print-object", partly="beats,beat-type")
public final class MxlTime
{
	
	public static final String ELEM_NAME = "time";
	
	@NeverNull private final MxlTimeContent content;
	@MaybeNull private final MxlTimeSymbol symbol;
	
	
	public MxlTime(MxlTimeContent content, MxlTimeSymbol symbol)
	{
		this.content = content;
		this.symbol = symbol;
	}

	
	@NeverNull public MxlTimeContent getContent()
	{
		return content;
	}

	
	@MaybeNull public MxlTimeSymbol getSymbol()
	{
		return symbol;
	}
	
	
	/**
	 * Returns null, if the time signature is unsupported.
	 */
	@MaybeNull public static MxlTime read(Element e)
	{
		MxlTimeContent content = null;
		Element firstChild = element(e);
		String n = firstChild.getNodeName();
		if (n.equals("beats"))
			content = MxlNormalTime.read(e);
		else if (n.equals("senza-misura"))
			content = MxlSenzaMisura.read();
		if (content != null)
			return new MxlTime(content, MxlTimeSymbol.read(e));
		else
			return null;
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		content.write(e);
		if (symbol != null)
			symbol.write(e);
	}
	
	

}
