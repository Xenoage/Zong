package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Wedge;

/**
 * Open wedges, beams, slurs and ties.
 * 
 * @author Andreas Wenger
 */
@Getter
public final class OpenElements {

	private List<Wedge> openWedges;
	private List<List<Chord>> openBeams;
	private List<OpenSlur> openSlurs;
	private List<OpenSlur> openTies;
	private Map<Pitch, OpenSlur> openUnnumberedTies;
	@Setter private OpenVolta openVolta;


	@SuppressWarnings("unchecked") public OpenElements() {
		this.openWedges = alist(null, null, null, null, null, null);
		this.openBeams = alist(null, null, null, null, null, null);
		this.openSlurs = alist(null, null, null, null, null, null);
		this.openTies = alist(null, null, null, null, null, null);
		this.openUnnumberedTies = map();
	}

}
