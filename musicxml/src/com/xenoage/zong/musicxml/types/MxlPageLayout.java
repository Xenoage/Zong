package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.Parse.parseFloat;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML page-layout.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly="")
public final class MxlPageLayout
{
	
	public static final String ELEM_NAME = "page-layout";
	
	@MaybeNull private final Float pageHeight;
	@MaybeNull private final Float pageWidth;
	@MaybeEmpty private final PVector<MxlPageMargins> pageMargins;
	
	
	public MxlPageLayout(Float pageHeight, Float pageWidth, PVector<MxlPageMargins> pageMargins)
	{
		this.pageHeight = pageHeight;
		this.pageWidth = pageWidth;
		this.pageMargins = pageMargins;
	}

	
	/**
	 * If null, {@link #getPageWidth()} returns null true, and vice versa.
	 */
	@MaybeNull public Float getPageHeight()
	{
		return pageHeight;
	}

	
	/**
	 * If null, {@link #getPageHeight()} returns null true, and vice versa.
	 */
	@MaybeNull public Float getPageWidth()
	{
		return pageWidth;
	}

	
	/**
	 * Size is between 0 and 2.
	 */
	@MaybeEmpty public PVector<MxlPageMargins> getPageMargins()
	{
		return pageMargins;
	}
	
	
	@NeverNull public static MxlPageLayout read(Element e)
	{
		Float pageHeight = null;
		Float pageWidth = null;
		PVector<MxlPageMargins> pageMargins = pvec();
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			switch (n.charAt(5))
			{
				case 'h':
					if (pageHeight == null)
						pageHeight = parseFloat(c);
					break;
				case 'm':
					pageMargins = pageMargins.plus(MxlPageMargins.read(c));
					break;
				case 'w':
					if (pageWidth == null)
						pageWidth = parseFloat(c);
					break;
			}
		}
		return new MxlPageLayout(pageHeight, pageWidth, pageMargins);
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (pageHeight != null)
		{
			addElement("page-height", pageHeight, e);
			addElement("page-width", pageWidth, e);
		}
		for (MxlPageMargins item : pageMargins)
			item.write(e);
	}
	

}
