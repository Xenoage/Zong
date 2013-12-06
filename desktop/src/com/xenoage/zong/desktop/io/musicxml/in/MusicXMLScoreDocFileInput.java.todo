package com.xenoage.zong.desktop.io.musicxml.in;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.xenoage.utils.base.exceptions.InvalidFormatException;
import com.xenoage.utils.base.filter.AllFilter;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.ScoreDocFileInput;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.FrameData;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.symbols.SymbolPoolUtils;


/**
 * This class reads a MusicXML 2.0 file
 * into a instance of the {@link ScoreDoc} class.
 *
 * @author Andreas Wenger
 */
public class MusicXMLScoreDocFileInput
  implements ScoreDocFileInput
{
  
  
  /**
   * Creates a {@link ScoreDoc} instance from the document
   * at the given path.
   * 
   * The filepath must be given, when the opened file is an opus document,
   * otherwise null is returned.
   * 
   * If none is opened, null is returned.
   */
  @Override public ScoreDoc read(InputStream in, String filePath)
    throws InvalidFormatException, IOException
  {
  	Score score;
  	
  	List<Score> scores = FileReader.loadScores(in, filePath, new AllFilter<String>());
		if (scores.size() > 0)
			score = scores.get(0);
		else
			return null;

   return read(score, filePath);
  }
  
  
  /**
   * Creates a {@link ScoreDoc} instance from the given score.
   */
  public ScoreDoc read(Score score, String filePath)
    throws InvalidFormatException, IOException
  {

    //create the document
		ScoreDoc ret = new ScoreDoc(score);
    Layout layout = ret.getLayout();
    
    //page format
    Object layoutFormat = score.metaData.get("layoutformat");
    if (layoutFormat != null && layoutFormat instanceof LayoutFormat)
    {
    	layout = layout.withDefaults(new LayoutDefaults(
    		(LayoutFormat) layoutFormat, null, layout.defaults.getLayoutSettings()));
    }
    
    //layout basics
    PageFormat pageFormat = layout.defaults.getFormat().getPageFormat(layout.pages.size());
    Size2f frameSize = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
    Point2f framePos = new Point2f(pageFormat.margins.left + frameSize.width/2,
    	pageFormat.margins.top + frameSize.height/2);
    
    //layout the score to find out the needed space
    ScoreLayouter layouter = new ScoreLayouter(score,
    	SymbolPoolUtils.getDefaultSymbolPool(), LayoutSettings.loadDefault(), true, frameSize);
    ScoreLayout scoreLayout = layouter.createLayout();
    
    //create and fill at least one page
    ScoreFrameChain chain = null;
    for (int i = 0; i < scoreLayout.frames.size(); i++)
    {
    	Page page = new Page(pageFormat);
    	ScoreFrame frame = new ScoreFrame(new FrameData(framePos, frameSize));
    	//TEST frame = frame.withHFill(NoHorizontalSystemFillingStrategy.getInstance());
    	page = page.plusFrame(frame);
    	layout = layout.plusPage(page);
    	if (chain == null)
    		chain = ScoreFrameChain.create(frame);
    	else
    		chain = chain.plusFrame(frame);
    }
    layout = layout.plusScore(score, chain);
    
    //add credit elements
    Object o = score.metaData.get("mxldoc");
    if (o != null && o instanceof MxlScorePartwise)
    {
    	MxlScorePartwise doc = (MxlScorePartwise) o;
    	layout = CreditsReader.read(doc, layout, score.format);
    }
    
    ret.setLayout(layout);
    return ret;
  }
  

}
