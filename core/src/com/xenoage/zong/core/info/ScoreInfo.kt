package com.xenoage.zong.core.info

/**
 * Information about a single score,
 * like title or composer.
 */
class ScoreInfo(
	/** Title of the work */
	var workTitle: String? = null,
	/** Number of the work */
	var workNumber: String? = null,
	/** Title of the movement */
	var movementTitle: String? = null,
	/** Number of the movement */
	var movementNumber: String? = null,
	/** List of creators (composers, arrangers, ...)  */
	var creators: MutableList<Creator> = mutableListOf(),
	/** List of rights.  */
	var rights: MutableList<Rights> = mutableListOf()
) {

	/**
	 * Gets the first mentioned composer of this score, or null
	 * if unavailable.
	 */
	val composer: String?
		get() = creators.find { it.type == "composer" }?.name

	/**
	 * Gets the title of the score. This is the movement-title, or if unknown,
	 * the work-title. If both are unknown, null is returned.
	 */
	val title: String?
		get() = movementTitle ?: workTitle

}
