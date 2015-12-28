package com.xenoage.zong.musiclayout.layouter;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.jse.async.Sync.sync;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.symbols.SymbolPoolReader;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.Target;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.utils.demo.ScoreRevolutionary;

/**
 * Outputs the layout of the {@link ScoreRevolutionary} demo score
 * as a string.
 * 
 * @author Andreas Wenger
 */
public class DemoScoreLayoutTry {
	
	public static void main(String... args)
		throws Exception {
		SymbolPool symbolPool = sync(new SymbolPoolReader("default"));
		LayoutSettings layoutSettings = LayoutSettingsReader.read(
			jsePlatformUtils().openFile("data/test/layout/LayoutSettingsTest.xml"));
		
		try {
			Score score = ScoreRevolutionary.createScore();
			Size2f areaSize = new Size2f(150, 10000);
			Context context = new Context(score, symbolPool, layoutSettings);
			Target target = Target.completeLayoutTarget(new ScoreLayoutArea(areaSize));
			ScoreLayout layout = new ScoreLayouter(context, target).createLayoutWithExceptions();
			System.out.println(layout.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
