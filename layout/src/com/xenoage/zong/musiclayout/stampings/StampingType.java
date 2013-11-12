package com.xenoage.zong.musiclayout.stampings;


/**
 * List of types of stampings.
 * 
 * Needed as a fast workaround for the missing
 * multiple dispatch feature in Java.
 * 
 * @author Andreas Wenger
 */
public enum StampingType
{
	BarlineStamping,
	BeamStamping,
	BracketStamping,
	CurvedLineStamping,
	EmptySpaceStamping,
	KeySignatureStamping,
	FlagsStamping,
	LegerLineStamping,
	NormalTimeStamping,
	StaffCursorStamping,
	StaffStamping,
	StaffSymbolStamping,
	StemStamping,
	SystemCursorStamping,
	TestStamping,
	TextStamping,
	TupletStamping,
	VoltaStamping,
	WedgeStamping
}
