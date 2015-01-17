package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
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
	@NonNull private MxlLayout layout;
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
			else if (n.equals(MxlLyricFont.elemName) && lyricFont == null) //read only first
				lyricFont = MxlLyricFont.read(reader);
			else
				layout.readElement(reader);
			reader.closeElement();
		}
		if (false == layout.isUsed())
			layout = null;
		if (scaling != null || layout != null || wordFont != null || lyricFont != null)
			return new MxlDefaults(scaling, layout, wordFont, lyricFont);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (scaling != null)
			scaling.write(writer);
		if (layout != null)
			layout.write(writer);
		if (wordFont != null) {
			writer.writeElementStart("word-font");
			wordFont.write(writer);
			writer.writeElementEnd();
		}
		if (lyricFont != null)
			lyricFont.write(writer);
		writer.writeElementEnd();
	}

}
