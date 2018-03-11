package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.ScoreFrameLayouter.scoreFrameLayouter;
import static com.xenoage.zong.musiclayout.notator.Notator.notator;
import static com.xenoage.zong.musiclayout.spacer.BeamsSpacer.beamsSpacer;
import static com.xenoage.zong.musiclayout.spacer.ColumnsSpacer.columnsSpacer;
import static com.xenoage.zong.musiclayout.spacer.FramesSpacer.framesSpacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacer.frame.fill.FrameFiller;
import com.xenoage.zong.musiclayout.spacer.system.fill.SystemFiller;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.FramesSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * A score layouter creates the content for
 * score frames from a given score.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayouter {

	//input
	private Context context;
	private Target target;

	//output
	private ScoreLayout layout = null;


	/**
	 * Creates a {@link ScoreLayouter} for the given score document and target.
	 */
	public ScoreLayouter(ScoreDoc doc, Target target) {
		LayoutDefaults defaults = doc.getLayout().getDefaults();
		this.context = new Context(doc.getScore(), defaults.getSymbolPool(), defaults.getLayoutSettings());
		this.target = target;
	}

	public ScoreLayouter(Context context, Target target) {
		this.context = context;
		this.target = target;
	}

	/**
	 * Creates the {@link ScoreLayout}. If an error occurs, an error layout
	 * is returned.
	 */
	public ScoreLayout createScoreLayout() {
		try {
			layout = createLayoutWithExceptions();
		} catch (Exception ex) {
			//exception during the layouting process. show error page
			INSTANCE.log(Companion.warning("Layouting failed", ex));
			layout = ScoreLayout.createErrorLayout(context.score, context.symbols);
		}
		return layout;
	}

	
	/**
	 * Computes the whole layout and returns it.
	 * If something fails, an exception is thrown.
	 */
	public ScoreLayout createLayoutWithExceptions() {
		
		//notations of elements
		Notations notations = notator.computeAll(context);
		
		//compute optimal measure column spacings
		List<ColumnSpacing> columns = columnsSpacer.compute(notations, context);
		
		//break columns into systems and frames
		FramesSpacing frames = framesSpacer.compute(columns, target, context, notations);
		
		//system stretching (horizontal)
		fillSystemsHorizontally(frames, target);
		//frame filling (vertical)
		fillFramesVertically(frames, target, context.score);
		
		//compute beam spacings. these are computed only now, after the horizontal
		//and vertical spacing of the score is fixed, since the beam slants depend on the
		//exact spacings
		Map<Beam, BeamSpacing> beams = beamsSpacer.compute(context.score, notations, frames);
		
		//create score frame layouts from the collected information
		List<ScoreFrameLayout> scoreFrameLayouts = createScoreFrameLayouts(frames,
			notations, context, beams);
		
		//create score layout
		return new ScoreLayout(context.score, scoreFrameLayouts, context.symbols, context.settings);
	}

	/**
	 * Fills the systems horizontally according to the {@link SystemFiller}
	 * of the frame.
	 */
	void fillSystemsHorizontally(FramesSpacing frames, Target target) {
		for (int iFrame : range(frames.size())) {
			FrameSpacing frameArr = frames.get(iFrame);
			SystemFiller hFill = target.getArea(iFrame).hFill;
			//apply strategy
			for (SystemSpacing oldSystemArr : frameArr.getSystems()) {
				float usableWidth = frameArr.getUsableSizeMm().width - oldSystemArr.getMarginLeftMm() -
					oldSystemArr.getMarginRightMm();
				hFill.compute(oldSystemArr, usableWidth);
			}
		}
	}

	/**
	 * Fills the frames vertically according to the {@link FrameFiller} of the frame.
	 */
	void fillFramesVertically(FramesSpacing frames, Target target, Score score) {
		for (int iFrame : range(frames.size())) {
			FrameFiller vFill = target.getArea(iFrame).vFill;
			vFill.compute(frames.get(iFrame), score);
		}
	}

	/**
	 * Creates all {@link ScoreFrameLayout}s.
	 */
	List<ScoreFrameLayout> createScoreFrameLayouts(FramesSpacing frames,
		Notations notations, Context context, Map<Beam, BeamSpacing> beamsSpacing) {
		ArrayList<ScoreFrameLayout> ret = alist();
		ArrayList<ContinuedElement> continuedElements = alist();
		for (int iFrame : range(frames.size())) {
			ScoreFrameLayout sfl = scoreFrameLayouter.computeScoreFrameLayout(frames.get(iFrame),
				iFrame, notations, continuedElements, context, beamsSpacing);
			ret.add(sfl);
			continuedElements = sfl.getContinuedElements();
		}
		return ret;
	}

}
