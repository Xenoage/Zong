package com.xenoage.zong.layout;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.pdlib.PMap.pmap;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.layout.LP.lp;
import static com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea.area;

import java.util.HashMap;

import com.xenoage.utils.base.annotations.Untested;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.pdlib.PMap;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.position.BMP;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.FP;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.ScoreLP;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.util.event.ScoreChangedEvent;


/**
 * Class for the layout of a score document.
 * 
 * It consists of several pages, each containing several frames.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class Layout
{
	
	/** Default settings */
	public final LayoutDefaults defaults;

	/** The list of pages */
	public final PVector<Page> pages;
	
	/** The chains of score frames */
	public final PMap<Score, ScoreFrameChain> scoreFrameChains;
	
	/** The musical layouts */
	public final PMap<ScoreFrameChain, ScoreLayout> scoreLayouts;
	
	/** The list of selected frames */
	public final PVector<Frame> selectedFrames;
	
	
	//cache of parent frames and pages for fast lookup
	HashMap<Frame, Page> parentPages = null;
	HashMap<Frame, GroupFrame> parentFrames = null;


	/**
	 * Initializes an empty {@link Layout}.
	 */
	public Layout(LayoutDefaults defaults)
	{
		this.defaults = defaults;
		this.pages = pvec();
		this.scoreFrameChains = pmap();
		this.scoreLayouts = pmap();
		this.selectedFrames = pvec();
	}


	private Layout(LayoutDefaults defaults, PVector<Page> pages, PMap<Score,
		ScoreFrameChain> scoreFrameChains, PMap<ScoreFrameChain, ScoreLayout> scoreLayouts, 
		PVector<Frame> selectedFrames)
	{
		this.pages = pages;
		this.scoreFrameChains = scoreFrameChains;
		this.scoreLayouts = scoreLayouts;
		this.defaults = defaults;
		this.selectedFrames = selectedFrames;
	}
	

	/**
	 * Adds a new page to this layout.
	 */
	public Layout plusPage(Page page)
	{
		return new Layout(defaults, pages.plus(page), scoreFrameChains, scoreLayouts,
			selectedFrames);
	}


	/**
	 * Transforms the given layout coordinates to a frame position, that is a
	 * reference to a frame and the position within that frame in mm. If there
	 * is no frame, null is returned.
	 */
	public FP computeFramePosition(LP layoutPosition)
	{
		if (layoutPosition == null)
			return null;
		Page page = pages.get(layoutPosition.pageIndex);
		return page.computeFramePosition(layoutPosition.position, this);
	}


	/**
	 * Call this method when the given score has been changed.
	 */
	public Layout scoreChanged(ScoreChangedEvent event)
	{
		return updateScoreLayouts(event.oldScore, event.newScore);
	}


	/**
	 * Computes the adjustment handle at the given position and returns it. If
	 * there is none, null is returned.
	 * @param layoutPosition the position where to look for a handle
	 * @param scaling the current scaling factor
	 */
	/* TODO: Editor
	public FrameHandle computeFrameHandleAt(LayoutPosition layoutPosition, float scaling)
	{
		if (layoutPosition == null)
			return null;
		// find handle on the given page
		FrameHandle handle = null;
		Page givenPage = pages.get(layoutPosition.getPageIndex());
		handle = givenPage.computeFrameHandleAt(layoutPosition.getPosition(), scaling);
		return handle;
	} */


	/**
	 * Gets a LinkedList of the selected frames on this layout
	 */
	/* TODO: Editor
	public LinkedList<Frame> getSelectedFrames()
	{
		LinkedList<Frame> frames = new LinkedList<Frame>();
		for (Page page : pages)
		{
			for (Frame frame : page.frames)
			{
				if (frame.isSelected())
				{
					frames.add(frame);
				}
			}
		}
		return frames;
	}*/


	/**
	 * Gets the axis-aligned bounding rectangle of the given system, specified
	 * by its {@link ScoreLayout}, the index of the frame and the index of the
	 * system (relative to the frame) in page coordinates together with the
	 * index of the page. If not found, null is returned.
	 */
	public Tuple2<Integer, Rectangle2f> getSystemBoundingRect(
		ScoreLayout scoreLayout, int frameIndex, int systemIndex)
	{
		//find the frame
		ScoreFrameChain chain = scoreLayouts.getKeyByValue(scoreLayout);
		ScoreFrame scoreFrame = chain.frames.get(frameIndex);

		//get system boundaries in mm
		ScoreFrameLayout scoreFrameLayout = scoreLayout.frames.get(frameIndex);
		Rectangle2f rectMm = scoreFrameLayout.getSystemBoundaries(systemIndex);
		if (rectMm == null)
			return null;
		
		//compute corner points (because frame may be rotated) and transform them
		float x = rectMm.position.x - scoreFrame.getSize().width / 2;
		float y = rectMm.position.y - scoreFrame.getSize().height / 2;
		float w = rectMm.size.width;
		float h = rectMm.size.height;
		Point2f nw = scoreFrame.computePagePosition(new Point2f(x, y), this);
		Point2f ne = scoreFrame.computePagePosition(new Point2f(x + w, y), this);
		Point2f se = scoreFrame.computePagePosition(new Point2f(x + w, y + h), this);
		Point2f sw = scoreFrame.computePagePosition(new Point2f(x, y + h), this);
		
		// compute axis-aligned bounding box and return it
		Rectangle2f ret = new Rectangle2f(nw.x, nw.y, 0, 0);
		ret = ret.extend(ne);
		ret = ret.extend(se);
		ret = ret.extend(sw);
		
		int pageIndex = pages.indexOf(getPage(scoreFrame));
		return new Tuple2<Integer, Rectangle2f>(pageIndex, ret);
	}
	
	
	/**
	 * Gets the {@link Score} that belongs to the given {@link ScoreFrame},
	 * or null if there is none.
	 */
	public Score getScore(ScoreFrame scoreFrame)
	{
		for (Score score : scoreFrameChains.keySet())
		{
			if (scoreFrameChains.get(score).frames.contains(scoreFrame))
				return score;
		}
		return null;
	}
	
	
	/**
	 * Gets the musical layout of the given score frame which
	 * shows the given score, or null if it is not available.
	 */
	public ScoreFrameLayout getScoreFrameLayout(ScoreFrame scoreFrame)
	{
		ScoreFrameChain chain = scoreFrameChains.get(getScore(scoreFrame));
		if (chain == null)
			throw new IllegalArgumentException("Unknown score frame");
		ScoreLayout scoreLayout = scoreLayouts.get(chain);
		if (scoreLayout != null)
		{
			int frameIndex = chain.frames.indexOf(scoreFrame);
			if (frameIndex >= 0 && frameIndex < scoreLayout.frames.size())
				return scoreLayout.frames.get(frameIndex);
		}
		return null;
	}
	
	
	/**
	 * Gets the ScoreFrame which contains the given measure within
	 * the given score. If it can not be found, null is returned.
	 */
	@Untested public ScoreFrame getScoreFrame(Score score, int measure)
	{
		ScoreFrameChain chain = scoreFrameChains.get(score);
		if (chain == null)
			throw new IllegalArgumentException("Unknown score");
		ScoreLayout scoreLayout = scoreLayouts.get(chain);
		if (scoreLayout != null)
		{
			int frameIndex = scoreLayout.getFrameIndexOf(measure);
			if (frameIndex >= 0 && frameIndex < scoreLayout.frames.size())
				return chain.frames.get(frameIndex);
		}
		return null;
	}
	
	
	/**
	 * Returns true, if the given {@link ScoreFrame} is the first one in its
	 * score frame chain.
	 */
	public boolean isLeadingScoreFrame(ScoreFrame scoreFrame)
	{
		for (Score score : scoreFrameChains.keySet())
		{
			if (scoreFrameChains.get(score).frames.get(0) == scoreFrame)
				return true;
		}
		return false;
	}

	
	/**
	 * Gets the parent {@link GroupFrame} of the given frame, or null, if there is none.
	 */
	public GroupFrame getParentGroupFrame(Frame frame)
	{
		if (parentFrames == null)
			updateCache();
		return parentFrames.get(frame);
	}
	
	
	/**
	 * Gets the parent {@link Page} of the given frame, or null, if it is not
	 * part of this layout.
	 */
	public Page getPage(Frame frame)
	{
		if (parentPages == null)
			updateCache();
		Page page = parentPages.get(frame);
		if (page != null)
			return page;
		GroupFrame parent = getParentGroupFrame(frame);
		if (parent != null)
			return getPage(parent);
		return null;
	}
	
	
	/**
	 * Adds the given {@link Frame} to the page with the given index.
	 */
	public Layout plusFrame(Frame frame, int pageIndex)
	{
		Page page = pages.get(pageIndex).plusFrame(frame);
		return new Layout(defaults, pages.with(pageIndex, page), scoreFrameChains, scoreLayouts,
			selectedFrames);
	}
	
	
	/**
	 * Adds the given {@link Frame} as a child to the given parent {@link GroupFrame}.
	 * If it was placed elsewhere, it is removed there.
	 * @param recomputeScoreLayout  true, if the score layout should be recomputed
	 *                              if necessary, otherwise false
	 */
	public Layout plusChildFrame(Frame child, GroupFrame parent, boolean recomputeScoreLayout)
	{
		//first remove it
		Layout ret = this.removeFrame(child, false);
		//now add it
		GroupFrame groupFrame = parent.plusChildFrame(child);
		ret = ret.replaceFrame(parent, groupFrame, recomputeScoreLayout);
		return ret;
	}
	
	
	/**
	 * Removes the given {@link Frame}.
	 */
	public Layout removeFrame(Frame frame, boolean recomputeScoreLayout)
	{
		return replaceFrame(frame, null, recomputeScoreLayout);
	}
	
	
	/**
	 * Replaces the given old page with the given new one.
	 */
	private Layout replacePage(Page oldPage, Page newPage)
	{
		PVector<Page> pages = this.pages.replace(oldPage, newPage);
		return new Layout(defaults, pages, scoreFrameChains, scoreLayouts, selectedFrames);
	}
	
	
	/**
	 * Replaces the given old frame with the given new one (or null to delete it).
	 * @param oldFrame              the frame to replace
	 * @param newFrame              the frame to insert
	 * @param recomputeScoreLayout  true, if the score layout should be recomputed
	 *                              if necessary, otherwise false
	 */
	public Layout replaceFrame(Frame oldFrame, Frame newFrame, boolean recomputeScoreLayout)
	{
		Layout layout = this;
		//easy case: frame on page
		if (parentPages == null)
			updateCache();
		Page oldPage = parentPages.get(oldFrame);
		if (oldPage != null)
		{
			Page newPage = oldPage.replaceFrame(oldFrame, newFrame);
			layout = layout.replacePage(oldPage, newPage);
		}
		//recursive case: frame within group frame
		GroupFrame oldGroupFrame = parentFrames.get(oldFrame);
		if (oldGroupFrame != null)
		{
			GroupFrame newGroupFrame = oldGroupFrame.replaceChildFrame(oldFrame, newFrame);
			layout = layout.replaceFrame(oldGroupFrame, newGroupFrame, recomputeScoreLayout);
		}
		if (layout == null)
			throw new IllegalArgumentException("Unknown old frame");
		//replace in score frame chains
		PMap<Score, ScoreFrameChain> scoreFrameChains = this.scoreFrameChains;
		PMap<ScoreFrameChain, ScoreLayout> scoreLayouts = this.scoreLayouts;
		Score scoreToRecompute = null;
		if (oldFrame instanceof ScoreFrame)
		{
			ScoreFrame oldScoreFrame = (ScoreFrame) oldFrame;
			ScoreFrame newScoreFrame = (ScoreFrame) newFrame;
			for (Score score : scoreFrameChains.keySet())
			{
				ScoreFrameChain oldChain = scoreFrameChains.get(score);
				ScoreFrameChain newChain = oldChain.replaceFrame(oldScoreFrame, newScoreFrame);
				if (newChain == null)
				{
					//score frame chain is empty now. remove it.
					scoreFrameChains = scoreFrameChains.minus(score);
					scoreLayouts = scoreLayouts.minus(oldChain);
				}
				else if (oldChain != newChain)
				{
					scoreFrameChains = scoreFrameChains.plus(score, newChain);
					//if the new frame has the same size as the old frame, the
					//musical layout can be reused. otherwise, it has to be recomputed (if requested)
					ScoreLayout oldScoreLayout = scoreLayouts.get(oldChain);
					ScoreLayout newScoreLayout = oldScoreLayout;
					if (recomputeScoreLayout && !oldFrame.data.size.equals(newFrame.data.size))
					{
						newScoreLayout = null;
						scoreToRecompute = score;
					}
					scoreLayouts = scoreLayouts.minus(oldChain).plus(newChain, newScoreLayout);
				}
			}
		}
		//replace in selected frames
		PVector<Frame> selectedFrames = this.selectedFrames.replaceOrMinus(oldFrame, newFrame);
		Layout ret = new Layout(layout.defaults, layout.pages, scoreFrameChains, scoreLayouts, selectedFrames);
		//recompute score layout, if needed
		if (scoreToRecompute != null)
		{
			ret = ret.updateScoreLayouts(scoreToRecompute, scoreToRecompute);
		}
		return ret;
	}
	
	
	/**
	 * Adds a {@link Score} to this layout, shown in the given {@link ScoreFrameChain}.
	 */
	public Layout plusScore(Score score, ScoreFrameChain chain)
	{
		Layout layout = new Layout(defaults, pages, scoreFrameChains.plus(score, chain),
			scoreLayouts, selectedFrames);
		return layout.updateScoreLayouts(score, score);
	}
	
	
	void updateCache()
	{
		//parent pages and group frames
		parentPages = new HashMap<Frame, Page>();
		parentFrames = new HashMap<Frame, GroupFrame>();
		for (Page page : pages)
		{
			for (Frame frame : page.frames)
			{
				parentPages.put(frame, page);
				if (frame instanceof GroupFrame)
				{
					addGroupFrameToCache((GroupFrame) frame);
				}
			}
		}
	}
	
	
	private void addGroupFrameToCache(GroupFrame groupFrame)
	{
		for (Frame frame : groupFrame.children)
		{
			parentFrames.put(frame, groupFrame);
			if (frame instanceof GroupFrame)
			{
				addGroupFrameToCache((GroupFrame) frame);
			}
		}
	}
	
	
	/**
	 * Updates the {@link ScoreLayout}s belonging to the given old {@link Score}
	 * using the given new {@link Score} and returns the updated {@link Layout}.
	 */
	public Layout updateScoreLayouts(Score oldScore, Score newScore)
	{
		Layout layout = this;
		ScoreFrameChain chain = scoreFrameChains.get(oldScore);
		ScoreLayout oldScoreLayout = scoreLayouts.get(chain);
		
		//select symbol pool and layout settings
		SymbolPool symbolPool = oldScoreLayout != null ?
			oldScoreLayout.symbolPool : defaults.getSymbolPool();
		LayoutSettings layoutSettings = oldScoreLayout != null ?
			oldScoreLayout.layoutSettings : defaults.getLayoutSettings();

		if (chain != null)
		{
			PVector<ScoreLayoutArea> areas = pvec();
			for (ScoreFrame scoreFrame : chain.frames)
			{
				areas = areas.plus(area(scoreFrame.data.size, scoreFrame.hFill, scoreFrame.vFill));
			}
			ScoreLayout scoreLayout = new ScoreLayouter(newScore, symbolPool, layoutSettings, false,
				areas, areas.getLast()).createLayout();
			
			//create updated layout
			PMap<ScoreFrameChain, ScoreLayout> scoreLayouts = this.scoreLayouts.plus(chain, scoreLayout);
			PMap<Score, ScoreFrameChain> scoreFrameChains =
				this.scoreFrameChains.minus(oldScore).plus(newScore, chain);
			return new Layout(defaults, pages, scoreFrameChains, scoreLayouts, selectedFrames);
		}
		return layout;
	}
	
	
	public Layout withDefaults(LayoutDefaults defaults)
	{
		return new Layout(defaults, pages, scoreFrameChains, scoreLayouts, selectedFrames);
	}
	
	
	/**
	 * Sets all pages to the given format.
	 * The defaults values are not changed.
	 */
	public Layout withLayoutFormat(LayoutFormat layoutFormat)
	{
		PVector<Page> pages = this.pages;
		for (int i : range(pages))
		{
			pages = pages.with(i, pages.get(i).withFormat(layoutFormat.getPageFormat(i)));
		}
		return new Layout(defaults, pages, scoreFrameChains, scoreLayouts, selectedFrames);
	}
	
	
	
	public Layout withSelectedFrames(PVector<Frame> selectedFrames)
	{
		return new Layout(defaults, pages, scoreFrameChains, scoreLayouts, selectedFrames);
	}
	
	
	public Layout chainUpScoreFrame(ScoreFrame frameFrom, ScoreFrame frameTo)
	{
		if (frameFrom == frameTo)
			throw new IllegalArgumentException("Same frames");
		PMap<Score, ScoreFrameChain> scoreFrameChains = this.scoreFrameChains;
		//score layouts have to be recomputed
		PMap<ScoreFrameChain, ScoreLayout> scoreLayouts = this.scoreLayouts;
		
		//remove target frame from other chain, if there is one
		Score toOldScore = getScore(frameTo);
		if (toOldScore != null)
		{
			ScoreFrameChain toOldChain = scoreFrameChains.get(toOldScore);
			scoreLayouts = scoreLayouts.minus(toOldChain);
			toOldChain = toOldChain.replaceFrame(frameTo, null);
			if (toOldChain != null)
				scoreFrameChains = scoreFrameChains.plus(toOldScore, toOldChain);
			else
				scoreFrameChains = scoreFrameChains.minus(toOldScore);
		}
		
		//add target frame to the chain of the the source frame
		Score fromScore = getScore(frameFrom);
		ScoreFrameChain fromChain = scoreFrameChains.get(fromScore);
		scoreLayouts = scoreLayouts.minus(fromChain);
		fromChain = fromChain.plusFrame(frameFrom, frameTo);
		scoreFrameChains = scoreFrameChains.plus(fromScore, fromChain);
		
		return new Layout(defaults, pages, scoreFrameChains, scoreLayouts, selectedFrames);
	}
	
	
	/**
	 * Returns the {@link LP} of the given {@link BMP} within the given {@link Score}
	 * at the given line position, or null if unknown.
	 */
	public LP computeLP(Score score, BMP bmp, float lp)
	{
		ScoreFrameChain chain = scoreFrameChains.get(score);
		if (chain != null)
		{
			ScoreLayout sl = scoreLayouts.get(chain);
			ScoreLP slp = sl.computeScoreLP(bmp, lp);
			if (slp != null)
			{
				ScoreFrame frame = chain.frames.get(slp.frameIndex);
				Page page = getPage(frame);
				if (page != null)
				{
					Point2f frameP = slp.pMm.sub(frame.getSize().width/2, frame.getSize().height/2);
					int pageIndex = pages.indexOf(page);
					Point2f pMm = frame.computePagePosition(frameP, this);
					return lp(this, pageIndex, pMm);
				}
			}
		}
		return null;
	}
	
}
