package com.xenoage.zong.io.musicxml.opus;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.zong.io.musicxml.link.LinkAttributes;

/**
 * Link to a score within an {@link Opus}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class Score
	implements OpusItem {

	/** A link to the XML document. */
	private LinkAttributes link;
	/** True if score begins on new page, false if not, null for undefined. */
	private Boolean newPage;

}
