package com.xenoage.zong.musicxml.types.groups;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlPageLayout;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML layout.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "page-layout")
@AllArgsConstructor @Getter @Setter
public final class MxlLayout {

	@NonNull private MxlPageLayout pageLayout;
	@NonNull private MxlSystemLayout systemLayout;
	@MaybeEmpty private List<MxlStaffLayout> staffLayouts;


	@MaybeNull public static MxlLayout read(XmlReader reader) {
		MxlPageLayout pageLayout = null;
		MxlSystemLayout systemLayout = null;
		List<MxlStaffLayout> staffLayouts = alist();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlPageLayout.elemName))
				pageLayout = MxlPageLayout.read(reader);
			else if (n.equals(MxlSystemLayout.elemName))
				systemLayout = MxlSystemLayout.read(reader);
			else if (n.equals(MxlStaffLayout.elemName))
				staffLayouts.add(MxlStaffLayout.read(reader));
		}
		return new MxlLayout(pageLayout, systemLayout, staffLayouts);
	}

	public void write(XmlWriter writer) {
		if (pageLayout != null)
			pageLayout.write(writer);
		if (systemLayout != null)
			systemLayout.write(writer);
		for (MxlStaffLayout staffLayout : staffLayouts)
			staffLayout.write(writer);
	}

}
