package com.xenoage.zong.musiclayout.layouter;

import com.xenoage.zong.musiclayout.layouter.notation.AccidentalsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.ArticulationsAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.NotesAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemAlignmentStrategy;
import com.xenoage.zong.musiclayout.layouter.notation.StemDirectionStrategy;

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
	/* GOON
	@Test public void testSampleFiles() {
		TestIO.initWithSharedDir();
		SymbolPoolUtils.init(new AWTSVGPathReader());
		SymbolPool symbolPool = new SymbolPool();
		LayoutSettings layoutSettings = LayoutSettings.loadDefault();
		for (String file : MusicXMLScoreFileInputTest.getSampleFiles()) {
			try {
				Score score = new MusicXMLScoreFileInput().read(new FileInputStream(file), file);
				Size2f areaSize = new Size2f(150, 10000);
				new ScoreLayouter(score, symbolPool, layoutSettings, false, pvec(area(areaSize)),
					area(areaSize)).createLayoutWithExceptions();
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Failed to layout file: " + file);
			}
		}
	} */

	public static NotationStrategy getNotationStrategy() {
		return new NotationStrategy(new StemDirectionStrategy(), new NotesAlignmentStrategy(),
			new AccidentalsAlignmentStrategy(), new StemAlignmentStrategy(),
			new ArticulationsAlignmentStrategy());
	}

}
