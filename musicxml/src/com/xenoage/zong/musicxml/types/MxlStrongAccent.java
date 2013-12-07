package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlUpDown;

/**
 * MusicXML strong-accent.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlStrongAccent
	implements MxlArticulationsContent {

	public static final String elemName = "strong-accent";

	@MaybeNull private MxlEmptyPlacement emptyPlacement;
	@MaybeNull private MxlUpDown type;

	private static final MxlUpDown defaultType = MxlUpDown.Up;
	public static final MxlStrongAccent defaultInstance = new MxlStrongAccent(null, defaultType);


	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.StrongAccent;
	}

	@NonNull public static MxlStrongAccent read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		MxlUpDown type = notNull(MxlUpDown.read(reader.getAttributeString("type")), defaultType);
		if (emptyPlacement != null || type != defaultType)
			return new MxlStrongAccent(emptyPlacement, type);
		else
			return defaultInstance;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (emptyPlacement != null)
			emptyPlacement.write(writer);
		writer.writeAttribute("type", type.write());
		writer.writeElementEnd();
	}

}
