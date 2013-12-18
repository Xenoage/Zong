package com.xenoage.zong.io.musicxml.opus;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.zong.io.musicxml.link.LinkAttributes;

/**
 * Link to another {@link Opus}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class OpusLink
	implements OpusItem {

	private LinkAttributes link;

}
