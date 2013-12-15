package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.Parser;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML forward.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "staff", children = "editorial-voice")
@AllArgsConstructor @Getter @Setter
public final class MxlForward
	implements MxlMusicDataContent {

	public static final String elemName = "forward";

	private final int duration;
	@MaybeNull private MxlEditorialVoice editorialVoice;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Forward;
	}
	
	@NonNull public static MxlForward read(XmlReader reader) {
		Integer duration = null;
		MxlEditorialVoice editorialVoice = new MxlEditorialVoice();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("duration"))
				duration = Parser.parseInt(reader.getText());
			else
				editorialVoice.readElement(reader);
			reader.closeElement();
		}
		if (duration == null)
			throw reader.dataException("duration unknown");
		if (false == editorialVoice.isUsed())
			editorialVoice = null;
		return new MxlForward(duration, editorialVoice);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("duration", duration);
		if (editorialVoice != null)
			editorialVoice.write(writer);
		writer.writeElementEnd();
	}

}
