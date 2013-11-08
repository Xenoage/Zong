package com.xenoage.zong.core.format;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Size2f;


/**
 * Class for a page format, containing
 * width and height in mm and margins.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class PageFormat {

	private final Size2f size;
	private final PageMargins margins;

	/** Default page format, DIN A4, default {@link PageMargins}. */
	public static final PageFormat defaultValue = new PageFormat(new Size2f(210, 297), PageMargins.defaultValue);


	/**
	 * Gets the usable width of the page. This is the
	 * horizontal size minus the left and right margin.
	 */
	public float getUseableWidth() {
		return size.width - margins.getLeft() - margins.getRight();
	}


	/**
	 * Gets the usable height of the page. This is the
	 * vertical size minus the top and bottom margin.
	 */
	public float getUseableHeight() {
		return size.height - margins.getTop() - margins.getBottom();
	}

}
