package com.xenoage.zong.musicxml.types.groups;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlCredit;
import com.xenoage.zong.musicxml.types.MxlDefaults;
import com.xenoage.zong.musicxml.types.MxlIdentification;
import com.xenoage.zong.musicxml.types.MxlPartList;
import com.xenoage.zong.musicxml.types.MxlWork;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML score-header.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "work,identification,defaults,part-list")
@Getter @Setter
public final class MxlScoreHeader {

	@MaybeNull private MxlWork work;
	@MaybeNull private String movementNumber;
	@MaybeNull private String movementTitle;
	@MaybeNull private MxlIdentification identification;
	@MaybeNull private MxlDefaults defaults;
	@MaybeNull private List<MxlCredit> credits;
	@NonNull private MxlPartList partList;


	public void readElement(XmlReader reader) {
		String n = reader.getElementName();
		if (n.equals(MxlCredit.elemName)) {
			if (credits == null)
				credits = new ArrayList<MxlCredit>();
			credits.add(MxlCredit.read(reader));
		}
		else if (n.equals(MxlDefaults.elemName))
			defaults = MxlDefaults.read(reader);
		else if (n.equals(MxlIdentification.elemName))
			identification = MxlIdentification.read(reader);
		else if (n.equals("movement-number"))
			movementNumber = reader.getTextNotNull();
		else if (n.equals("movement-title"))
			movementTitle = reader.getTextNotNull();
		else if (n.equals(MxlPartList.elemName))
			partList = MxlPartList.read(reader);
		else if (n.equals(MxlWork.elemName))
			work = MxlWork.read(reader);
	}
	
	public void check(XmlReader reader) {
		if (partList == null)
			throw reader.dataException(MxlPartList.elemName + " missing");
	}

	public void write(XmlWriter writer) {
		if (work != null)
			work.write(writer);
		writer.writeElementText("movement-number", movementNumber);
		writer.writeElementText("movement-title", movementTitle);
		if (identification != null)
			identification.write(writer);
		if (defaults != null)
			defaults.write(writer);
		if (credits != null) {
			for (MxlCredit credit : credits)
				credit.write(writer);
		}
		partList.write(writer);
	}

}
