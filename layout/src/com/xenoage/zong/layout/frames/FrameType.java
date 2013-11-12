package com.xenoage.zong.layout.frames;

/**
 * List of types of frames.
 * 
 * Needed as a fast workaround for the missing
 * multiple dispatch feature in Java.
 * 
 * @author Andreas Wenger
 */
public enum FrameType {
	/** {@link GroupFrame} frame. */
	GroupFrame,
	/** {@link ImageFrame} frame. */
	ImageFrame,
	/** {@link ScoreFrame} frame. */
	ScoreFrame,
	/** Template frame. */
	TemplateFrame,
	/** {@link TextFrame} frame. */
	TextFrame,
	/** For frame handles (selected frames) */
	FrameHandles;
}
