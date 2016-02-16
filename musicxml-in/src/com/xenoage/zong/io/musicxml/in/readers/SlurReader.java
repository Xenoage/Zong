package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readBezierPoint;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readVSide;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.io.musicxml.in.util.OpenSlur;
import com.xenoage.zong.io.musicxml.in.util.OpenUnnumberedTieds;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied.MxlElementType;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopContinue;

/**
 * Reads a {@link Slur} from a {@link MxlSlurOrTied} in its context.
 * 
 * @author Andreas Wenger
 */
public class SlurReader {
	
	public static void readToContext(Chord chord, int noteIndex, int staffIndexInPart,
		Context context, MxlSlurOrTied mxlSlur) {
		
		Pitch pitch = chord.getNotes().get(noteIndex).getPitch();
		float noteLP = context.getMusicContext(staffIndexInPart).getLp(pitch);

		//type
		SlurType type = (mxlSlur.getElementType() == MxlElementType.Slur ?
			SlurType.Slur : SlurType.Tie);

		//number (tied does usually not use a number, but is distinguished by pitch)
		Integer number = mxlSlur.getNumber();
		BezierPoint bezierPoint = readBezierPoint(mxlSlur.getPosition(),
			mxlSlur.getBezier(), context.getTenthMm(),
			context.getStaffLinesCount(staffIndexInPart), noteLP, chord.getDuration());
		VSide side = readVSide(mxlSlur.getPlacement());

		//waypoint
		SlurWaypoint wp = new SlurWaypoint(chord, noteIndex, bezierPoint);
		if (type == SlurType.Tie && number == null) {
			//unnumbered tied
			OpenUnnumberedTieds openTieds = context.getOpenElements().getOpenUnnumberedTies();
			if (mxlSlur.getType() == MxlStartStopContinue.Start) {
				openTieds.startTied(wp, side);
			}
			else if (mxlSlur.getType() == MxlStartStopContinue.Stop) {
				OpenSlur openTied = openTieds.stopTied(wp, side, context);
				if (openTied != null)
					context.createSlur(openTied);
			}
		}
		else {
			//numbered
			WaypointPosition wpPos;
			if (mxlSlur.getType() == MxlStartStopContinue.Start)
				wpPos = WaypointPosition.Start;
			else if (mxlSlur.getType() == MxlStartStopContinue.Stop)
				wpPos = WaypointPosition.Stop;
			else
				wpPos = WaypointPosition.Continue;
			context.registerSlur(type, wpPos, number, wp, side);
		}
	}

}
