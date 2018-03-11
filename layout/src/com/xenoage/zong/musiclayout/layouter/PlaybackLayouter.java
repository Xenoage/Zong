package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CList.ilist;

import com.xenoage.zong.core.position.Time;
import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.SystemCursorStamping;

/**
 * This class modifies a given {@link ScoreLayout}
 * so that the given playback situation is shown.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public class PlaybackLayouter {

	@NonNull private final ScoreLayout scoreLayout;

	private static IList<Stamping> noStampings = CList.<Stamping>ilist();


	/**
	 * Sets a {@link SystemCursorStamping} at the given {@link MP},
	 * representing the current playback position.
	 */
	public void setCursorAt(Time time) {
		//remove old playback cursors
		removePlaybackStampings();
		//create new one
		SystemCursorStamping cursor = createCursorAt(time);
		//find frame
		if (cursor != null) {
			ScoreFrameLayout frame = scoreLayout.getScoreFrameLayout(time.getMeasure());
			frame.setPlaybackStampings(ilist(cursor));
		}
	}
	
	/**
	 * Returns a new {@link SystemCursorStamping} at the given {@link Time},
	 * representing the current playback position, or null if there is none.
	 */
	public SystemCursorStamping createCursorAt(Time time) {
		ScoreFrameLayout frame = scoreLayout.getScoreFrameLayout(time.getMeasure());
		if (frame != null) {
			StaffStamping topStaff = frame.getStaffStamping(0, time.getMeasure());
			StaffStamping bottomStaff = frame.getStaffStamping(scoreLayout.score.getStavesCount() - 1,
					time.getMeasure());
			if (topStaff != null && bottomStaff != null)
				return new SystemCursorStamping(topStaff, bottomStaff, frame.getPositionX(time));
		}
		return null;
	}

	/**
	 * Removes all playback stampings.
	 */
	public void removePlaybackStampings() {
		for (ScoreFrameLayout frame : scoreLayout.frames) {
			frame.setPlaybackStampings(noStampings);
		}
	}

}
