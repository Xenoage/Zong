package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.enums.MxlBarStyle;

/**
 * MusicXML bar-style-color.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlBarStyleColor {

	public static final String elemName = "bar-style";

	@NonNull private MxlBarStyle barStyle;
	@NonNull private MxlColor color;


	@NonNull public static MxlBarStyleColor read(XmlReader reader) {
		MxlColor color = MxlColor.read(reader);
		MxlBarStyle barStyle = MxlBarStyle.read(reader);
		return new MxlBarStyleColor(barStyle, color);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		barStyle.write(writer);
		color.write(writer);
		writer.writeElementEnd();
	}

}
