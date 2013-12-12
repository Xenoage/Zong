package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.attributes.MxlFont;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML defaults.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "appearance,music-font,lyric-language", partly = "lyric-font")
@AllArgsConstructor @Getter @Setter
public final class MxlDefaults {

	public static final String elemName = "defaults";

	@MaybeNull private MxlScaling scaling;
	@MaybeNull private MxlLayout layout;
	@MaybeNull private MxlFont wordFont;
	@MaybeNull private MxlLyricFont lyricFont;


	@MaybeNull public static MxlDefaults read(XmlReader reader) {
		MxlScaling scaling = null;
		MxlLayout layout = new MxlLayout();
		MxlFont wordFont = null;
		MxlLyricFont lyricFont = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlScaling.elemName))
				scaling = MxlScaling.read(reader);
			else if (n.equals("word-font"))
				wordFont = MxlFont.read(reader);
			else if (n.equals(MxlLyricFont.ELEM_NAME) && lyricFont == null) //read only first
				lyricFont = MxlLyricFont.read(c);
			reader.closeElement();
		}
		return new MxlDefaults(scaling, MxlLayout.read(e), wordFont, lyricFont);
	}

	public void write(Element parent) {
		Element e = addElement(elemName, parent);
		if (scaling != null)
			scaling.write(e);
		layout.write(e);
		if (wordFont != null)
			wordFont.write(e);
		if (lyricFont != null)
			lyricFont.write(e);
	}

}
