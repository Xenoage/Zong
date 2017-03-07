package com.xenoage.zong.android;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.android.AndroidPlatformUtils.io;
import static com.xenoage.utils.jse.promise.Sync.sync;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

import com.xenoage.utils.android.AndroidPlatformUtils;
import com.xenoage.utils.android.log.AndroidLogProcessing;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.async.Sync;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Zong;
import com.xenoage.zong.android.util.AndroidZongPlatformUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreDocFileReader;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInput;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.util.ZongPlatformUtils;

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
		AndroidZongPlatformUtils.init(context.getResources());
		Log.init(new AndroidLogProcessing(Zong.getNameAndVersion(projectFirstName)));

		//load symbol pool
		symbolPool = zongPlatformUtils().getSymbolPool();

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
		InputStream in = io().openFile(filepath);
		try {
			return sync(new MusicXmlScoreDocFileReader(in, filepath).read());
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

}
