package com.xenoage.zong.io.musicxml.in.readers;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlFormattedText;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.util.MxlEmptyPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlFormattedTextContent;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlPositionContent;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;

/**
 * Reads {@link Positioning} information from any MusicXML element.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class PositioningReader {
	
	private final StaffDetails staffDetails;
	
	
	/**
	 * Reads the positioning from any given MusicXML element.
	 * An inner {@link MxlFormattedText} has highest priority, then an inner {@link MxlEmptyPlacement},
	 * then an inner {@link MxlPrintStyle}, then a {@link MxlPosition}, then a {@link MxlPlacement}.
	 */
	@MaybeNull public Positioning readFromAny(Object anyElement) {
		Positioning positioning = null;
		if (anyElement == null)
			return null;
		//first priority: inner MxlFormattedText
		if (anyElement instanceof MxlFormattedTextContent)
			positioning = readFromPrintStyle(((MxlFormattedTextContent) anyElement).getFormattedText());
		//second priority: inner MxlEmptyPlacement
		if (anyElement instanceof MxlEmptyPlacementContent)
			positioning = readFromAny(((MxlEmptyPlacementContent) anyElement).getEmptyPlacement());
		//third priority: inner MxlPrintStyle
		if (positioning == null && anyElement instanceof MxlPrintStyleContent)
			positioning = readFromPrintStyle(((MxlPrintStyleContent) anyElement));
		//fourth priority: position
		if (positioning == null && anyElement instanceof MxlPositionContent)
			positioning = readFromPosition(((MxlPositionContent) anyElement));
		//fifth priority: placement
		if (positioning == null && anyElement instanceof MxlPlacementContent)
			positioning = readFromPlacement(((MxlPlacementContent) anyElement));
		return positioning;
	}
	
	/**
	 * Like {@link #readFromAny(Object)}, but an additional elements are considered
	 * as long as the previous ones contains no positioning information.
	 */
	@MaybeNull public Positioning readFromAny(Object... anyElements) {
		for (Object anyElement : anyElements) {
			Positioning positioning = readFromAny(anyElement);
			if (positioning != null)
				return positioning;
		}
		return null;
	}
	
	@MaybeNull public Position readFromPrintStyle(MxlPrintStyleContent mxlPrintStyleContent) {
		MxlPrintStyle mxlPrintStyle = mxlPrintStyleContent.getPrintStyle();
		return readFromPosition(mxlPrintStyle);
	}
	
	@MaybeNull public Position readFromPosition(MxlPositionContent mxlPositionContent) {
		return readPosition(mxlPositionContent.getPosition());
	}

	@MaybeNull public Position readPosition(MxlPosition mxlPosition) {
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
				fx = x / 10 * staffDetails.tenthsMm;
			}
			Float fy = null;
			if (y != null) {
				fy = (staffDetails.linesCount - 1) * 2 + y / 10 * 2;
			}
			Float frx = null;
			if (rx != null) {
				frx = rx / 10 * staffDetails.tenthsMm;
			}
			Float fry = null;
			if (ry != null) {
				fry = ry / 10 * 2;
			}
			return new Position(fx, fy, frx, fry);
		}
	}
	
	@MaybeNull public static Placement readFromPlacement(MxlPlacementContent mxlPlacementContent) {
		MxlPlacement mxlPlacement = mxlPlacementContent.getPlacement();
		return readPlacement(mxlPlacement);
	}
	
	@MaybeNull public static Placement readPlacement(MxlPlacement mxlPlacement) {
		switch (mxlPlacement) {
			case Above:
				return Placement.Above;
			case Below:
				return Placement.Below;
			default:
				return null;
		}
	}

}
