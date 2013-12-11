package com.xenoage.zong.musicxml.types.partwise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NeverNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.MxlMusicData;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML measure in a partwise score.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly = "measure-attributes", children = "music-data")
@AllArgsConstructor @Getter @Setter
public final class MxlMeasure {

	public static final String elemName = "measure";

	@NonNull private MxlMusicData musicData;
	@NonNull private String number;


	@NonNull public static MxlMeasure read(XmlReader reader) {
		String number = reader.getAttributeString("number");
		if (number == null)
			reader.throwDataException("number unknown");
		return new MxlMeasure(MxlMusicData.read(reader), number);
	}

	public void write(Element parent) {
		Element e = addElement(elemName, parent);
		addAttribute(e, "number", number);
		musicData.write(e);
	}

}
