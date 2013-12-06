package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.NeverNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;

/**
 * MusicXML accent.
 * 
 * @author Andreas Wenger
 */
public final class MxlAccent
	implements MxlArticulationsContent {

	public static final String elemName = "accent";

	@NonNull private final MxlEmptyPlacement emptyPlacement;

	public static final MxlAccent defaultInstance = new MxlAccent(MxlEmptyPlacement.empty);


	public MxlAccent(MxlEmptyPlacement emptyPlacement) {
		this.emptyPlacement = emptyPlacement;
	}

	@NeverNull public MxlEmptyPlacement getEmptyPlacement() {
		return emptyPlacement;
	}

	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.Accent;
	}

	@NeverNull public static MxlAccent read(Element e) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(e);
		if (emptyPlacement != MxlEmptyPlacement.empty)
			return new MxlAccent(emptyPlacement);
		else
			return defaultInstance;
	}

	@Override public void write(Element parent) {
		Element e = addElement(elemName, parent);
		emptyPlacement.write(e);
	}

}
