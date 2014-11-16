package com.xenoage.zong.core.info;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;

/**
 * Class for rights.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class Rights {

	@NonNull private final String text;
	@MaybeNull private final String type;

}
