package com.xenoage.zong.musiclayout.layouter.scoreframelayout.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.alistInit;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * The staff stampings on a {@link ScoreFrameLayout}.
 * 
 * @author Andreas Wenger
 */
public class StaffStampings {

	//lists of staves, if we are interested in a certain score staff: staves[globalstaffindex][systemindex]
	private ArrayList<ArrayList<StaffStamping>> stavesByStaff;
	//lists of staves, if we are interested in a certain system: staves[systemindex][globalstaffindex]
	private ArrayList<ArrayList<StaffStamping>> stavesBySystem;


	/**
	 * Creates a new list of staff stampings for the given number of
	 * systems and staves per system. The given staves can be in any order.
	 */
	public StaffStampings(List<StaffStamping> allStaves, int systemsCount, int stavesCount) {
		//init arrays
		stavesByStaff = alist(stavesCount);
		for (int i = 0; i < stavesCount; i++) {
			stavesByStaff.add(alistInit((StaffStamping) null, systemsCount));
		}
		stavesBySystem = alist(systemsCount);
		for (int i = 0; i < systemsCount; i++) {
			stavesBySystem.add(alistInit((StaffStamping) null, stavesCount));
		}
		//fill with staves
		for (StaffStamping s : allStaves) {
			int staffIndex = s.getStaffIndex();
			int systemIndex = s.getSystemIndex();
			stavesByStaff.get(staffIndex).set(systemIndex, s);
			stavesBySystem.get(systemIndex).set(staffIndex, s);
		}
	}

	/**
	 * Gets the staff stamping for the given global staff index and
	 * system index, or null if unset.
	 */
	public StaffStamping get(int system, int staff) {
		return stavesByStaff.get(staff).get(system);
	}

	/**
	 * Gets the staves of the given global staff index (one per system).
	 */
	public List<StaffStamping> getAllOfStaff(int staff) {
		return stavesByStaff.get(staff);
	}

	/**
	 * Gets the staves of the given system.
	 */
	public List<StaffStamping> getAllOfSystem(int system) {
		return stavesBySystem.get(system);
	}

	/**
	 * Adds all staves to the given list, system by system.
	 */
	public void addAllTo(List<StaffStamping> list) {
		for (int i = 0; i < stavesBySystem.size(); i++)
			list.addAll(stavesBySystem.get(i));
	}

}
