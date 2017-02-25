package com.xenoage.zong.musiclayout.notation;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Mock object for a {@link Notation}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class NotationMock
	implements Notation {

	@Getter private MusicElement element;
	@Getter private MP mp;
	@Getter private ElementWidth width;

}
