package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.base.NullUtils.throwNullArg;
import static com.xenoage.utils.pdlib.PVector.pvec;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.position.BMP;
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
public class PlaybackLayouter
{
	
	private final ScoreLayout scoreLayout;
	
	private static PVector<Stamping> noStampings = pvec();
	
	
	/**
	 * Creates a new {@link PlaybackLayouter} for the given {@link ScoreLayout}.
	 */
	public PlaybackLayouter(ScoreLayout scoreLayout)
	{
		throwNullArg(scoreLayout);
		this.scoreLayout = scoreLayout;
	}
	
	
	/**
	 * Sets a {@link SystemCursorStamping} at the given {@link BMP},
	 * representing the current playback position.
	 */
	public void setCursorAt(BMP bmp)
	{
		//remove old playback cursors
		removePlaybackStampings();
		//find frame
		int measure = bmp.measure;
		ScoreFrameLayout frame = scoreLayout.getScoreFrameLayout(measure);
		if (frame != null)
		{
			StaffStamping topStaff = frame.getStaffStamping(0, measure);
			StaffStamping bottomStaff = frame.getStaffStamping(scoreLayout.score.getStavesCount() - 1, measure);
			if (topStaff != null && bottomStaff != null)
			{
				frame.setPlaybackStampings(pvec(new SystemCursorStamping(topStaff, bottomStaff,
					frame.getPositionX(bmp))));
			}
			return;
		}
	}
	
	
	/**
	 * Removes all playback stampings.
	 */
	public void removePlaybackStampings()
	{
		for (ScoreFrameLayout frame : scoreLayout.frames)
		{
			frame.setPlaybackStampings(noStampings);
		}
	}
	

}
