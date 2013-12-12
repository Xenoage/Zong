package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML music-data.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "harmony,figured-bass,sound,grouping,link,bookmark", children = "note,backup,forward,direction,attributes,print,barline")
@AllArgsConstructor @Getter @Setter
public final class MxlMusicData {

	@NonEmpty private final List<MxlMusicDataContent> content;


	@NonNull public static MxlMusicData read(XmlReader reader) {
		List<MxlMusicDataContent> content = alist();
		while (reader.openNextChildElement()) {
			MxlMusicDataContent item = null;
			String n = reader.getElementName();
			switch (n.charAt(0)) { //switch for performance
				case 'a':
					if (n.equals(MxlAttributes.elemName))
						item = MxlAttributes.read(reader);
					break;
				case 'b':
					if (n.equals(MxlBackup.elemName))
						item = MxlBackup.read(reader);
					else if (n.equals(MxlBarline.elemName))
						item = MxlBarline.read(reader);
					break;
				case 'd':
					if (n.equals(MxlDirection.elemName))
						item = MxlDirection.read(reader);
					break;
				case 'f':
					if (n.equals(MxlForward.elemName))
						item = MxlForward.read(reader);
					break;
				case 'n':
					if (n.equals(MxlNote.elemName))
						item = MxlNote.read(reader);
					break;
				case 'p':
					if (n.equals(MxlPrint.ELEM_NAME))
						item = MxlPrint.read(c);
					break;
			}
			reader.closeElement();
			if (item != null)
				content = content.plus(item);
		}
		if (content.size() < 1)
			throw invalid(e);
		return new MxlMusicData(content);
	}

	public void write(Element e) {
		for (MxlMusicDataContent item : content) {
			item.write(e);
		}
	}

}
