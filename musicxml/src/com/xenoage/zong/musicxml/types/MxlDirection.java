package com.xenoage.zong.musicxml.types;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
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
	implements MxlMusicDataContent {

	public static final String elemName = "direction";

	@NonNull private List<MxlDirectionType> directionTypes;
	@MaybeNull private Integer staff;
	@MaybeNull private MxlSound sound;
	@MaybeNull private MxlPlacement placement;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Direction;
	}

	/**
	 * Returns null, when no supported content was found.
	 */
	@MaybeNull public static MxlDirection read(XmlReader reader) {
		PVector<MxlDirectionType> directionTypes = pvec();
		MxlSound sound = null;
		for (Element child : XMLReader.elements(e)) {
			String n = child.getNodeName();
			if (n.equals(MxlDirectionType.elemName)) {
				MxlDirectionType directionType = MxlDirectionType.read(child);
				if (directionType != null)
					directionTypes = directionTypes.plus(directionType);
			}
			else if (n.equals(MxlSound.ELEM_NAME)) {
				sound = MxlSound.read(child);
			}
		}
		if (directionTypes.size() > 0) {
			return new MxlDirection(directionTypes, parseChildIntNull(e, "staff"), sound,
				MxlPlacement.read(e));
		}
		else {
			return null;
		}
	}

	@Override public void write(Element parent) {
		Element e = addElement(elemName, parent);
		for (MxlDirectionType directionType : directionTypes)
			directionType.write(e);
		addAttribute(e, "staff", staff);
		if (sound != null)
			sound.write(e);
		if (placement != null)
			placement.write(e);
	}

}
