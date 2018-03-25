package com.xenoage.zong.utils.exceptions

import com.xenoage.zong.core.Score


/**
 * This exception is used when there is a situation which would
 * lead to an inconsistent [Score].
 */
class InconsistentScoreException(message: String?) : Exception(message)
