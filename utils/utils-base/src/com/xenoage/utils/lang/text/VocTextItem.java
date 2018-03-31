package com.xenoage.utils.lang.text;

import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.lang.VocID;

/**
 * One piece of text identified by a vocabulary.
 * 
 * @author Andreas Wenger
 */
public class VocTextItem
	implements TextItem {

	private final VocID vocID;


	public VocTextItem(VocID vocID) {
		this.vocID = vocID;
	}

	@Override public String getText() {
		return Lang.get(vocID);
	}

}
