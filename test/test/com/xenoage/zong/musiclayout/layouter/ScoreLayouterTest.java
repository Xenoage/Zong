package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.jse.async.Sync.sync;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInputTest;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;
import com.xenoage.zong.io.symbols.SymbolPoolReader;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Test cases for the {@link ScoreLayouter} class.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayouterTest {

	/**
	 * Try to layout all official MusicXML 1.1 and 2.0 sample files.
	 * We can not test for the correct layout of course, but at least
	 * we want to have no exceptions. 
	 */
	@Test public void testSampleFiles()
		throws Exception {
		SymbolPool symbolPool = sync(new SymbolPoolReader("default"));
		LayoutSettings layoutSettings = LayoutSettingsReader.read(
			jsePlatformUtils().openFile("data/test/layout/LayoutSettingsTest.xml"));
		for (String file : MusicXmlScoreFileInputTest.getSampleFiles()) {
			try {
				//System.out.println(file);
				Score score = new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(file), file);
				Size2f areaSize = new Size2f(150, 10000);
				Context context = new Context(score, symbolPool, layoutSettings);
				Target target = Target.completeLayoutTarget(new ScoreLayoutArea(areaSize));
				ScoreLayouter layouter = new ScoreLayouter(context, target);
				layouter.createLayoutWithExceptions();
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Failed to layout file: " + file);
			}
		}
	}

}
