package com.xenoage.zong.test;

import static com.xenoage.zong.core.format.LayoutFormat.defaultLayoutFormat;
import static com.xenoage.zong.musiclayout.settings.LayoutSettings.defaultLayoutSettings;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;

import java.io.IOException;

import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.demos.simplegui.SimpleGuiDemo;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.ScoreDocFactory;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreDocFileReader;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Simple viewer for showing test scores.
 * Based on the SimpleGUI demo.
 * 
 * @author Andreas Wenger
 */
public class VisualTester {
	
	public static void start(VisualTest test) {
		try {
			JseZongPlatformUtils.init(test.getClass().getSimpleName());
			Log.init(new DesktopLogProcessing(test.getClass().getSimpleName()));
			ScoreDoc scoreDoc = createScoreDoc(test);
			SimpleGuiDemo.start(scoreDoc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static ScoreDoc createScoreDoc(VisualTest test)
		throws IOException {
		return new ScoreDocFactory().read(test.getScore());
	}
	
}
