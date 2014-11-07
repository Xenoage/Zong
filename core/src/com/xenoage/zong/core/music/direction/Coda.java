package com.xenoage.zong.core.music.direction;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.zong.core.header.ColumnHeader;


/**
 * Class for a coda sign.
 * 
 * It must always be written to the {@link ColumnHeader}.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class Coda
	extends Direction
	implements NavigationMarker {

}
