package com.xenoage.zong.core.format;

import com.xenoage.utils.annotations.Const;

import lombok.Data;
import lombok.experimental.Wither;


/**
 * Default formats for a layout.
 *
 * @author Andreas Wenger
 */
@Const @Data @Wither public final class LayoutFormat {

	private final PageFormat leftPageFormat, rightPageFormat;

	/** Default layout format, consisting of the default {@link PageFormat} for both sides. */
	public static final LayoutFormat defaultValue = new LayoutFormat(PageFormat.defaultValue, PageFormat.defaultValue);


	/**
	 * Gets the format of the given page.
	 * @param pageIndex  the 0-based index of the page
	 */
	public PageFormat getPageFormat(int pageIndex)
		throws IllegalArgumentException {
		if (pageIndex % 2 == 0)
			return rightPageFormat;
		else
			return leftPageFormat;
	}


}
