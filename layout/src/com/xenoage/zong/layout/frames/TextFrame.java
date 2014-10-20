package com.xenoage.zong.layout.frames;

import lombok.Getter;

import com.xenoage.zong.core.text.FormattedText;

/**
 * A {@link TextFrame} is a frame that contains a {@link FormattedText}.
 * 
 * It may use multiple fonts, colors and styles and may contain
 * several paragraphs.
 * 
 * @author Andreas Wenger
 */
public class TextFrame
	extends Frame {

	
	/**
	 * Line break style.
	 */
	public enum LineBreakStyle {
		/** If there is no line break in the formatted text,
		 * the text will continue beyond the borders of the frame. */
		Manual,
		/** Pragraphs are automatically broken into lines fitting
		 * into the borders of the frame. */
		Automatic
	};
	
	/** The text content of this frame. */
	@Getter private FormattedText text = FormattedText.empty;
	/** Line break style in this frame. */
	@Getter private LineBreakStyle lineBreakStyle = LineBreakStyle.Manual;
	
	/** The formatted text, including line breaks. */
	private transient FormattedText cacheTextLineBreak = null;


	/**
	 * Sets the text content of this frame.
	 */
	public void setText(FormattedText text) {
		this.text = text;
		updateCache();
	}

	/**
	 * Gets the text content of this frame with automatic line breaks,
	 * if enabled, otherwise the original text.
	 */
	public FormattedText getTextWithLineBreaks() {
		if (lineBreakStyle == LineBreakStyle.Automatic) {
			return cacheTextLineBreak;
		}
		else {
			return text;
		}
	}

	/**
	 * Sets the type of line breaks.
	 */
	public void setLineBreakStyle(LineBreakStyle lineBreakStyle) {
		this.lineBreakStyle = lineBreakStyle;
		updateCache();
	}

	@Override public FrameType getType() {
		return FrameType.TextFrame;
	}

	/**
	 * Updates the cache:
	 * Text with line breaks for text frame with automatic line break.
	 */
	private void updateCache() {
		if (lineBreakStyle == LineBreakStyle.Automatic) {
			cacheTextLineBreak = text.lineBreak(size.width);
		}
		else {
			cacheTextLineBreak = null;
		}
	}

}
