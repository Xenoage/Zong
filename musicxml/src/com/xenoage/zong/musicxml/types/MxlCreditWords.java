package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.choice.MxlCreditContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML credit-words content for the credit element.
 * 
 * Here, the sequence of several credit-words elements
 * is accumulated to a single class instance, with multiple
 * {@link MxlFormattedText} instances instead.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="link,bookmark", partly="credit-words")
public final class MxlCreditWords
	implements MxlCreditContent
{
	
	@NeverEmpty private final PVector<MxlFormattedText> items;

	
	public MxlCreditWords(PVector<MxlFormattedText> items)
	{
		this.items = items;
	}

	
	@NeverEmpty public PVector<MxlFormattedText> getItems()
	{
		return items;
	}
	
	
	@Override public MxlCreditContentType getCreditContentType()
	{
		return MxlCreditContentType.CreditWords;
	}
	
	
	@NeverNull public static MxlCreditWords read(Element parent)
	{
		PVector<MxlFormattedText> items = pvec();
		for (Element e : elements(parent))
		{
			String n = e.getNodeName();
			if (n.equals("credit-words"))
				items = items.plus(MxlFormattedText.read(e));
		}
		if (items.size() < 1)
			throw invalid(parent);
		return new MxlCreditWords(items);
	}
	
	
	@Override public void write(Element parent)
	{
		for (MxlFormattedText item : items)
		{
			Element e = addElement("credit-words", parent);
			item.write(e);
		}
	}


}
