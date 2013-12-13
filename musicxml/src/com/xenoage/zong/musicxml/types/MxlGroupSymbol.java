package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.enums.MxlGroupSymbolValue;

/**
 * MusicXML group-symbol.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlGroupSymbol {

	public static final String elemName = "group-symbol";

	@NonNull private MxlGroupSymbolValue value;
	@MaybeNull private MxlPosition position;
	@MaybeNull private MxlColor color;
	

	@NonNull public static MxlGroupSymbol read(XmlReader reader) {
		MxlPosition position = MxlPosition.read(reader);
		MxlColor color = MxlColor.read(reader);
		MxlGroupSymbolValue value = MxlGroupSymbolValue.read(reader);
		return new MxlGroupSymbol(value, position, color);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		value.write(writer);
		if (position != null)
			position.write(writer);
		if (color != null)
			color.write(writer);
		writer.writeElementEnd();
	}

}
