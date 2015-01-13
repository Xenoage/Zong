package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.zong.core.music.annotation.Ornament;
import com.xenoage.zong.core.music.annotation.OrnamentType;
import com.xenoage.zong.io.musicxml.Equivalents;
import com.xenoage.zong.musicxml.types.MxlOrnaments;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent.MxlOrnamentsContentType;

/**
 * Reads {@link Ornament}s from {@link MxlOrnaments}.
 * 
 * @author Andreas Wenger
 */
public class OrnamentReader {

	public static List<Ornament> read(MxlOrnaments mxlOrnaments) {
		List<Ornament> ret = alist();
		for (MxlOrnamentsContent mxlOC : mxlOrnaments.getContent()) {
			MxlOrnamentsContentType mxlOCType = mxlOC.getOrnamentsContentType();
			OrnamentType type = Equivalents.ornaments.get1(mxlOCType);
			if (type != null) {
				Ornament ornament = new Ornament(type);
				ret.add(ornament);
			}
		}
		return ret;
	}
	
}
