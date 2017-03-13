package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * MusicXML ornaments.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "shake,wavy-line,schleifer,tremolo,other-ornament")
@AllArgsConstructor @Getter @Setter
public final class MxlOrnaments
	implements MxlNotationsContent {

	public static final String elemName = "ornaments";

	@MaybeEmpty private List<MxlOrnamentsContent> content;


	@Override public MxlNotationsContentType getNotationsContentType() {
		return MxlNotationsContentType.Ornaments;
	}

	@MaybeNull public static MxlOrnaments read(XmlReader reader) {
		List<MxlOrnamentsContent> content = alist();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			switch (n) {
				case MxlTrillMark.elemName:
					content.add(MxlTrillMark.read(reader));
					break;
				case MxlTurn.elemName:
					content.add(MxlTurn.read(reader));
					break;
				case MxlDelayedTurn.elemName:
					content.add(MxlDelayedTurn.read(reader));
					break;
				case MxlInvertedTurn.elemName:
					content.add(MxlInvertedTurn.read(reader));
					break;
				case MxlMordent.elemName:
					content.add(MxlMordent.read(reader));
					break;
				case MxlInvertedMordent.elemName:
					content.add(MxlInvertedMordent.read(reader));
					break;
				case MxlAccidentalMark.elemName:
					content.add(MxlAccidentalMark.read(reader));
					break;
			}
			reader.closeElement();
		}
		if (content.size() == 0)
			return null;
		return new MxlOrnaments(content);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		for (MxlOrnamentsContent item : content)
			item.write(writer);
		writer.writeElementEnd();
	}

}
