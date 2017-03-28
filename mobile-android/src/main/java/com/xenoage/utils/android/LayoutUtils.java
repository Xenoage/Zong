package com.xenoage.utils.android;

import android.view.View;
import android.view.ViewGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Useful functions for working with Android layouts.
 *
 * @author Andreas Wenger
 */
public class LayoutUtils {

	private static final ViewGroup.LayoutParams matchParent =
		new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);

	public static void setMatchParent(View view) {
		view.setLayoutParams(matchParent);
	}
}
