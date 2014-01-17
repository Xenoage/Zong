package com.xenoage.zong.mobile.android.model;

import com.xenoage.utils.annotations.Const;

/**
 * A document, which consists of a name and a file name.
 * 
 * @author Andreas Wenger
 */
@Const public class Document {

	public final String name;
	public final String filename;


	public Document(String name, String filename) {
		this.name = name;
		this.filename = filename;
	}

}
