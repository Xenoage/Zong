package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlMarginType;
import com.xenoage.zong.musicxml.types.groups.MxlAllMargins;

/**
 * MusicXML page-margins.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlPageMargins {

	public static final String elemName = "page-margins";

	@MaybeNull private MxlAllMargins value;
	@MaybeNull private MxlMarginType type;


	public float getLeftMargin() {
		return value == null ? 0 : value.getLeftMargin();
	}

	public float getRightMargin() {
		return value == null ? 0 : value.getRightMargin();
	}

	public float getTopMargin() {
		return value == null ? 0 : value.getTopMargin();
	}

	public float getBottomMargin() {
		return value == null ? 0 : value.getBottomMargin();
	}

	/**
	 * Default is {@link MxlMarginType#Both}.
	 */
	@NonNull public MxlMarginType getTypeNotNull() {
		return (type != null ? type : MxlMarginType.Both);
	}

	@NonNull public static MxlPageMargins read(XmlReader reader) {
		//read attributes first, before moving forward to child elements
		MxlMarginType type = MxlMarginType.read(reader);
		//read child elements
		MxlAllMargins value = MxlAllMargins.read(reader);
		return new MxlPageMargins(value, type);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (type != null)
			type.write(writer);
		if (value != null)
			value.write(writer);
		writer.writeElementEnd();
	}

}
