package com.xenoage.zong.documents;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;

/**
 * Class for a score document.
 * 
 * At the moment a score document has only one score, one layout and page view.
 * Later, it will be able to contain more than one score and more
 * than one layout (e.g. a main score and extracted parts).
 *
 * @author Andreas Wenger
 */
public class ScoreDoc {

	//the score
	Score score;

	//layout
	Layout layout;


	/**
	 * Creates a score document with the given score and an empty layout.
	 */
	public ScoreDoc(Score score) {
		this.score = score;
		//create an empty layout
		this.layout = new Layout(new LayoutDefaults(LayoutFormat.defaultValue, null,
			LayoutSettings.loadDefault()));
	}

	/**
	 * Gets the score.
	 */
	public Score getScore() {
		return score;
	}

	/**
	 * Sets the score and the updated layout.
	 * It is not possible to change the score without also updating the layout.
	 */
	public void setScoreAndLayout(Score score, Layout layout) {
		//check consistency
		for (Score sfcScore : layout.scoreFrameChains.keySet()) {
			if (sfcScore != score)
				throw new IllegalArgumentException("Unknown score");
		}
		//save score and layout
		Layout oldLayout = this.layout;
		this.score = score;
		this.layout = layout;
		layoutChanged(oldLayout, this.layout);
	}

	/**
	 * This method is called when the layout was replaced.
	 * @param oldLayout  the layout before the change
	 * @param newLayout  the layout after the change
	 */
	void layoutChanged(Layout oldLayout, Layout newLayout) {
	}

	/**
	 * Gets the layout of the document.
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * Sets the layout of the document.
	 */
	public void setLayout(Layout layout) {
		Layout oldLayout = this.layout;
		this.layout = layout;
		layoutChanged(oldLayout, layout);
	}

	/**
	 * Gets the {@link ScoreLayout}.
	 */
	public ScoreLayout getScoreLayout() {
		ScoreFrameChain chain = layout.scoreFrameChains.get(score);
		return layout.scoreLayouts.get(chain);
	}

}
