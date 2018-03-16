package com.xenoage.zong.core.music

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason.Performance


/**
 * Interface for all musical elements,
 * like notes, rests, barlines or directions.
 */
interface MusicElement {

	@Optimized(Performance)
	val elementType: MusicElementType

	/**
	 * Returns true, if this [MusicElement] is not null and of the given type.
	 */
	infix fun of(type: MusicElementType): Boolean =
		this != null && elementType == type

}
