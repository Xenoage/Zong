package com.xenoage.zong.core.music

import com.xenoage.zong.core.format.Break
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.direction.Tempo
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.volta.Volta
import com.xenoage.zong.core.position.MPElement


/**
 * Column elements are [MPElement]s, which can
 * appear in a whole measure column.
 *
 * This is the case for [TimeSignature], [Barline], [Volta],
 * [Key], [Tempo] and [Break].
 */
interface ColumnElement : MPElement
