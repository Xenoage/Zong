package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML x-position and y-position.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter public final class MxlBezier {

	@MaybeNull private Float bezierOffset, bezierOffset2, bezierX, bezierY, bezierX2, bezierY2;


	@MaybeNull public static MxlBezier read(XmlReader reader) {
		Float bezierOffset = reader.getAttributeFloat("bezier-offset");
		Float bezierOffset2 = reader.getAttributeFloat("bezier-offset-2");
		Float bezierX = reader.getAttributeFloat("bezier-x");
		Float bezierY = reader.getAttributeFloat("bezier-y");
		Float bezierX2 = reader.getAttributeFloat("bezier-x2");
		Float bezierY2 = reader.getAttributeFloat("bezier-y2");
		if (bezierOffset != null || bezierOffset2 != null || bezierX != null || bezierY != null ||
			bezierX2 != null || bezierY2 != null)
			return new MxlBezier(bezierOffset, bezierOffset2, bezierX, bezierY, bezierX2, bezierY2);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute("bezier-offset", bezierOffset);
		writer.writeAttribute("bezier-offset-2", bezierOffset2);
		writer.writeAttribute("bezier-x", bezierX);
		writer.writeAttribute("bezier-y", bezierY);
		writer.writeAttribute("bezier-x2", bezierX2);
		writer.writeAttribute("bezier-y2", bezierY2);
	}

}
