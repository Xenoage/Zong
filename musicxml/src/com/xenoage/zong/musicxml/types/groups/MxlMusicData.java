package com.xenoage.zong.musicxml.types.groups;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlBackup;
import com.xenoage.zong.musicxml.types.MxlBarline;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlForward;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPrint;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML music-data.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "harmony,figured-bass,sound,grouping,link,bookmark", children = "note,backup,forward,direction,attributes,print,barline")
@Getter @Setter
public final class MxlMusicData {

	@NonEmpty private List<MxlMusicDataContent> content = alist();


	public void readElement(XmlReader reader) {
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
				if (n.equals(MxlPrint.elemName))
					item = MxlPrint.read(reader);
				break;
		}
		if (item != null)
			content.add(item);
	}
	
	public void check(XmlReader reader) {
		if (content.size() < 1)
			throw reader.dataException("no content");
	}

	public void write(XmlWriter writer) {
		for (MxlMusicDataContent item : content) {
			item.write(writer);
		}
	}

}
