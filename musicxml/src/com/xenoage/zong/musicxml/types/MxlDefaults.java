package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlFont;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML defaults.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="appearance,music-font,lyric-language",
	partly="lyric-font")
public final class MxlDefaults
{
	
	public static final String ELEM_NAME = "defaults";
	
	@MaybeNull private final MxlScaling scaling;
	@NeverNull private final MxlLayout layout;
	@MaybeNull private final MxlFont wordFont;
	@MaybeNull private final MxlLyricFont lyricFont;
	
	
	public MxlDefaults(MxlScaling scaling, MxlLayout layout, MxlFont wordFont, MxlLyricFont lyricFont)
	{
		this.scaling = scaling;
		this.layout = layout;
		this.wordFont = wordFont;
		this.lyricFont = lyricFont;
	}

	
	@MaybeNull public MxlScaling getScaling()
	{
		return scaling;
	}

	
	@NeverNull public MxlLayout getLayout()
	{
		return layout;
	}

	
	@MaybeNull public MxlFont getWordFont()
	{
		return wordFont;
	}

	
	@MaybeNull public MxlLyricFont getLyricFont()
	{
		return lyricFont;
	}
	
	
	@NeverNull public static MxlDefaults read(Element e)
	{
		MxlScaling scaling = null;
		MxlFont wordFont = null;
		MxlLyricFont lyricFont = null;
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals(MxlScaling.ELEM_NAME))
				scaling = MxlScaling.read(c);
			else if (n.equals("word-font"))
				wordFont = MxlFont.read(c);
			else if (n.equals(MxlLyricFont.ELEM_NAME) && lyricFont == null) //read only first
				lyricFont = MxlLyricFont.read(c);
		}
		return new MxlDefaults(scaling, MxlLayout.read(e), wordFont, lyricFont);
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (scaling != null)
			scaling.write(e);
		layout.write(e);
		if (wordFont != null)
			wordFont.write(e);
		if (lyricFont != null)
			lyricFont.write(e);
	}
	
	

}
