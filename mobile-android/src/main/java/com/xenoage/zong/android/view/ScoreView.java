package com.xenoage.zong.android.view;

import com.xenoage.zong.android.view.provider.ScoreViewProvider;

/**
 * View of a score, consisting of pages.
 *
 * @author Andreas Wenger
 */
public class ScoreView {

	private ScoreViewProvider provider;

	public ScoreView(ScoreViewProvider provider) {
		this.provider = provider;
	}

	/**
	 * Gets the number of pages in this score.
	 */
	public int getPagesCount() {
		return provider.getPagesCount();
	}

	/**
	 * Gets the page with the given index.
	 */
	public PageView getPage(int pageIndex) {
		return provider.createPageView(pageIndex);
	}

}
