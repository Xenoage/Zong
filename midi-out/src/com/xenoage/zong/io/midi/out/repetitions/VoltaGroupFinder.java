package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.collections.CList;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.midi.out.repetitions.VoltaGroup.VoltaStartMeasure;
import lombok.AllArgsConstructor;
import lombok.val;

import static com.xenoage.utils.collections.CList.clist;

/**
 * Finds the {@link VoltaGroup}s in a score.
 *
 * A volta group is a list of consecutive voltas. See {@link Volta} for more details
 * and ordering rules.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class VoltaGroupFinder {

	private final Score score;


	/**
	 * Finds all {@link VoltaGroup}s in the score.
	 * The groups are returned in a map with their start measure index as the key.
	 */
	public VoltaGroups findAllVoltaGroups() {
		CList<VoltaGroup> groups = clist();
		val scoreHeader = score.getHeader();
		for (int measure = 0; measure < score.getMeasuresCount();) {
			if (scoreHeader.getColumnHeader(measure).getVolta() != null) {
				val voltaGroup = findVoltaGroup(measure);
				groups.add(voltaGroup);
				measure += voltaGroup.getMeasuresCount();
			}
			else {
				measure ++;
			}
		}
		return new VoltaGroups(groups.close());
	}

	/**
	 * Collects all consecutive {@link Volta}s starting at the given measure
	 * into a {@link VoltaGroup}.
	 */
	private VoltaGroup findVoltaGroup(int startMeasure) {
		CList<VoltaStartMeasure> voltasMeasures = clist();
		val scoreHeader = score.getHeader();
		for (int measure = startMeasure; measure < score.getMeasuresCount();) {
			Volta volta = scoreHeader.getColumnHeader(measure).getVolta();
			if (volta != null) {
				voltasMeasures.add(new VoltaStartMeasure(volta, measure));
				measure += volta.getLength();
			}
			else {
				break;
			}
		}
		return new VoltaGroup(voltasMeasures.close());
	}

}
