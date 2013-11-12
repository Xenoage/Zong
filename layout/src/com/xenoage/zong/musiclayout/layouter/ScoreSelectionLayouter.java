package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.base.NullUtils.throwNullArg;
import static com.xenoage.utils.pdlib.PVector.pvec;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.position.BMP;
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
public class ScoreSelectionLayouter
{
	
	private final ScoreLayout scoreLayout;
	
	private static PVector<Stamping> noStampings = pvec();
	
	
	/**
	 * Creates a new {@link ScoreSelectionLayouter} for the given {@link ScoreLayout}.
	 */
	public ScoreSelectionLayouter(ScoreLayout scoreLayout)
	{
		throwNullArg(scoreLayout);
		this.scoreLayout = scoreLayout;
	}
	
	
	/**
	 * Sets the stampings for the given {@link ScoreSelection}.
	 */
	public void setSelection(ScoreSelection selection)
	{
		if (selection == null)
		{
			removeSelectionStampings();
		}
		else if (selection instanceof Cursor)
		{
			setCursor((Cursor) selection);
		}
	}
	
	
	/**
	 * Sets a {@link StaffCursorStamping} for the given {@link Cursor}.
	 */
	private void setCursor(Cursor cursor)
	{
		//remove old stampings
		removeSelectionStampings();
		//find frame
		BMP bmp = cursor.getBMP();
		int measure = bmp.measure;
		ScoreFrameLayout frame = scoreLayout.getScoreFrameLayout(measure);
		if (frame != null)
		{
			StaffStamping staff = frame.getStaffStamping(bmp.staff, measure);
			if (staff != null)
			{
				frame.setSelectionStampings(pvec(new StaffCursorStamping(staff, frame.getPositionX(bmp), -0.5f)));
			}
		}
	}
	
	
	/**
	 * Removes all selection stampings.
	 */
	public void removeSelectionStampings()
	{
		for (ScoreFrameLayout frame : scoreLayout.frames)
		{
			frame.setSelectionStampings(noStampings);
		}
	}


}
