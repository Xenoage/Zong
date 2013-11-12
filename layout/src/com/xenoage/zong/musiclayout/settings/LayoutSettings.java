package com.xenoage.zong.musiclayout.settings;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.utils.xml.XMLReader.attributeNotNull;
import static com.xenoage.utils.xml.XMLReader.elementNotNull;
import static com.xenoage.utils.xml.XMLReader.root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.io.IO;
import com.xenoage.utils.xml.XMLReader;


/**
 * Settings for the musical layout.
 * 
 * One instance of this class is used throughout the
 * whole application, and is loaded on demand from
 * "data/layout/default.xml".
 * 
 * All values are in interline spaces, unless otherwise stated.
 * 
 * Some of the default values are adapted from
 * "Ross: The Art of Music Engraving", page 77.
 * 
 * @author Andreas Wenger
 */
public final class LayoutSettings
{
	
	//chord settings
	public final ChordWidths chordWidths;
	public final ChordWidths graceChordWidths;
	
	//spacings
	public final Spacings spacings;
	
	//scalings
	public final float scalingClefInner; //clef in the middle of a staff
	public final float scalingGrace;
	
	//offsets
	public final float offsetMeasureStart; //offset of the first element in a measure
	public final float offsetBeatsMinimal; //minimal offset between to different beats
	
	private static final String file = "data/layout/default.xml";
	
	
	public static LayoutSettings loadDefault()
	{
		return load(file);
	}
	
	
	public static LayoutSettings load(String file)
	{
		ChordWidths chordWidths, graceChordWidths;
		Spacings spacings;
		float scalingClefInner, scalingGrace;
		float offsetMeasureStart;
		float offsetBeatsMinimal;
		try
		{
			Document doc = XMLReader.readFile(IO.openInputStream(file));
			//load the chord layout settings
			Element eChordWidths = elementNotNull(root(doc), "chordwidths");
			chordWidths = ChordWidths.fromXML(eChordWidths);
			//load the space settings
			Element eSpacings = elementNotNull(root(doc), "spacings");
			spacings = Spacings.fromXML(eSpacings);
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
		}
		catch (Exception ex)
		{
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
		return new LayoutSettings(chordWidths, graceChordWidths, spacings,
			scalingClefInner, scalingGrace, offsetMeasureStart, offsetBeatsMinimal);
	}
	

	public LayoutSettings(ChordWidths chordWidths, ChordWidths graceChordWidths, Spacings spacings,
		float scalingClefInner, float scalingGrace, float offsetMeasureStart, float offsetBeatsMinimal)
	{
		this.chordWidths = chordWidths;
		this.graceChordWidths = graceChordWidths;
		this.spacings = spacings;
		this.scalingClefInner = scalingClefInner;
		this.scalingGrace = scalingGrace;
		this.offsetMeasureStart = offsetMeasureStart;
		this.offsetBeatsMinimal = offsetBeatsMinimal;
	}
	

}
