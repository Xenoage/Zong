package com.xenoage.zong.core.music.time

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.position.MP
import lombok.NonNull
import lombok.RequiredArgsConstructor


/**
 * A time signature.
 */
class TimeSignature(
		/** The time signature type. */
		val type: TimeType
) : ColumnElement {

	/** Back reference: the parent column header, or null, if not part of a score. */
	override var parent: ColumnHeader? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Time


	companion object {
		/** Implicit senza misura object. Do not use it within a score,
		 * but only as a return value to indicate that there is no time signature.  */
		val implicitSenzaMisura = TimeSignature(TimeType.timeSenzaMisura)
	}

}
