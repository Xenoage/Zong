package com.xenoage.zong.musiclayout.layouter.cache.index;

import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;

/**
 * Index for elements in the {@link NotationsCache}.
 * 
 * For {@link VoiceElement}s and {@link MeasureElement}s the
 * element is the key itself. For {@link ColumnElement}s also
 * the staff index is needed, because the same element is placed
 * on multiple staves but with different notations.
 * 
 * TODO: needed?
 * 
 * @author Andreas Wenger
 */
public interface NotationsCacheIndex {

}
