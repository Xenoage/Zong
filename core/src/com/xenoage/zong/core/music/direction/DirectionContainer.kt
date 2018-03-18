package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement

/**
 * Common interface for [Measure] and [VoiceElement].
 * Both may contain [Direction]s.
 */
interface DirectionContainer : MPContainer