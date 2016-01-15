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
	
	/** Staves from 1 to 10 lines. */
	private static final StaffLines[] staffLines = {
		new StaffLines(1), new StaffLines(2), new StaffLines(3), new StaffLines(4), new StaffLines(5),
		new StaffLines(6), new StaffLines(7), new StaffLines(8), new StaffLines(9), new StaffLines(10) 
	};
	

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
	
	
	/**
	 * Gets the {@link StaffLines} instance for the given number of staff lines.
	 */
	public static StaffLines staffLines(int count) {
		if (count < 1)
			throw new IllegalArgumentException("Staff must have at least one line");
		else if (count <= 10)
			return staffLines[count-1];
		else
			return new StaffLines(count);
	}
	
	private StaffLines(int count) {
		this.count = count;
		this.middleLp = count - 1;
		this.topLp = (count - 1) * 2;
		this.topLegerLp = count * 2;
	}
	
}
