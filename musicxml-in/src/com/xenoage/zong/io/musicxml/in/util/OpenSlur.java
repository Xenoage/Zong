package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;

/**
 * An unclosed slur, needed during MusicXML import.
 * A slur may be closed before it is opened, e.g.
 * when it is closed on beat 3/4 in voice 1 (read first),
 * but opened on beat 1/4 in voice 2 (read later).
 * 
 * @author Andreas Wenger
 */
public class OpenSlur {

	public static class Waypoint {

		public SlurWaypoint wp;
		public VSide side;
	}


	public SlurType type;
	public Waypoint start;
	public Waypoint stop;

}
