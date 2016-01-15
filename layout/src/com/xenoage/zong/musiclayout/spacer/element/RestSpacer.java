package com.xenoage.zong.musiclayout.spacer.element;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.notation.RestNotation;
import com.xenoage.zong.musiclayout.spacing.RestSpacing;

/**
 * Computes the {@link RestSpacing}s for a {@link Rest}.
 * 
 * @author Andreas Wenger
 */
public class RestSpacer {
	
	public static final RestSpacer restSpacer = new RestSpacer();
	
	
	public RestSpacing compute(RestNotation notation, Fraction beat, float offsetIs,
		int staffLinesCount) {
		int lp = getRestLp(staffLinesCount, notation.duration);
		return new RestSpacing(notation, beat, offsetIs, lp);
	}
	
	/**
	 * The quarter rest is centered around the middle
	 * line of the staff, the half rest sits on the
	 * middle line and the whole rest hangs on the
	 * line over the middle staff.
	 */
	private int getRestLp(int staffLinesCount, DurationInfo.Type duration) {
		if (duration == DurationInfo.Type.Whole) {
			//whole rest hangs on the line above the middle
			//(the symbol hangs from the origin)
			return staffLinesCount + 1;
		}
		else if (duration == DurationInfo.Type.Half) {
			//half rest sits on the line under the middle
			//(the symbol sits on the origin)
			return staffLinesCount - 1;
		}
		else {
			//all other rests are centered on the middle line
			//(the symbol is vertically centered on the origin)
			return staffLinesCount - 1;
		}
	}

}
