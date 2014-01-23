package com.xenoage.zong.android;

import static com.xenoage.utils.PlatformUtils.platformUtils;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

import com.xenoage.utils.android.AndroidPlatformUtils;
import com.xenoage.utils.android.log.AndroidLogProcessing;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * General information about the app and
 * main functions.
 * 
 * @author Andreas Wenger
 */
public class App {

	//naming "... for Android" according to http://developer.android.com/distribute/googleplay/promote/brand.html
	//notice: use "Android™" when it appears the first time, and add "Android is a trademark of Google Inc." on the about screen
	public static final String projectFirstName = "Viewer for Android™";

	private static SymbolPool symbolPool;
	private static Bitmap symbolsBitmap;
	private static MidiPlayer midiPlayer;


	public static void init(Context context)
		throws IOException {
		
		//init platform utils and logging
		AndroidPlatformUtils.init(context.getResources());
		Log.init(new AndroidLogProcessing(Zong.getNameAndVersion(projectFirstName)));

		//load symbol pool
		//SymbolPoolUtils.init(new AndroidSvgPathReader());
		//symbolPool = new SymbolPool();
		//symbolPoolUtils.setDefaultSymbolPool(symbolPool);

		//midi player
		midiPlayer = new MidiPlayer();
	}

	public static SymbolPool getSymbolPool() {
		return symbolPool;
	}

	public static Bitmap getSymbolsBitmap() {
		return symbolsBitmap;
	}

	public static MidiPlayer getMidiPlayer() {
		return midiPlayer;
	}

	/**
	 * Loads the {@link ScoreDoc} with the given filename.
	 */
	public static ScoreDoc load(String filename)
		throws IOException {
		String filepath = "files/" + filename;
		InputStream in = platformUtils().openFile(filepath);
		Score score = new MusicXmlScoreFileInput().read(in, filepath);
		return new MusicXmlScoreDocFileInput().read(score, filepath);
	}

}
