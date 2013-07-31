package com.xenoage.zong.core.music.clef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.xenoage.utils.base.annotations.NonNull;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.position.MPContainer;


/**
 * Class for a clef.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class Clef
	implements MeasureElement
{

	/** The type of the clef. */
	@Getter @Setter @NonNull private ClefType type;
	
	/** Back reference: the parent element, or null, if not part of a score. */
	@Getter @Setter private MPContainer parent;

}
