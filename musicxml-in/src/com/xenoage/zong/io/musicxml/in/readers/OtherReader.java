package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.musicxml.types.MxlBezier;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;

/**
 * Reads some other MusicXML elements.
 * 
 * @author Andreas Wenger
 */
public final class OtherReader {

	@MaybeNull public static Alignment readAlignment(MxlLeftCenterRight mxlLeftCenterRight) {
		if (mxlLeftCenterRight != null) {
			switch (mxlLeftCenterRight) {
				case Left:
					return Alignment.Left;
				case Center:
					return Alignment.Center;
				case Right:
					return Alignment.Right;
			}
		}
		return null;
	}

	@MaybeNull public static BezierPoint readBezierPoint(MxlPosition mxlPosition,
		MxlBezier mxlBezier, float tenthsMm, int staffLinesCount, float noteLP, Fraction chordDuration) {
		Float px = (mxlPosition != null ? mxlPosition.getDefaultX() : null);
		Float py = (mxlPosition != null ? mxlPosition.getDefaultY() : null);
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

	@MaybeNull public static Placement readPlacement(MxlPlacement mxlPlacement) {
		switch (mxlPlacement) {
			case Above:
				return Placement.Above;
			case Below:
				return Placement.Below;
		}
		return null;
	}
	
	@MaybeNull public static Position readPosition(MxlPrintStyle printStyle, float tenthsMm,
		int staffLinesCount) {
		if (printStyle == null)
			return null;
		return readPosition(printStyle.getPosition(), tenthsMm, staffLinesCount);
	}

	@MaybeNull public static Position readPosition(MxlPosition mxlPosition, float tenthsMm,
		int staffLinesCount) {
		Float x = mxlPosition.getDefaultX();
		Float y = mxlPosition.getDefaultY();
		Float rx = mxlPosition.getRelativeX();
		Float ry = mxlPosition.getRelativeY();
		if (x == null && y == null && rx == null && ry == null) {
			return null;
		}
		else {
			Float fx = null;
			if (x != null) {
				fx = x / 10 * tenthsMm;
			}
			Float fy = null;
			if (y != null) {
				fy = (staffLinesCount - 1) * 2 + y / 10 * 2;
			}
			Float frx = null;
			if (rx != null) {
				frx = rx / 10 * tenthsMm;
			}
			Float fry = null;
			if (ry != null) {
				fry = ry / 10 * 2;
			}
			return new Position(fx, fy, frx, fry);
		}
	}

	/**
	 * Reads a {@link Position} or {@link Placement}. Arguments may be null.
	 * {@link Position}s have higher priority than {@link Placement}s. The first
	 * placement has higher priority than the second one.
	 */
	@MaybeNull public static Positioning readPositioning(MxlPosition mxlPosition,
		MxlPlacement mxlPlacement1, MxlPlacement mxlPlacement2, float tenthsMm, int staffLinesCount) {
		Position position = readPosition(mxlPosition, tenthsMm, staffLinesCount);
		if (position != null)
			return position;
		else if (mxlPlacement1 != null)
			return readPlacement(mxlPlacement1);
		else if (mxlPlacement2 != null)
			return readPlacement(mxlPlacement2);
		return null;
	}

	@MaybeNull public static VSide readVSide(MxlPlacement mxlPlacement) {
		if (mxlPlacement == null)
			return null;
		else if (mxlPlacement == MxlPlacement.Above)
			return VSide.Top;
		else
			return VSide.Bottom;
	}

	//TIDY: see ChordWidths, but is defined in layout project
	private static float getNoteheadWidth(Fraction chordDuration) {
		if (chordDuration.compareTo(_1) < 0)
			return 1.2f; //quarter or half notehead
		else
			return 2f; //whole note
	}

}
