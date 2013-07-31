package com.xenoage.zong.core.music.util;

import java.util.ArrayList;

import com.xenoage.zong.core.music.Measure;


/**
 * Just a wrapper for a list of measures
 * within one column.
 * 
 * @author Andreas Wenger
 */
public final class Column
	extends ArrayList<Measure> {


	public Column(int size) {
		super(size);
	}

}
