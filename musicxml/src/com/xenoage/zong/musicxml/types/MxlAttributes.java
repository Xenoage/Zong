package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.Parse.parseInt;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML attributes.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="editorial,part-symbol,instruments,staff-details," +
	"directive,measure-style", partly="time,clef", children="key,time,clef")
public final class MxlAttributes
	implements MxlMusicDataContent
{
	
	public static final String ELEM_NAME = "attributes";
	
	@MaybeNull public final Integer divisions;
	@MaybeNull public final MxlKey key;
	@MaybeNull public final MxlTime time;
	@MaybeNull public final Integer staves;
	@MaybeEmpty public final PVector<MxlClef> clefs;
	@MaybeNull public final MxlTranspose transpose;
	
	private static final PVector<MxlClef> emptyClefs = pvec();
	
	
	public MxlAttributes(Integer divisions, MxlKey key, MxlTime time, Integer staves,
		PVector<MxlClef> clefs, MxlTranspose transpose)
	{
		this.divisions = divisions;
		this.key = key;
		this.time = time;
		this.staves = staves;
		this.clefs = clefs;
		this.transpose = transpose;
	}
	
	
	@Override public MxlMusicDataContentType getMusicDataContentType()
	{
		return MxlMusicDataContentType.Attributes;
	}
	
	
	@NeverNull public static MxlAttributes read(Element e)
	{
		Integer divisions = null;
		MxlKey key = null;
		MxlTime time = null;
		Integer staves = null;
		PVector<MxlClef> clefs = emptyClefs;
		MxlTranspose transpose = null;
		for (Element child : elements(e))
		{
			String n = child.getNodeName();
			switch (n.charAt(0))
			{
				case 'c':
					if (MxlClef.elemName.equals(n))
						clefs = clefs.plus(MxlClef.read(child));
					break;
				case 'd':
					if ("divisions".equals(n))
						divisions = parseInt(child);
					break;
				case 'k':
					if (MxlKey.elemName.equals(n))
						key = MxlKey.read(child);
					break;
				case 's':
					if ("staves".equals(n))
						staves = parseInt(child);
					break;
				case 't':
					if (MxlTime.ELEM_NAME.equals(n))
						time = MxlTime.read(child);
					else if (MxlTranspose.elemName.equals(n))
						transpose = MxlTranspose.read(child);
					break;
			}
		}
		return new MxlAttributes(divisions, key, time, staves, clefs, transpose);
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("divisions", divisions, e);
		if (key != null)
			key.write(e);
		if (time != null)
			time.write(e);
		addElement("staves", staves, e);
		for (MxlClef clef : clefs)
			clef.write(e);
		if (transpose != null)
			transpose.write(e);
	}
	
	
	
	
	

}
