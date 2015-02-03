package com.xenoage.zong.core.music;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;

import com.xenoage.utils.annotations.Optimized;


/**
 * Interface for all musical elements,
 * like notes, rests, barlines or directions.
 *
 * @author Andreas Wenger
 */
public interface MusicElement {
	
	@Optimized(Performance) MusicElementType getMusicElementType();

}
