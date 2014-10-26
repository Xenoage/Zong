package com.xenoage.zong.core.music;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.xenoage.utils.annotations.Unneeded;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.utils.exceptions.IllegalMPException;

/**
 * A {@link StavesList} manages the staves of a score
 * and all of its parts, and bracket and barline groups.
 *
 * @author Andreas Wenger
 */
@Data
public final class StavesList {

	/** The parent score. */
	private Score score = null;
	/** The list of all staves. */
	private List<Staff> staves = new ArrayList<Staff>();
	/** The list of all parts. */
	private List<Part> parts = new ArrayList<Part>();
	/** The groups of the staves with barlines. */
	private List<BarlineGroup> barlineGroups = new ArrayList<BarlineGroup>();
	/** The groups of the staves with brackets. */
	private List<BracketGroup> bracketGroups = new ArrayList<BracketGroup>();


	/**
	 * Gets the measure column at the given measure index. 
	 */
	public Column getColumn(int measure) {
		Column ret = new Column(staves.size());
		for (int iStaff : range(staves.size())) {
			ret.add(staves.get(iStaff).getMeasures().get(measure));
		}
		return ret;
	}

	/**
	 * Gets the part the given staff belongs to, or null if not found.
	 */
	public Part getPartByStaffIndex(int staffIndex) {
		int iStaff = 0;
		for (Part part : parts) {
			iStaff += part.getStavesCount();
			if (staffIndex < iStaff)
				return part;
		}
		return null;
	}

	/**
	 * Gets the staves indices of the given part, or null if not found.
	 */
	public StavesRange getPartStaffIndices(Part part) {
		int iStaff = 0;
		for (Part p : parts) {
			if (p == part)
				return new StavesRange(iStaff, iStaff + part.getStavesCount() - 1);
			iStaff += p.getStavesCount();
		}
		return null;
	}

	/**
	 * Gets the barline group the given staff belongs to.
	 * If no group is found, null is returned.
	 */
	public BarlineGroup getBarlineGroupByStaff(int staffIndex) {
		for (BarlineGroup barlineGroup : barlineGroups) {
			if (barlineGroup.getStaves().contains(staffIndex))
				return barlineGroup;
		}
		return null;
	}

	/**
	 * Returns the number of divisions of a quarter note. This is needed for
	 * Midi and MusicXML Export.
	 */
	public int computeDivisions() {
		int actualdivision = 4;
		for (Staff staff : staves) {
			for (Measure measure : staff.getMeasures()) {
				for (Voice voice : measure.getVoices()) {
					for (VoiceElement me : voice.getElements()) {
						if (me.getDuration() != null) {
							actualdivision = MathUtils.lcm(actualdivision, me.getDuration().getDenominator());
						}
					}
				}
			}
		}
		return actualdivision / 4;
	}

	/**
	 * Gets the {@link Staff} at the given {@link MP} (only the staff index is read).
	 */
	public Staff getStaff(MP mp) {
		return getStaff(mp.staff);
	}

	/**
	 * Gets the {@link Staff} at the given global index.
	 */
	public Staff getStaff(int staffIndex) {
		if (staffIndex >= 0 && staffIndex < staves.size()) {
			return staves.get(staffIndex);
		}
		else {
			throw new IllegalMPException(MP.atStaff(staffIndex));
		}
	}

	/**
	 * Gets the global index of the given {@link Staff}, or -1 if the staff
	 * is not part of this staves list.
	 */
	public int getStaffIndex(Staff staff) {
		return staves.indexOf(staff);
	}

	/**
	 * Gets the {@link Measure} with the given index at the staff with the given
	 * global index.
	 */
	public Measure getMeasure(MP mp) {
		return getStaff(mp).getMeasure(mp);
	}

	/**
	 * Gets the {@link Voice} at the given {@link BMP} (beat is ignored).
	 */
	public Voice getVoice(MP mp) {
		return getMeasure(mp).getVoice(mp);
	}

	/**
	 * Gets the filled beats for each measure column, that
	 * means, the first beat in each column where there is no music
	 * element following any more.
	 */
	@Unneeded public Fraction[] getFilledBeats() {
		int measuresCount = staves.get(0).getMeasures().size();
		Fraction[] ret = new Fraction[measuresCount];
		for (int iMeasure = 0; iMeasure < measuresCount; iMeasure++) {
			Fraction maxBeat = Fraction._0;
			for (Staff staff : staves) {
				Fraction beat = staff.getMeasures().get(iMeasure).getFilledBeats();
				if (beat.compareTo(maxBeat) > 0)
					maxBeat = beat;
			}
			ret[iMeasure] = maxBeat;
		}
		return ret;
	}
	
	/**
	 * Adds a staff group for the given staves with the given style.
	 * Since a staff may only have one barline group, existing barline groups
	 * at the given positions are merged with the given group.
	 */
	public void addBarlineGroup(StavesRange stavesRange, BarlineGroup.Style style) {
		if (stavesRange.getStop() >= staves.size())
			throw new IllegalArgumentException("staves out of range");
		
		//if the given group is within an existing one, ignore the new group
		//(we do not support nested barline groups)
		for (int i : range(barlineGroups)) {
			BarlineGroup group = barlineGroups.get(i);
			if (group.getStaves().contains(stavesRange))
				return;
		}

		//delete existing groups intersecting the given range, but merge
		//the affected staves into the given group
		for (int i : rangeReverse(barlineGroups)) {
			BarlineGroup group = barlineGroups.get(i);
			if (group.getStaves().intersects(stavesRange)) {
				barlineGroups.remove(i);
				stavesRange = stavesRange.merge(group.getStaves());
			}
		}

		//add new group at the right position
		//(the barline groups are sorted by start index)
		int i = 0;
		while (i < barlineGroups.size() && stavesRange.getStart() > barlineGroups.get(i).getStaves().getStart()) {
			i++;
		}
		barlineGroups.add(i, new BarlineGroup(stavesRange, style));
	}


	/**
	 * Adds a bracket group for the given staves with the given style.
	 */
	public void addBracketGroup(StavesRange stavesRange, BracketGroup.Style style) {
		if (stavesRange.getStop() >= staves.size())
			throw new IllegalArgumentException("staves out of range");
		//add new group at the right position
		//(the bracket groups are sorted by start index)
		int i = 0;
		while (i < bracketGroups.size() && bracketGroups.get(i).getStaves().getStart() > stavesRange.getStart()) {
			i++;
		}
		bracketGroups.add(i, new BracketGroup(stavesRange, style));
	}

}
