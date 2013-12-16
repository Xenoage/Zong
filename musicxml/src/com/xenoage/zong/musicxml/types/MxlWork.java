package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML work.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "opus")
@AllArgsConstructor @Getter @Setter
public final class MxlWork {

	public static final String elemName = "work";
	public static final MxlWork empty = new MxlWork(null, null);

	@MaybeNull private String workNumber;
	@MaybeNull private String workTitle;


	@MaybeNull public static MxlWork read(XmlReader reader) {
		String workNumber = null, workTitle = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("work-number"))
				workNumber = reader.getText();
			else if (n.equals("work-title"))
				workTitle = reader.getText();
			reader.closeElement();
		}
		if (workNumber != null || workTitle != null)
			return new MxlWork(workNumber, workTitle);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("work-number", workNumber);
		writer.writeElementText("work-title", workTitle);
		writer.writeElementEnd();
	}

}
