package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlRepeat;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.enums.MxlRightLeftMiddle;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML barline.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "editorial,wavy-line,segno,coda,fermata,"
	+ "segno,coda,divisions")
@AllArgsConstructor @Getter @Setter
public final class MxlBarline
	implements MxlMusicDataContent {

	public static final String elemName = "barline";

	@MaybeNull private MxlBarStyleColor barStyle;
	@MaybeNull private MxlEnding ending;
	@MaybeNull private MxlRepeat repeat;
	@NonNull private MxlRightLeftMiddle location;

	private static final MxlRightLeftMiddle defaultLocation = MxlRightLeftMiddle.Right;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Barline;
	}

	public static MxlBarline read(XmlReader reader) {
		//attributes
		MxlRightLeftMiddle location = MxlRightLeftMiddle.readOr(reader, defaultLocation);
		//elements
		MxlBarStyleColor barStyle = null;
		MxlEnding ending = null;
		MxlRepeat repeat = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlBarStyleColor.elemName))
				barStyle = MxlBarStyleColor.read(reader);
			else if (n.equals(MxlEnding.elemName))
				ending = MxlEnding.read(reader);
			else if (n.equals(MxlRepeat.elemName))
				repeat = MxlRepeat.read(reader);
			reader.closeElement();
		}
		return new MxlBarline(barStyle, ending, repeat, location);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		location.write(writer);
		if (barStyle != null)
			barStyle.write(writer);
		if (ending != null)
			ending.write(writer);
		if (repeat != null)
			repeat.write(writer);
		writer.writeElementEnd();
	}

}
