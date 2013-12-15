package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent;
import com.xenoage.zong.musicxml.types.enums.MxlSyllabic;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML syllabic and its text.
 * See lines 3312 and 3313 in musicxml.xsd (version 2.0).
 * Elisions are not handled.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly = "", children="text-element-data")
@AllArgsConstructor @Getter @Setter
public final class MxlSyllabicText
	implements MxlLyricContent {

	/** The {@link MxlSyllabic} element. Never null (since missing syllabic element in MusicXML
	 * means single, see {@link #read(XmlReader)}). */
	@NonNull private MxlSyllabic syllabic;
	@NonNull private MxlTextElementData text;

	private static final MxlSyllabic defaultSyllabic = MxlSyllabic.Single;


	@Override public MxlLyricContentType getLyricContentType() {
		return MxlLyricContentType.SyllabicText;
	}

	/**
	 * Reads the given syllabic text.
	 * If the syllabic element is missing, it is treated as {@link MxlSyllabic#Single}
	 * (according to MusicXML mailgroup, R. Kainhofer, 2010-04-08).
	 */
	public static MxlSyllabicText read(XmlReader reader) {
		MxlSyllabic syllabic = defaultSyllabic;
		MxlTextElementData text = null;
		//the first element can be "syllabic"
		if (reader.getElementName().equals("syllabic")) {
			String syllabicText = reader.getText();
			syllabic = getEnumValue(syllabicText, MxlSyllabic.values());
			if (syllabic == null)
				throw reader.dataException("syllabic = " + syllabicText);
			//open next element
			reader.closeElement();
			if (false == reader.openNextChildElement())
				throw reader.dataException("missing text element after syllabic");
		}
		//next one must be "text"
		if (false == reader.getElementName().equals("text"))
			throw reader.dataException("text element must follow after syllabic");
		text = MxlTextElementData.read(reader);
		if (text == null)
			throw reader.dataException("missing text element");
		return new MxlSyllabicText(syllabic, text);
	}

	@Override public void write(XmlWriter writer) {
		syllabic.write(writer);
		writer.writeElementStart("text");
		text.write(writer);
		writer.writeElementEnd();
	}

}
