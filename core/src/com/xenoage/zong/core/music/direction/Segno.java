package com.xenoage.zong.core.music.direction;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.position.MP;


/**
 * Class for a segno sign.
 * 
 * It must always be written to the {@link ColumnHeader}.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class Segno
	extends Direction
	implements NavigationSign {

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.Segno;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
}
