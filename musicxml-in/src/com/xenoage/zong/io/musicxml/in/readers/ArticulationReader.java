package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.addNotNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPlacement;

import java.util.List;

import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.io.musicxml.Equivalents;
import com.xenoage.zong.musicxml.types.MxlArticulations;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent.MxlArticulationsContentType;

/**
 * Reads {@link Articulation}s from {@link MxlArticulations}.
 * 
 * @author Andreas Wenger
 */
public class ArticulationReader {

	public static List<Articulation> read(MxlArticulations mxlArticulations) {
		List<Articulation> ret = alist();
		for (MxlArticulationsContent mxlAC : mxlArticulations.getContent())
			addNotNull(ret, readArticulation(mxlAC));
		return ret;
	}

	private static Articulation readArticulation(MxlArticulationsContent mxlAC) {
		MxlArticulationsContentType mxlACType = mxlAC.getArticulationsContentType();
		ArticulationType type = Equivalents.articulations.get1(mxlACType);
		if (type == null)
			return null;
		Articulation articulation = new Articulation(type);
		//read placement
		MxlEmptyPlacement mxlPlacement = mxlAC.getEmptyPlacement();
		if (mxlPlacement != null) {
			Placement placement = readPlacement(mxlPlacement.getPlacement());
			articulation.setPlacement(placement);
		}
		return articulation;
	}
	
}
