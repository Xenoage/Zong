package com.xenoage.zong.musiclayout.spacer.beam.placement;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.musiclayout.SLP;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import lombok.AllArgsConstructor;

/**
 * Computes the {@link Placement} of a beam on two adjacent staves, given its {@link Slant}.
 * 
 * We find the dictator stem of the upper staff (pointing down) and the dictator stem of
 * the lower staff (pointing up). The beam is centered between these positions.
 * 
 * @author Andreas Wenger
 */
public class TwoStavesBeamPlacer {
	
	public static final TwoStavesBeamPlacer twoStavesBeamPlacer = new TwoStavesBeamPlacer();

	/**
	 * Vertical placement of a beam on two adjacent staves,
	 * defined by the {@link SLP}s of the left and the right stem.
	 *
	 * @author Andreas Wenger
	 */
	@Const @AllArgsConstructor
	public static final class Placement {
		public final SLP leftSlp, rightSlp;
	}


	/**
	 * Computes the {@link Placement} of a beam on two adjacent staves.
	 * @param slant          the slant for this beam
	 * GOON
	 */
	public Placement compute(Slant slant) {
		return null;
	}
	
}
