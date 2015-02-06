package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * Mock object for a {@link Notation}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class NotationMock
	implements Notation {

	@Getter private MusicElement element;
	@Getter private ElementWidth width;

}
