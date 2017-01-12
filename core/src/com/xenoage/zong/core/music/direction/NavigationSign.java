package com.xenoage.zong.core.music.direction;

/**
 * Interface for {@link Direction}s with modify the playback sequence,
 * like {@link DaCapo} or {@link Coda}.
 *
 * The term "navigation sign" is inspired by Wikipedia's article
 * on "Dal Segno", which calls it a "navigation marker".
 *
 * Navigation signs can either be placed at the beginning or at the end
 * of a measure column. At the beginning, it is a target sign, i.e. a jump
 * ends here and this measure is played next. At the ending, it is an
 * origin sign, i.e. the jump is performed from here after the measure is played.
 * 
 * @author Andreas Wenger
 */
public interface NavigationSign {

}
