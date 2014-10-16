package com.xenoage.zong.core.music.annotation;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.format.Positioning;

/**
 * A fermata.
 * 
 * @author Andreas Wenger
 */
@Const @Data public class Fermata
	implements Annotation {
	
	/** The positioning of the fermata, or null (default position). */
	@MaybeNull private Positioning positioning;

}
