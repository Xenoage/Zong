package com.xenoage.zong.io.musicxml.link;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * MusicXML link-attributes.
 * Currently, only the href attribute is supported.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class LinkAttributes {

	private String href;

}
