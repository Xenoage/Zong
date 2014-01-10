package com.xenoage.zong.documents;

import lombok.Getter;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;

/**
 * Class for a score document.
 * 
 * At the moment a score document has only one score, one layout and page view.
 * Later, it will be able to contain more than one score and more
 * than one layout (e.g. a main score and extracted parts).
 *
 * @author Andreas Wenger
 */
public class ScoreDoc
	implements Document {

	/** The score. */
	@Getter private Score score;
	/** The layout of the document. */
	@Getter private Layout layout;
	
	/** Performs commands on this score document and supports undo. */
	@Getter private CommandPerformer commandPerformer = new CommandPerformer(this);
	/** Supported formats for reading score documents from files and writing them to files. */
	@Getter private SupportedFormats<ScoreDoc> supportedFormats = null;


	/**
	 * Creates a score document with the given score and an empty layout.
	 */
	public ScoreDoc(Score score, LayoutDefaults layoutDefaults) {
		this.score = score;
		//create an empty layout
		this.layout = new Layout(layoutDefaults);
	}

	/**
	 * Sets the score and the updated layout.
	 * It is not possible to change the score without also updating the layout.
	 */
	public void setScoreAndLayout(Score score, Layout layout) {
		//check consistency
		/* for (Score sfcScore : layout.scoreFrameChains.keySet()) {
			if (sfcScore != score)
				throw new IllegalArgumentException("Unknown score");
		} */
		//save score and layout
		this.score = score;
		this.layout = layout;
		layoutChanged();
	}

	/**
	 * This method is called when the layout was changed.
	 */
	void layoutChanged() {
	}

	/**
	 * Sets the layout of the document.
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
		layoutChanged();
	}

	/**
	 * Gets the {@link ScoreLayout}.
	 */
	public ScoreLayout getScoreLayout() {
		ScoreFrameChain chain = layout.getScoreFrameChain(score);
		return chain.getScoreLayout();
	}

}
