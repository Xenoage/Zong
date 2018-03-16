package com.xenoage.zong.utils.exceptions

import com.xenoage.zong.core.position.MP


/**
 * This exception is thrown when an illegal [MP] is used.
 */
class IllegalMPException(
		val mp: MP,
		message: String? = null
) : Exception(message) {

	override val message: String
		get() = "${super.message?.plus(" - ")}Invalid MP: $mp"

}
