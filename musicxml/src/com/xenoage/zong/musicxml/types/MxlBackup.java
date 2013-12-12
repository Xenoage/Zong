package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.Parser;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML backup.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "editorial")
@AllArgsConstructor @Getter @Setter
public final class MxlBackup
	implements MxlMusicDataContent {

	public static final String elemName = "backup";

	private int duration;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Backup;
	}

	@NonNull public static MxlBackup read(XmlReader reader) {
		Integer duration = null;
		if (reader.openNextChildElement("duration")) {
			duration = Parser.parseInt(reader.getText());
			reader.closeElement();
		}
		if (duration == null)
			throw reader.dataException("duration unknown");
		return new MxlBackup(duration);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("duration", duration);
		writer.writeElementEnd();
	}

}
