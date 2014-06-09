package com.xenoage.zong.io.musiclayout;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.zong.io.musiclayout.ChordWidthsReader.readChordWidths;
import static com.xenoage.zong.io.musiclayout.SpacingsReader.readSpacings;
import static java.lang.Float.parseFloat;

import java.io.IOException;

import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.settings.Spacings;

/**
 * Reader for {@link LayoutSettings}. 
 * 
 * @author Andreas Wenger
 */
public final class LayoutSettingsReader {

	//private static final String file = "data/musiclayout/default.xml";

	/**
	 * Reads the {@link LayoutSettings} from the given input stream.
	 */
	public static LayoutSettings read(InputStream inputStream)
		throws IOException {
		ChordWidths chordWidths = null, graceChordWidths;
		Spacings spacings = null;
		float scalingClefInner = 0, scalingGrace = 0;
		float offsetMeasureStart = 0;
		float offsetBeatsMinimal = 0;
		try {
			XmlReader r = platformUtils().createXmlReader(inputStream);
			r.openNextChildElement();
			while (r.openNextChildElement()) {
				String n = r.getElementName();
				if (n.equals("chordwidths")) {
					//load the chord layout settings
					chordWidths = readChordWidths(r);
				}
				else if (n.equals("spacings")) {
					//load the space settings
					spacings = readSpacings(r);
				}
				else if (n.equals("scaling")) {
					//load scalings
					while (r.openNextChildElement()) {
						String n2 = r.getElementName();
						if (n2.equals("clef"))
							scalingClefInner = parseFloat(r.getAttributeNotNull("inner"));
						else if (n2.equals("grace"))
							scalingGrace = parseFloat(r.getAttributeNotNull("scaling"));
						r.closeElement();
					}
				}
				else if (n.equals("offset")) {
					//load offsets
					while (r.openNextChildElement()) {
						String n2 = r.getElementName();
						if (n2.equals("measure"))
							offsetMeasureStart = parseFloat(r.getAttributeNotNull("start"));
						else if (n2.equals("beats"))
							offsetBeatsMinimal = parseFloat(r.getAttributeNotNull("minimal"));
						r.closeElement();
					}
				}
				r.closeElement();
			}
			r.close();
		} catch (Exception ex) {
			log(error("Could not read the input stream", ex));
			throw new IOException(ex);
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
