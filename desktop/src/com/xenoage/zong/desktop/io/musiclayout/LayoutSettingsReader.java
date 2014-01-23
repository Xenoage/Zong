package com.xenoage.zong.desktop.io.musiclayout;

import static com.xenoage.utils.jse.xml.XMLReader.attributeNotNull;
import static com.xenoage.utils.jse.xml.XMLReader.elementNotNull;
import static com.xenoage.utils.jse.xml.XMLReader.readFile;
import static com.xenoage.utils.jse.xml.XMLReader.root;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.zong.desktop.io.musiclayout.ChordWidthsReader.readChordWidths;
import static com.xenoage.zong.desktop.io.musiclayout.SpacingsReader.readSpacings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLReader;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.settings.Spacings;

/**
 * Desktop reader for {@link LayoutSettings}. 
 * 
 * @author Andreas Wenger
 */
public final class LayoutSettingsReader {

	//private static final String file = "data/musiclayout/default.xml";


	/**
	 * Reads the {@link LayoutSettings} from the given file.
	 */
	public static LayoutSettings load(String file) {
		ChordWidths chordWidths, graceChordWidths;
		Spacings spacings;
		float scalingClefInner, scalingGrace;
		float offsetMeasureStart;
		float offsetBeatsMinimal;
		try {
			Document doc = readFile(desktopIO().findFile(file).getAbsolutePath());
			//load the chord layout settings
			Element eChordWidths = elementNotNull(root(doc), "chordwidths");
			chordWidths = readChordWidths(eChordWidths);
			//load the space settings
			Element eSpacings = elementNotNull(root(doc), "spacings");
			spacings = readSpacings(eSpacings);
			//load scalings
			Element eScaling = elementNotNull(XMLReader.root(doc), "scaling");
			Element e = elementNotNull(eScaling, "clef");
			scalingClefInner = Float.parseFloat(attributeNotNull(e, "inner"));
			e = elementNotNull(eScaling, "grace");
			scalingGrace = Float.parseFloat(attributeNotNull(e, "scaling"));
			//offsets
			Element eOffset = elementNotNull(XMLReader.root(doc), "offset");
			e = elementNotNull(eOffset, "measure");
			offsetMeasureStart = Float.parseFloat(attributeNotNull(e, "start"));
			e = elementNotNull(eOffset, "beats");
			offsetBeatsMinimal = Float.parseFloat(attributeNotNull(e, "minimal"));
		} catch (Exception ex) {
			log(error("Could not read the file \"" + file + "\":", ex));
			throw new RuntimeException(ex);
			/*
			//default values
			durationWidths.put(fr(1, 32), 1 + 1/2f);
			durationWidths.put(fr(1, 16), 1 + 3/4f);
			durationWidths.put(fr(1, 8), 2 + 1/2f);
			durationWidths.put(fr(1, 2), 4 + 3/4f);
			widthClef = 4;
			widthSharp = 1.2f;
			widthFlat = 1f;
			widthMeasureEmpty = 8f;
			scalingClefInner = 0.75f;
			offsetMeasureStart = 1;
			offsetBeatsMinimal = 1.5f;
			*/
		}
		//compute grace chord widths
		graceChordWidths = chordWidths.scale(scalingGrace);
		return new LayoutSettings(chordWidths, graceChordWidths, spacings, scalingClefInner,
			scalingGrace, offsetMeasureStart, offsetBeatsMinimal);
	}

}
