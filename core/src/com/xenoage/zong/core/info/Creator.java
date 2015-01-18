package com.xenoage.zong.core.info;

import lombok.Data;

import com.xenoage.utils.annotations.Const;

/**
 * Class for a creator.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class Creator {

	private final String name;
	private final String type;

}
