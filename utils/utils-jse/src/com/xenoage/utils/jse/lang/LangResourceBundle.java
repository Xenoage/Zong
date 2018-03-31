package com.xenoage.utils.jse.lang;

import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.lang.VocID;

import java.util.*;

/**
 * This class provides access to {@link Lang} language packs
 * via the {@link ResourceBundle} interface.
 * 
 * The keys are simply String encoded {@link VocID} values,
 * with some special additions:
 * <ul>
 *   <li>if the resource ID ends with ":", {@link Lang#getLabel(VocID)} is used</li>
 *   <li>if the resource ID ends with "...", {@link Lang#getWithEllipsis(VocID)} is used</li>
 * </ul>
 * 
 * If an unknown resource is queried, its key is returned. It is better
 * to have at least the vocabulary ID instead of nothing.
 * 
 * @author Andreas Wenger
 */
public class LangResourceBundle
	extends ResourceBundle {

	private Map<String, VocID> vocIDs;
	private Enumeration<String> vocIDStrings;


	/**
	 * Creates a new {@link LangResourceBundle} for the given
	 * vocabulary keys.
	 */
	public LangResourceBundle(VocID... vocIDs) {
		this.vocIDs = new HashMap<>();
		for (VocID vocID : vocIDs) {
			this.vocIDs.put(vocID.toString(), vocID);
		}
		this.vocIDStrings = Collections.enumeration(this.vocIDs.keySet());
	}

	@Override protected Object handleGetObject(String key) {
		if (key.endsWith(":")) {
			//read as label (usually the text, followed by ":")
			VocID vocID = vocIDs.get(key.substring(0, key.length() - 1));
			if (vocID == null)
				return key;
			return Lang.getLabel(vocID);
		}
		else if (key.endsWith("...")) {
			//read with ellipsis (text followed by "...")
			VocID vocID = vocIDs.get(key.substring(0, key.length() - 3));
			if (vocID == null)
				return key;
			return Lang.getWithEllipsis(vocID);
		}
		else {
			//normal case
			VocID vocID = vocIDs.get(key);
			if (vocID == null)
				return key;
			return Lang.get(vocID);
		}
	}

	@Override public Enumeration<String> getKeys() {
		return vocIDStrings;
	}

	/**
	 * Returns true, since we support each key to avoid
	 * problems with missing vocabulary.
	 */
	@Override public boolean containsKey(String key) {
		return true;
	}

}
