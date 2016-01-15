package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;

import com.xenoage.utils.annotations.Optimized;

/**
 * List of types of stampings.
 * 
 * Needed as a fast workaround for the missing
 * multiple dispatch feature in Java and for the missing
 * Reflection in GWT.
 * 
 * @author Andreas Wenger
 */
@Optimized(Performance)
public enum StampingType {
	BarlineStamping,
	BeamStamping,
	BracketStamping,
	EmptySpaceStamping,
	KeySignatureStamping,
	FlagsStamping,
	LegerLineStamping,
	SlurStamping,
	StaffCursorStamping,
	StaffStamping,
	StaffSymbolStamping,
	StemStamping,
	SystemCursorStamping,
	TestStamping,
	TextStamping,
	NormalTimeStamping,
	TupletStamping,
	VoltaStamping,
	WedgeStamping
}
