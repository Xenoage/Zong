package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent;
import com.xenoage.zong.musicxml.types.enums.MxlTimeSymbol;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML time.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "number,print-style,print-object", partly = "beats,beat-type")
@AllArgsConstructor @Getter @Setter
public final class MxlTime {

	public static final String elemName = "time";

	@NonNull private MxlTimeContent content;
	@MaybeNull private MxlTimeSymbol symbol;


	/**
	 * Returns null, if the time signature is unsupported.
	 */
	@MaybeNull public static MxlTime read(XmlReader reader) {
		MxlTimeSymbol symbol = MxlTimeSymbol.read(reader);
		MxlTimeContent content = null;
		if (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("beats"))
				content = MxlNormalTime.read(reader);
			else if (n.equals("senza-misura")) {
				content = MxlSenzaMisura.read();
				reader.closeElement();
			}
			else
				reader.closeElement();
		}
		if (content != null)
			return new MxlTime(content, symbol);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (symbol != null)
			symbol.write(writer);
		content.write(writer);
		writer.writeElementEnd();
	}

}
