package com.xenoage.utils.annotations

import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * Annotation for entities which were optimized for performance
 * reasons or for saving memory. When this annotation is present,
 * the reader of the source code knows why the design of the code
 * may not optimal but was tuned for some reason.
 */
@Retention(SOURCE)
annotation class Optimized(val value: Reason = Reason.Unknown) {
}

/**
 * Reason for an optimization.
 */
enum class Reason {
	Performance,
	MemorySaving,
	Unknown
}
