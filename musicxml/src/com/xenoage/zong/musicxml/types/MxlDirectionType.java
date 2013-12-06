package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML direction-type.
 * 
 * Only one content element is used.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="rehearsal,segno,coda,dashes,bracket," +
	"octave-shift,harp-pedals,damp,damp-all,eyeglasses,scordatura,accordion-registration," +
	"other-direction")
public final class MxlDirectionType
{
	
	public static final String ELEM_NAME = "direction-type";
	
	@NeverNull private final MxlDirectionTypeContent content;

	
	public MxlDirectionType(MxlDirectionTypeContent content)
	{
		this.content = content;
	}

	
	@NeverNull public MxlDirectionTypeContent getContent()
	{
		return content;
	}
	
	
	/**
	 * Returns null, if content is unsupported.
	 */
	@MaybeNull public static MxlDirectionType read(Element e)
	{
		MxlDirectionTypeContent content = null;
		Element firstChild = element(e);
		String n = firstChild.getNodeName();
		switch (n.charAt(0))	
		{
			case 'd':
				if (n.equals(MxlDynamics.ELEM_NAME))
					content = MxlDynamics.read(firstChild);
				break;
			case 'i':
				if (n.equals("image"))
					content = MxlImage.read(firstChild);
				break;
			case 'p':
				if (n.equals(MxlPedal.ELEM_NAME))
					content = MxlPedal.read(firstChild);
				break;
			case 'm':
				if (n.equals(MxlMetronome.ELEM_NAME))
					content = MxlMetronome.read(firstChild);
				break;
			case 'w':
				if (n.equals(MxlWedge.ELEM_NAME))
					content = MxlWedge.read(firstChild);
				else if (n.equals(MxlWords.ELEM_NAME))
					content = MxlWords.read(firstChild);
				break;
		}
		if (content != null)
			return new MxlDirectionType(content);
		else
			return null;
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		content.write(e);
	}
	

}
