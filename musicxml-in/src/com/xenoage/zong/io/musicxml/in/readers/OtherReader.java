package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.musicxml.types.MxlBezier;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;

/**
 * Reads some other MusicXML elements.
 * 
 * @author Andreas Wenger
 */
public final class OtherReader {

	@MaybeNull public static Alignment readAlignment(MxlLeftCenterRight mxlLeftCenterRight) {
		switch (mxlLeftCenterRight) {
			case Left:
				return Alignment.Left;
			case Center:
				return Alignment.Center;
			case Right:
				return Alignment.Right;
			default:
				return null;
		}
	}

	@MaybeNull public static BezierPoint readBezierPoint(MxlPosition mxlPosition,
		MxlBezier mxlBezier, float tenthsMm, int staffLinesCount, float noteLP, Fraction chordDuration) {
		Float px = mxlPosition.getDefaultX();
		Float py = mxlPosition.getDefaultY();
		Float cx = (mxlBezier != null ? mxlBezier.getBezierX() : null);
		Float cy = (mxlBezier != null ? mxlBezier.getBezierY() : null);
		SP point = null;
		SP control = null;

		float halfNoteWidth = getNoteheadWidth(chordDuration) / 2;
		if (px != null && py != null) {
			float fpx = notNull(px, 0).floatValue();
			float fpy = notNull(py, 0).floatValue();
			//default-x is relative to left side of note. thus, substract the half width
			//of a note (TODO: note type. e.g., whole note is wider)
			point = sp((fpx / 10 - halfNoteWidth) * tenthsMm, (staffLinesCount - 1) * 2 + fpy / 10 * 2 -
				noteLP);
		}
		if (cx != null && cy != null) {
			float fcx = notNull(cx, 0).floatValue();
			float fcy = notNull(cy, 0).floatValue();
			control = sp((fcx / 10 - halfNoteWidth) * tenthsMm, fcy / 10 * 2);
		}
		if (point != null || control != null)
			return new BezierPoint(point, control);
		else
			return null;
	}
	
	/**
	 * Returns the duration as a {@link Fraction} from the given duration in divisions.
	 */
	public static Fraction readDuration(int duration, int divisionsPerQuarter) {
		if (duration == 0) {
			throw new RuntimeException("Element has a duration of 0.");
		}
		Fraction ret = fr(duration, 4 * divisionsPerQuarter);
		return ret;
	}

	@MaybeNull public static VSide readVSide(MxlPlacement mxlPlacement) {
		if (mxlPlacement == MxlPlacement.Above)
			return VSide.Top;
		else if (mxlPlacement == MxlPlacement.Below)
			return VSide.Bottom;
		else
			return null;
	}

	//TIDY: see ChordWidths, but is defined in layout project
	private static float getNoteheadWidth(Fraction chordDuration) {
		if (chordDuration.compareTo(_1) < 0)
			return 1.2f; //quarter or half notehead
		else
			return 2f; //whole note
	}

}
