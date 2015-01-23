package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopContinue;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlPositionContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML slur or tied.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "line-type,orientation,color")
@AllArgsConstructor @Getter @Setter
public final class MxlSlurOrTied
	implements MxlNotationsContent, MxlPositionContent, MxlPlacementContent {

	public static final String elemNameSlur = "slur";
	public static final String elemNameTied = "tied";


	public enum MxlElementType {
		Slur,
		Tied;
	}


	@NonNull private MxlElementType elementType;
	@NonNull private MxlStartStopContinue type;
	/** May be null for tied elements, but never null for slurs. */
	@MaybeNull private Integer number;
	private MxlPosition position;
	private MxlPlacement placement;
	@MaybeNull private MxlBezier bezier;

	private static final int defaultNumberForSlur = 1;


	@Override public MxlNotationsContentType getNotationsContentType() {
		return MxlNotationsContentType.SlurOrTied;
	}

	@MaybeNull public static MxlSlurOrTied read(XmlReader reader) {
		//element type
		MxlElementType elementType = null;
		String eName = reader.getElementName();
		if (elemNameSlur.equals(eName))
			elementType = MxlElementType.Slur;
		else if (elemNameTied.equals(eName))
			elementType = MxlElementType.Tied;
		else
			throw reader.dataException("slur or tied expected");
		//type
		MxlStartStopContinue type = MxlStartStopContinue.read(reader.getAttribute("type"));
		if (type == MxlStartStopContinue.Continue && elementType == MxlElementType.Tied)
			throw reader.dataException("tied can not be continued");
		//other members
		Integer number = reader.getAttributeInt("number");
		if (elementType == MxlElementType.Slur)
			number = notNull(number, defaultNumberForSlur);
		MxlPosition position = MxlPosition.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		MxlBezier bezier = MxlBezier.read(reader);
		return new MxlSlurOrTied(elementType, type, number, position, placement, bezier);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elementType == MxlElementType.Slur ? elemNameSlur : elemNameTied);
		writer.writeAttribute("type", type.write());
		writer.writeAttribute("number", number);
		position.write(writer);
		placement.write(writer);
		if (bezier != null)
			bezier.write(writer);
		writer.writeElementEnd();
	}

}
