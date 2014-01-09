package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.desktop.io.symbols.SymbolPoolReader;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInput;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;
import com.xenoage.zong.musiclayout.layouter.notation.AccidentalsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.ArticulationsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotesAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemDirectionStrategy;
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
	@Test public void testSampleFiles() {
		//SymbolPoolUtils.init(new AWTSVGPathReader());
		SymbolPool symbolPool = SymbolPoolReader.readSymbolPool("default");
		LayoutSettings layoutSettings = LayoutSettingsReader.load("data/test/layout/LayoutSettingsTest.xml");
		for (String file : MusicXMLScoreFileInputTest.getSampleFiles()) {
			try {
				//System.out.println(file);
				Score score = new MusicXMLScoreFileInput().read(platformUtils().openFile(file), file);
				Size2f areaSize = new Size2f(150, 10000);
				new ScoreLayouter(score, symbolPool, layoutSettings, false, areaSize).createLayoutWithExceptions();
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Failed to layout file: " + file);
			}
		}
	}

	public static NotationStrategy getNotationStrategy() {
		return new NotationStrategy(new StemDirectionStrategy(), new NotesAlignmentStrategy(),
			new AccidentalsAlignmentStrategy(), new StemAlignmentStrategy(),
			new ArticulationsAlignmentStrategy());
	}

}
