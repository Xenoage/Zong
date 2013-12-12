package com.xenoage.zong.musicxml.types.partwise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.groups.MxlMusicData;
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
		String number = reader.getAttributeNotNull("number");
		MxlMusicData musicData = new MxlMusicData();
		while (reader.openNextChildElement()) {
			musicData.readElement(reader);
			reader.closeElement();
		}
		musicData.check(reader);
		return new MxlMeasure(musicData, number);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("number", number);
		musicData.write(writer);
		writer.writeElementEnd();
	}

}
