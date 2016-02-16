package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.zong.core.music.direction.Wedge;

import lombok.Getter;
import lombok.Setter;

/**
 * Open wedges, beams, slurs and ties.
 * 
 * @author Andreas Wenger
 */
@Getter
public final class OpenElements {

	private List<Wedge> openWedges;
	private OpenBeams openBeams;
	private List<OpenSlur> openSlurs;
	private List<OpenSlur> openTies;
	private OpenUnnumberedTieds openUnnumberedTies;
	@Setter private OpenVolta openVolta;


	public OpenElements() {
		this.openWedges = alist(null, null, null, null, null, null);
		this.openBeams = new OpenBeams();
		this.openSlurs = alist(null, null, null, null, null, null);
		this.openTies = alist(null, null, null, null, null, null);
		this.openUnnumberedTies = new OpenUnnumberedTieds();
	}

}
