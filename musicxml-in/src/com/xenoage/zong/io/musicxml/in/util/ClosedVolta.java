package com.xenoage.zong.io.musicxml.in.util;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.volta.Volta;

/**
 * A {@link Volta} and its start measure.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class ClosedVolta {
	public Volta volta;
	public int measure;
}
