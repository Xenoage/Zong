package com.xenoage.zong.musicxml.types.groups;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
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
	@MaybeNull private MxlPartList partList;


	public MxlScoreHeader(MxlWork work, String movementNumber, String movementTitle,
		MxlIdentification identification, MxlDefaults defaults, PVector<MxlCredit> credits,
		MxlPartList partList) {
		this.work = work;
		this.movementNumber = movementNumber;
		this.movementTitle = movementTitle;
		this.identification = identification;
		this.defaults = defaults;
		this.credits = credits;
		this.partList = partList;
	}

	@MaybeNull public MxlWork getWork() {
		return work;
	}

	@MaybeNull public String getMovementNumber() {
		return movementNumber;
	}

	@MaybeNull public String getMovementTitle() {
		return movementTitle;
	}

	@MaybeNull public MxlIdentification getIdentification() {
		return identification;
	}

	@MaybeNull public MxlDefaults getDefaults() {
		return defaults;
	}

	@MaybeEmpty public PVector<MxlCredit> getCredits() {
		return credits;
	}

	@NeverNull public MxlPartList getPartList() {
		return partList;
	}

	@NeverNull public static MxlScoreHeader read(Element e) {
		MxlWork work = null;
		String movementNumber = null;
		String movementTitle = null;
		MxlIdentification identification = null;
		MxlDefaults defaults = null;
		PVector<MxlCredit> credits = PVector.pvec();
		MxlPartList partList = null;
		for (Element c : elements(e)) {
			String n = c.getNodeName();
			switch (n.charAt(0)) {
				case 'c':
					if (n.equals(MxlCredit.elemName))
						credits = credits.plus(MxlCredit.read(c));
					break;
				case 'd':
					if (n.equals(MxlDefaults.elemName))
						defaults = MxlDefaults.read(c);
					break;
				case 'i':
					if (n.equals(MxlIdentification.elemName))
						identification = MxlIdentification.read(c);
					break;
				case 'm':
					if (n.equals("movement-number"))
						movementNumber = getTextContent(c);
					else if (n.equals("movement-title"))
						movementTitle = getTextContent(c);
					break;
				case 'p':
					if (n.equals(MxlPartList.elemName))
						partList = MxlPartList.read(c);
					break;
				case 'w':
					if (n.equals(MxlWork.elemName))
						work = MxlWork.read(c);
					break;
			}
		}
		return new MxlScoreHeader(work, movementNumber, movementTitle, identification, defaults,
			credits, throwNull(partList, e));
	}

	public void write(Element e) {
		if (work != null)
			work.write(e);
		addElement("movement-number", movementNumber, e);
		addElement("movement-title", movementTitle, e);
		if (identification != null)
			identification.write(e);
		if (defaults != null)
			defaults.write(e);
		for (MxlCredit credit : credits)
			credit.write(e);
		partList.write(e);
	}

}
