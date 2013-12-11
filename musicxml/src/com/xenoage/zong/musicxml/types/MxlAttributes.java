package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.Parser.parseIntegerNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML attributes.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "editorial,part-symbol,instruments,staff-details,"
	+ "directive,measure-style", partly = "time,clef", children = "key,time,clef")
@AllArgsConstructor @Getter @Setter
public final class MxlAttributes
	implements MxlMusicDataContent {

	public static final String elemName = "attributes";

	@MaybeNull private Integer divisions;
	@MaybeNull private MxlKey key;
	@MaybeNull private MxlTime time;
	@MaybeNull private Integer staves;
	@MaybeNull private List<MxlClef> clefs;
	@MaybeNull private MxlTranspose transpose;

	
	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Attributes;
	}

	@NonNull public static MxlAttributes read(XmlReader reader) {
		Integer divisions = null;
		MxlKey key = null;
		MxlTime time = null;
		Integer staves = null;
		List<MxlClef> clefs = null;
		MxlTranspose transpose = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			switch (n.charAt(0)) { //switch for performance
				case 'c':
					if (MxlClef.elemName.equals(n)) {
						if (clefs == null)
							clefs = new ArrayList<MxlClef>();
						clefs.add(MxlClef.read(reader));
					}
					break;
				case 'd':
					if ("divisions".equals(n))
						divisions = parseIntegerNull(reader.getText());
					break;
				case 'k':
					if (MxlKey.elemName.equals(n))
						key = MxlKey.read(reader);
					break;
				case 's':
					if ("staves".equals(n))
						staves = parseIntegerNull(reader.getText());
					break;
				case 't':
					if (MxlTime.elemName.equals(n))
						time = MxlTime.read(reader);
					else if (MxlTranspose.elemName.equals(n))
						transpose = MxlTranspose.read(reader);
					break;
			}
			reader.closeElement();
		}
		return new MxlAttributes(divisions, key, time, staves, clefs, transpose);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("divisions", divisions);
		if (key != null)
			key.write(writer);
		if (time != null)
			time.write(writer);
		writer.writeElementText("staves", staves);
		if (clefs != null) {
			for (MxlClef clef : clefs)
				clef.write(writer);
		}
		if (transpose != null)
			transpose.write(writer);
		writer.writeElementEnd();
	}

}
