package com.xenoage.zong.musicxml.types.groups;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
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
@Getter @Setter
public final class MxlLayout {

	@MaybeNull private MxlPageLayout pageLayout = null;
	@MaybeNull private MxlSystemLayout systemLayout = null;
	@MaybeNull private List<MxlStaffLayout> staffLayouts = null;


	public void readElement(XmlReader reader) {
		String n = reader.getElementName();
		if (n.equals(MxlPageLayout.elemName))
			pageLayout = MxlPageLayout.read(reader);
		else if (n.equals(MxlSystemLayout.elemName))
			systemLayout = MxlSystemLayout.read(reader);
		else if (n.equals(MxlStaffLayout.elemName)) {
			if (staffLayouts == null)
				staffLayouts = new ArrayList<MxlStaffLayout>();
			staffLayouts.add(MxlStaffLayout.read(reader));
		}
	}
	
	public boolean isUsed() {
		return pageLayout != null || systemLayout != null || (staffLayouts != null && staffLayouts.size() > 0);
	}

	public void write(XmlWriter writer) {
		if (pageLayout != null)
			pageLayout.write(writer);
		if (systemLayout != null)
			systemLayout.write(writer);
		if (staffLayouts != null) {
			for (MxlStaffLayout staffLayout : staffLayouts)
				staffLayout.write(writer);
		}
	}

}
