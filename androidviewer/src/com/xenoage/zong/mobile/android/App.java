package com.xenoage.zong.mobile.android;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;

import com.xenoage.utils.font.android.AndroidTextMeasurerFactory;
import com.xenoage.utils.graphics.font.FontUtils;
import com.xenoage.utils.io.AndroidIO;
import com.xenoage.utils.io.IO;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreDocFileInput;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInput;
import com.xenoage.zong.io.symbols.AndroidSVGPathReader;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.SymbolPoolUtils;


/**
 * General information about the app and
 * main functions.
 * 
 * @author Andreas Wenger
 */
public class App
{
	
	//naming "... for Android" according to http://developer.android.com/distribute/googleplay/promote/brand.html
	//notice: use "Android™" when it appears the first time, and add "Android is a trademark of Google Inc." on the about screen
	public static final String PROJECT_FIRST_NAME = "Viewer for Android";
	
	private static SymbolPool symbolPool;
	private static Bitmap symbolsBitmap;
	private static MidiPlayer midiPlayer;

	
	public static void init(Context context)
		throws IOException
	{
		//init IO and logging
		IO.initCustom(new AndroidIO(context.getResources()));
		Log.initNoLog();
		
		//init Android specific settings
		FontUtils.textMeasurerFactory = new AndroidTextMeasurerFactory();
		
		//load symbol pool
		SymbolPoolUtils.init(new AndroidSVGPathReader());
		symbolPool = new SymbolPool();
		SymbolPoolUtils.setDefaultSymbolPool(symbolPool);
		
		//midi player
		midiPlayer = new MidiPlayer();
	}

	
	public static SymbolPool getSymbolPool()
	{
		return symbolPool;
	}

	
	public static Bitmap getSymbolsBitmap()
	{
		return symbolsBitmap;
	}
	
	
	public static MidiPlayer getMidiPlayer()
	{
		return midiPlayer;
	}
	
	
	/**
	 * Loads the {@link ScoreDoc} with the given filename.
	 */
	public static ScoreDoc load(String filename)
		throws IOException
	{
		String filepath = "files/" + filename;
		InputStream in = IO.openInputStream(filepath);
		Score score = new MusicXMLScoreFileInput().read(in, filepath);
		return new MusicXMLScoreDocFileInput().read(score, filepath);
	}
	

}
