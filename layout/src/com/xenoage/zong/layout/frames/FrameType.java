package com.xenoage.zong.layout.frames;


/**
 * List of types of frames.
 * 
 * Needed as a fast workaround for the missing
 * multiple dispatch feature in Java.
 * 
 * @author Andreas Wenger
 */
public enum FrameType
{
	GroupFrame,
	ImageFrame,
	ScoreFrame,
	TemplateFrame,
	TextFrame,
	/** For frame handles (selected frames) */
	FrameHandles;
}
