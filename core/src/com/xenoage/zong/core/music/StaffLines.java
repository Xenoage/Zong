package com.xenoage.zong.core.music;

import com.xenoage.utils.annotations.Const;

/**
 * Number of lines in a staff. In most cases 5-line staves are used.
 * 
 * Instead of just using an <code>int</code> value for the number of staff lines,
 * this class also provides information about LPs in the staff (like the bottom,
 * middle or top line LP), so that it does not have to be recomputed each time.
 * 
 * @author Andreas Wenger
 */
@Const
public final class StaffLines {
	
	/** Normal 5-line staff. */
	public static final StaffLines staff5Lines = new StaffLines(5);
	

	/** Number of staff lines. */
	public final int count;
	/** LP of the bottom line. */
	public final int bottomLp = 0; //fixed by definition of LP
	/** LP of the middle line. For an even number of staff lines, this will
	 * return the LP of the middle space.
	 */
	public final int middleLp;
	/** LP of the top line. */
	public final int topLp;
	/** LP of the first bottom leger line LP. */
	public final int topLegerLp;
	/** LP of the first bottom leger line LP. */
	public final int bottomLegerLp = -2; //fixed by definition of LP
	
	
	public StaffLines(int count) {
		this.count = count;
		this.middleLp = count - 1;
		this.topLp = (count - 1) * 2;
		this.topLegerLp = count * 2;
	}
	
}
