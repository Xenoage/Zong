package com.xenoage.zong.musicxml.types.groups;

import static com.xenoage.utils.xml.XMLReader.elements;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.MxlPageLayout;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;
import com.xenoage.zong.musicxml.types.MxlSystemLayout;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML layout.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="page-layout")
public final class MxlLayout
{
	
	@MaybeNull private final MxlPageLayout pageLayout;
	@MaybeNull private final MxlSystemLayout systemLayout;
	@MaybeEmpty private final PVector<MxlStaffLayout> staffLayouts;
	
	public static final MxlLayout empty = new MxlLayout(null, null,
		new PVector<MxlStaffLayout>());
	
	
	public MxlLayout(MxlPageLayout pageLayout, MxlSystemLayout systemLayout,
		PVector<MxlStaffLayout> staffLayouts)
	{
		this.pageLayout = pageLayout;
		this.systemLayout = systemLayout;
		this.staffLayouts = staffLayouts;
	}
	
	
	@MaybeNull public MxlPageLayout getPageLayout()
	{
		return pageLayout;
	}


	@MaybeNull public MxlSystemLayout getSystemLayout()
	{
		return systemLayout;
	}


	@MaybeEmpty public PVector<MxlStaffLayout> getStaffLayouts()
	{
		return staffLayouts;
	}
	
	
	@NeverNull public static MxlLayout read(Element e)
	{
		MxlPageLayout pageLayout = null;
		MxlSystemLayout systemLayout = null;
		PVector<MxlStaffLayout> staffLayouts = new PVector<MxlStaffLayout>();
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals(MxlPageLayout.ELEM_NAME))
				pageLayout = MxlPageLayout.read(c);
			else if (n.equals(MxlSystemLayout.ELEM_NAME))
				systemLayout = MxlSystemLayout.read(c);
			else if (n.equals(MxlStaffLayout.ELEM_NAME))
				staffLayouts = staffLayouts.plus(MxlStaffLayout.read(c));
		}
		if (pageLayout != null || systemLayout != null || staffLayouts.size() > 0)
			return new MxlLayout(pageLayout, systemLayout, staffLayouts);
		else
			return empty;
	}
	
	
	public void write(Element e)
	{
		if (this != empty)
		{
			if (pageLayout != null)
				pageLayout.write(e);
			if (systemLayout != null)
				systemLayout.write(e);
			for (MxlStaffLayout staffLayout : staffLayouts)
				staffLayout.write(e);
		}
	}
	

}
