package com.xenoage.utils.jse.font;

import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.utils.jse.io.DesktopIO;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.xenoage.utils.io.FileFilters.ttfFilter;
import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

/**
 * Useful methods to work with fonts.
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class FontUtils {

	private static FontUtils instance = null;

	public static AwtTextMeasurer textMeasurer = new AwtTextMeasurer();

	private SortedList<String> supportedFontFamiliesSorted;
	private HashSet<String> supportedFontFamilies;


	public static FontUtils getInstance() {
		if (instance == null)
			instance = new FontUtils();
		return instance;
	}

	private FontUtils() {
		//sorted array for the GUI
		String[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getAvailableFontFamilyNames();
		Arrays.sort(systemFonts);
		//add to sorted list
		supportedFontFamiliesSorted = new SortedList<>(systemFonts, false);
		//hash set for fast queries
		supportedFontFamilies = new HashSet<>(supportedFontFamiliesSorted.getSize() * 2);
		for (String s : systemFonts) {
			supportedFontFamilies.add(s);
		}
	}

	/**
	 * Loads the shipped fonts in "data/fonts".
	 * If an exception occurs during loading, it is thrown.
	 */
	public void loadShippedFonts()
		throws Exception {
		String fontPath = "data/fonts";
		List<String> ttfFiles = io().listFiles(fontPath, ttfFilter);
		for (String file : ttfFiles) {
			Font font = Font.createFont(Font.TRUETYPE_FONT, io().openFile(fontPath + "/" + file));
			String fontName = font.getFamily();
			if (!isFontFamilySupported(fontName)) {
				log(remark("Registering font: " + fontName));
				supportedFontFamiliesSorted.add(fontName);
				supportedFontFamilies.add(fontName);
				GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			}
		}
	}

	/**
	 * Gets an alphabetically sorted list of all supported font families.
	 */
	public SortedList<String> getSupportedFontFamilies() {
		return supportedFontFamiliesSorted;
	}

	/**
	 * Returns, if the given font family is supported by this system.
	 */
	public boolean isFontFamilySupported(String fontFamily) {
		return supportedFontFamilies.contains(fontFamily);
	}

	public static FontStyle getFontStyle(Font font) {
		int style = 0;
		if (font.isBold())
			style |= FontStyle.Bold;
		if (font.isItalic())
			style |= FontStyle.Italic;
		return new FontStyle(style);
	}

}
