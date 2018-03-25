package com.xenoage.utils.annotations

import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * Annotation for untested code.
 *
 * While maybe a lot of code in a project is not covered
 * by tests, code marked with this annotation is especially
 * worth testing.
 */
@Retention(SOURCE)
annotation class Untested
