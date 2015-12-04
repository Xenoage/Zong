package com.xenoage.zong.musiclayout.notation.beam;

/**
 * Form of a beam line at a stem.
 * 
 * For example, a beam might have at a specific
 * stem a continuous line on the 16th line, but a
 * left hook on the 32nd line.
 * 
 * @author Andreas Wenger
 */
public enum Fragment {
	/** No beam line or within a continuous line. */
	None,
	/** A continuous line starts here. */
	Start,
	/** A continuous line ends here. */
	Stop,
	/** Just a hook at the left side. */
	HookLeft,
	/** Just a hook at the right side. */
	HookRight,
	/** End of continuous line, but extend the line to the right hook on the next level.
	 * TODO: no example found for this beam */
	@Deprecated StopHookRight
}
