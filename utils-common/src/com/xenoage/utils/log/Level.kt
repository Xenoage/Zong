package com.xenoage.utils.log

/**
 * Logging level.
 */
enum class Level private constructor(
		/** String with 8 characters for this log level */
		val fixedString: String) {

	/**
	 * Error message is shown, program has to be closed.
	 * For example, if inconsistent score data is found.
	 */
	Fatal("FATAL   "),

	/**
	 * Error message is shown, program can continue.
	 * For example, if a command fails that should always work.
	 */
	Error("ERROR   "),

	/**
	 * Just a warning, which is shown on the screen, too.
	 * For example, if a file can not be opened because it doesn't exist.
	 */
	Warning("WARNING "),

	/**
	 * An event, which is not interesting for the user, but should be logged.
	 * For example, if a opened file has an invalid format, but can be opened anyway.
	 */
	Remark("REMARK  ");

	/**
	 * Returns true, if this log level is included in the given other level.
	 * E.g. [Remark] includes all other levels, but [Error] does not include [Warning].
	 */
	fun isIncludedIn(topLevel: Level): Boolean {
		return this.ordinal <= topLevel.ordinal
	}

}
