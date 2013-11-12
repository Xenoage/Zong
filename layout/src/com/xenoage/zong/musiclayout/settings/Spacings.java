package com.xenoage.zong.musiclayout.settings;

import static com.xenoage.utils.xml.XMLReader.attributeNotNull;
import static com.xenoage.utils.xml.XMLReader.elementNotNull;

import java.io.IOException;

import org.w3c.dom.Element;

/**
 * Settings for spacings (distances) in IS.
 * 
 * @author Andreas Wenger
 */
public class Spacings
{
	
	//chord spacings
	public final ChordSpacings normalChordSpacings;
	public final ChordSpacings graceChordSpacings;
	
	//distances
	public final float widthSharp, widthFlat, widthClef, widthMeasureEmpty, widthDistanceMin;

	
	public static Spacings fromXML(Element eSpacings)
		throws IOException
	{
		ChordSpacings normalChordSpacings, graceChordSpacings;
		float widthSharp, widthFlat, widthClef, widthMeasureEmpty, widthDistanceMin;
		
		Element eChords = elementNotNull(eSpacings, "chords");
		normalChordSpacings = ChordSpacings.fromXML(elementNotNull(eChords, "normal"));
		graceChordSpacings = ChordSpacings.fromXML(elementNotNull(eChords, "grace"));

		Element e = elementNotNull(eSpacings, "clef");
		widthClef = Float.parseFloat(attributeNotNull(e, "width"));
		Element eKey = elementNotNull(eSpacings, "key");
		e = elementNotNull(eKey, "sharp");
		widthSharp = Float.parseFloat(attributeNotNull(e, "width"));
		e = elementNotNull(eKey, "flat");
		widthFlat = Float.parseFloat(attributeNotNull(e, "width"));
		e = elementNotNull(eSpacings, "measure");
		widthMeasureEmpty = Float.parseFloat(attributeNotNull(e, "empty"));
		e = elementNotNull(eSpacings, "distance");
		widthDistanceMin = Float.parseFloat(attributeNotNull(e, "minimal"));
		
		return new Spacings(normalChordSpacings, graceChordSpacings, widthSharp, widthFlat, widthClef,
			widthMeasureEmpty, widthDistanceMin);
	}
	

	public Spacings(ChordSpacings normalChordSpacings, ChordSpacings graceChordSpacings,
		float widthSharp, float widthFlat, float widthClef, float widthMeasureEmpty, float widthDistanceMin)
	{
		this.normalChordSpacings = normalChordSpacings;
		this.graceChordSpacings = graceChordSpacings;
		this.widthSharp = widthSharp;
		this.widthFlat = widthFlat;
		this.widthClef = widthClef;
		this.widthMeasureEmpty = widthMeasureEmpty;
		this.widthDistanceMin = widthDistanceMin;
	}

}
