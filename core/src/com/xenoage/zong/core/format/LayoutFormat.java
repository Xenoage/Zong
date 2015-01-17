package com.xenoage.zong.core.format;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Default formats for a layout.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class LayoutFormat {

	/** Default layout format, consisting of the default {@link PageFormat} for both sides. */
	public static final LayoutFormat defaultValue = new LayoutFormat(PageFormat.defaultValue, PageFormat.defaultValue);
	
	private PageFormat leftPageFormat, rightPageFormat;
	

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
