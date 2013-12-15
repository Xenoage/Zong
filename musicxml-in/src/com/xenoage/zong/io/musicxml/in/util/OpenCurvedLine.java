package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.curvedline.CurvedLine;
import com.xenoage.zong.core.music.curvedline.CurvedLineWaypoint;


/**
 * An unclosed curved line, needed during MusicXML import.
 * A curved line may be closed before it is opened, e.g.
 * when it is closed on beat 3/4 in voice 1 (read first),
 * but opened on beat 1/4 in voice 2 (read later).
 * 
 * @author Andreas Wenger
 */
//GOON
public class OpenCurvedLine
{
	
	public static class Waypoint
	{
		public CurvedLineWaypoint wp;
		public VSide side;
	}
	
	public CurvedLine.Type type;
	public Waypoint start;
	public Waypoint stop;

}
