package com.xenoage.zong.io.musicxml.in.util;

import lombok.AllArgsConstructor;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.music.volta.Volta;

/**
 * An unclosed {@link Volta}, needed during MusicXML import.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class OpenVolta {

	public int startMeasure;
	public Range numbers;
	public String caption;

}