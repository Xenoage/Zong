package com.xenoage.zong.musicxml.types.groups;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.*;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;
import com.xenoage.zong.musicxml.util.error.handler.ErrorHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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


	public void readElement(XmlReader reader, ErrorHandler errorHandler) {
		String n = reader.getElementName();
		switch (n) {
			case MxlCredit.elemName:
				if (credits == null)
					credits = new ArrayList<>();
				credits.add(MxlCredit.read(reader));
				break;
			case MxlDefaults.elemName:
				defaults = MxlDefaults.read(reader);
				break;
			case MxlIdentification.elemName:
				identification = MxlIdentification.read(reader);
				break;
			case "movement-number":
				movementNumber = reader.getTextNotNull();
				break;
			case "movement-title":
				movementTitle = reader.getTextNotNull();
				break;
			case MxlPartList.elemName:
				partList = MxlPartList.read(reader, errorHandler);
				break;
			case MxlWork.elemName:
				work = MxlWork.read(reader);
				break;
		}
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
