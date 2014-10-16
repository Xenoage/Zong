package com.xenoage.zong.core.music.annotation;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.format.Placement;

/**
 * An articulation, like staccato or tenuto.
 * 
 * @author Andreas Wenger
 */
@Const @Data public class Articulation
	implements Annotation {
	
	/** The type of the articulation, like staccato or tenuto. */
	@NonNull private final ArticulationType type;
	
	/** The placement of the articulation: above, below or default (null). */
	@MaybeNull private Placement placement;
	
}
