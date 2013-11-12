package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea.area;
import static org.junit.Assert.fail;

import java.io.FileInputStream;

import org.junit.Test;

import com.xenoage.utils.io.TestIO;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInput;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;
import com.xenoage.zong.io.symbols.AWTSVGPathReader;
import com.xenoage.zong.musiclayout.layouter.notation.AccidentalsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.ArticulationsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotesAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemDirectionStrategy;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.SymbolPoolUtils;


/**
 * Test cases for the {@link ScoreLayouter} class.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayouterTest
{
	
	
	/**
   * Try to layout all official MusicXML 1.1 and 2.0 sample files.
   * We can not test for the correct layout of course, but at least
   * we want to have no exceptions.
   */
  @Test public void testSampleFiles()
  {
  	TestIO.initWithSharedDir();
  	SymbolPoolUtils.init(new AWTSVGPathReader());
  	SymbolPool symbolPool = new SymbolPool();
  	LayoutSettings layoutSettings = LayoutSettings.loadDefault();
  	for (String file : MusicXMLScoreFileInputTest.getSampleFiles())
    {
    	try
    	{
    		Score score = new MusicXMLScoreFileInput().read(new FileInputStream(file), file);
    		Size2f areaSize = new Size2f(150, 10000);
    		new ScoreLayouter(score, symbolPool, layoutSettings, false,
    			pvec(area(areaSize)), area(areaSize)).createLayoutWithExceptions();
    	}
      catch (Exception ex)
      {
      	ex.printStackTrace();
        fail("Failed to layout file: " + file);
      }
    }
  }
	
	
	public static NotationStrategy getNotationStrategy()
	{
		return new NotationStrategy(
			new StemDirectionStrategy(),
			new NotesAlignmentStrategy(),
			new AccidentalsAlignmentStrategy(),
			new StemAlignmentStrategy(),
			new ArticulationsAlignmentStrategy());
	}

}
