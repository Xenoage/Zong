package com.xenoage.zong.core.format;

import lombok.Data;

import com.xenoage.utils.annotations.Const;


/**
 * Class for page margins in mm:
 * left, right, top and bottom.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class PageMargins {

	private final float left, right, top, bottom;

	/** Default page margins of 20 mm. */
	public static final PageMargins defaultValue = new PageMargins(20, 20, 20, 20);

}
