package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.utils.color.ColorUtils.getColor;
import static com.xenoage.utils.color.ColorUtils.getHex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML color.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlColor {

	@NonNull private Color value;

	public static final String attrName = "color";


	@MaybeNull public static MxlColor read(XmlReader reader) {
		String s = reader.getAttribute(attrName);
		if (s != null) {
			try {
				return new MxlColor(getColor(s));
			} catch (NumberFormatException ex) {
				reader.throwDataException(attrName + " = " + s);
				return null;
			}
		}
		else {
			return null;
		}
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, getHex(value));
	}

}
