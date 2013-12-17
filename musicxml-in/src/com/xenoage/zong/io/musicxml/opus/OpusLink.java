package com.xenoage.zong.io.musicxml.opus;

import lombok.Data;

import com.xenoage.zong.io.musicxml.link.LinkAttributes;

/**
 * Link to another {@link Opus}.
 * 
 * @author Andreas Wenger
 */
@Data
public class OpusLink
	implements OpusItem {

	private LinkAttributes link;

}
