package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.Parser.parseInt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;


/**
 * MusicXML transpose.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlTranspose {

	public static final String elemName = "transpose";

	@MaybeNull private Integer diatonic;
	private int chromatic;
	@MaybeNull private Integer octaveChange;
	private boolean doubleValue;


	@NonNull public static MxlTranspose read(XmlReader reader) {
		Integer diatonic = null;
		Integer chromatic = null;
		Integer octaveChange= null;
		boolean doubleValue = false;
		while (reader.openNextChildElement()) {
			String eName = reader.getElementName();
			if (eName.equals("diatonic"))
				diatonic = parseInt(reader.getText());
			else if (eName.equals("chromatic"))
				chromatic = parseInt(reader.getText());
			else if (eName.equals("octave-change"))
				octaveChange = parseInt(reader.getText());
			else if (eName.equals("double"))
				doubleValue = true;
			reader.closeElement();
		}
		if (chromatic == null)
			throw reader.dataException("chromatic not found");
		return new MxlTranspose(diatonic, chromatic, octaveChange, doubleValue);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("diatonic", diatonic);
		writer.writeElementText("chromatic", chromatic);
		writer.writeElementText("octave-change", octaveChange);
		if (doubleValue)
			writer.writeElementEmpty("double");
		writer.writeElementEnd();
	}

}
