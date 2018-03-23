package com.xenoage.zong.core.util

import com.xenoage.zong.core.Score


/**
 * This exception is used when there is a situation which would
 * lead to an inconsistent [Score].
 */
class InconsistentScoreException(message: String?) : Exception(message)
