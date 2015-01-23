package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML direction.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "offset,editorial-voice-direction,directive",
	partly = "direction-type", children = "direction-type,sound")
@AllArgsConstructor @Getter @Setter
public final class MxlDirection
	implements MxlMusicDataContent, MxlPlacementContent {

	public static final String elemName = "direction";

	@NonNull private List<MxlDirectionType> directionTypes;
	@MaybeNull private Integer staff;
	@MaybeNull private MxlSound sound;
	private MxlPlacement placement;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Direction;
	}

	/**
	 * Returns null, when no supported content was found.
	 */
	@MaybeNull public static MxlDirection read(XmlReader reader) {
		//attributes
		MxlPlacement placement = MxlPlacement.read(reader);
		//elements
		List<MxlDirectionType> directionTypes = alist();
		Integer staff = null;
		MxlSound sound = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlDirectionType.elemName)) {
				MxlDirectionType directionType = MxlDirectionType.read(reader);
				if (directionType != null)
					directionTypes.add(directionType);
			}
			else if (n.equals("staff")) {
				staff = reader.getTextInt();
			}
			else if (n.equals(MxlSound.elemName)) {
				sound = MxlSound.read(reader);
			}
			reader.closeElement();
		}
		if (directionTypes.size() > 0) {
			return new MxlDirection(directionTypes, staff, sound, placement);
		}
		else {
			return null;
		}
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		placement.write(writer);
		for (MxlDirectionType directionType : directionTypes)
			directionType.write(writer);
		writer.writeElementText("staff", staff);
		if (sound != null)
			sound.write(writer);
		writer.writeElementEnd();
	}

}
