package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlRepeat;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.enums.MxlRightLeftMiddle;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML barline.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="editorial,wavy-line,segno,coda,fermata,ending," +
	"segno,coda,divisions")
public final class MxlBarline
	implements MxlMusicDataContent
{
	
	public static final String ELEM_NAME = "barline";
	
	@MaybeNull private final MxlBarStyleColor barStyle;
	@MaybeNull private final MxlRepeat repeat;
	@NeverNull private final MxlRightLeftMiddle location;
	
	private static final MxlRightLeftMiddle defaultLocation = MxlRightLeftMiddle.Right;

	
	public MxlBarline(MxlBarStyleColor barStyle, MxlRepeat repeat, MxlRightLeftMiddle location)
	{
		this.barStyle = barStyle;
		this.repeat = repeat;
		this.location = location;
	}

	
	@MaybeNull public MxlBarStyleColor getBarStyle()
	{
		return barStyle;
	}

	
	@MaybeNull public MxlRepeat getRepeat()
	{
		return repeat;
	}

	
	@NeverNull public MxlRightLeftMiddle getLocation()
	{
		return location;
	}
	
	
	@Override public MxlMusicDataContentType getMusicDataContentType()
	{
		return MxlMusicDataContentType.Barline;
	}
	
	
	public static MxlBarline read(Element e)
	{
		MxlBarStyleColor barStyle = null;
		MxlRepeat repeat = null;
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals(MxlBarStyleColor.ELEM_NAME))
				barStyle = MxlBarStyleColor.read(c);
			else if (n.equals(MxlRepeat.ELEM_NAME))
				repeat = MxlRepeat.read(c);
		}
		MxlRightLeftMiddle location = notNull(MxlRightLeftMiddle.read(e), defaultLocation);
		return new MxlBarline(barStyle, repeat, location);
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (barStyle != null)
			barStyle.write(e);
		if (repeat != null)
			repeat.write(e);
		location.write(e);
	}
	
	

}
