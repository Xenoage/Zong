package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.Parser;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML page-layout.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly = "")
@AllArgsConstructor @Getter @Setter
public final class MxlPageLayout {

	public static final String elemName = "page-layout";

	@MaybeNull private Size2f pageSize;
	/** Size is between 0 and 2. */
	@MaybeNull private List<MxlPageMargins> pageMargins;
	

	@NonNull public static MxlPageLayout read(XmlReader reader) {
		Float pageWidth = null;
		Float pageHeight = null;
		List<MxlPageMargins> pageMargins = alist();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("page-width"))
				pageWidth = Parser.parseFloatNull(reader.getText());
			else if (n.equals("page-height"))
				pageHeight = Parser.parseFloatNull(reader.getText());
			else if (n.equals("page-margins"))
				pageMargins.add(MxlPageMargins.read(reader));
			reader.closeElement();
		}
		Size2f pageSize = (pageWidth == null && pageHeight == null ? null : new Size2f(pageWidth, pageHeight));
		return new MxlPageLayout(pageSize, pageMargins);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (pageSize != null) {
			writer.writeElementText("page-width", pageSize.width);
			writer.writeElementText("page-height", pageSize.height);
		}
		for (MxlPageMargins item : pageMargins)
			item.write(writer);
		writer.writeElementEnd();
	}

}
