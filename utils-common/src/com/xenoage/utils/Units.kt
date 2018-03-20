package com.xenoage.utils

import com.xenoage.utils.math.Size2f

/** A length in pt */
typealias Pt = Float

/** A length in mm */
typealias Mm = Float

/** A 2D size in mm */
typealias SizeMm = Size2f

/** Unit conversion constants. */
const val mmToPx_1_1 = 1 * 72.0f / 25.4f * 1
const val pxToMm_1_1 = 1 / mmToPx_1_1
