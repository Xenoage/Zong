package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CList.ilist;
import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.selection.Cursor;
import com.xenoage.zong.io.selection.ScoreSelection;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.stampings.StaffCursorStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * This class modifies a given {@link ScoreLayout}
 * so that the given selection situation is shown.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class ScoreSelectionLayouter {

	@NonNull private final ScoreLayout scoreLayout;

	private static final IList<Stamping> noStampings = CList.<Stamping> ilist();


	/**
	 * Sets the stampings for the given {@link ScoreSelection}.
	 */
	public void setSelection(ScoreSelection selection) {
		if (selection == null) {
			removeSelectionStampings();
		}
		else if (selection instanceof Cursor) {
			setCursor((Cursor) selection);
		}
	}

	/**
	 * Sets a {@link StaffCursorStamping} for the given {@link Cursor}.
	 */
	private void setCursor(Cursor cursor) {
		//remove old stampings
		removeSelectionStampings();
		//find frame
		MP mp = cursor.getMP();
		int measure = mp.measure;
		ScoreFrameLayout frame = scoreLayout.getScoreFrameLayout(measure);
		if (frame != null) {
			StaffStamping staff = frame.getStaffStamping(mp.staff, measure);
			if (staff != null) {
				frame.setSelectionStampings(ilist(
					new StaffCursorStamping(staff, frame.getPositionX(mp), -0.5f)));
			}
		}
	}

	/**
	 * Removes all selection stampings.
	 */
	public void removeSelectionStampings() {
		for (ScoreFrameLayout frame : scoreLayout.frames) {
			frame.setSelectionStampings(noStampings);
		}
	}

}
