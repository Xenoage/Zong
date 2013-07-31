package com.xenoage.zong.core.music;

import static com.xenoage.utils.kernel.Range.range;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.base.annotations.Unneeded;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.util.exceptions.IllegalMPException;


/**
 * A {@link StavesList} manages the staves of a score
 * and all of its parts, and bracket and barline groups.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class StavesList {

	/** The parent score. */
	@Getter @NonNull private Score score;
	/** The list of all staves. */
	@Getter private List<Staff> staves = new ArrayList<Staff>();
	/** The list of all parts. */
	@Getter private List<Part> parts = new ArrayList<Part>();
	/** The groups of the staves with barlines. */
	@Getter private List<BarlineGroup> barlineGroups = new ArrayList<BarlineGroup>();
	/** The groups of the staves with brackets. */
	@Getter private List<BracketGroup> bracketGroups = new ArrayList<BracketGroup>();


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
			if (staffIndex <= iStaff)
				return part;
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
					for (VoiceElement me : voice.elements) {
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


}
