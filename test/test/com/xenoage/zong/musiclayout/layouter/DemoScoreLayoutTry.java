package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.symbols.SymbolPoolReader;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.util.demo.ScoreRevolutionary;

/**
 * Outputs the layout of the {@link ScoreRevolutionary} demo score
 * as a string.
 * 
 * @author Andreas Wenger
 */
public class DemoScoreLayoutTry {
	
	public static void main(String... args)
		throws IOException {
		SymbolPool symbolPool = SymbolPoolReader.readSymbolPool("default");
		LayoutSettings layoutSettings = LayoutSettingsReader.load("data/test/layout/LayoutSettingsTest.xml");
		
		try {
			Score score = ScoreRevolutionary.createScore();
			Size2f areaSize = new Size2f(150, 10000);
			ScoreLayout layout = new ScoreLayouter(score, symbolPool, layoutSettings, true, areaSize).createLayoutWithExceptions();
			System.out.println(layout.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
