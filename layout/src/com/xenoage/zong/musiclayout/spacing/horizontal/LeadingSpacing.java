package com.xenoage.zong.musiclayout.spacing.horizontal;

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * The leading spacing of a measure of a single staff.
 * 
 * This spacing contains for example elements
 * like initial clefs and key signatures.
 * 
 * TODO: we also need a "TrailingSpacing" for the last measure
 * in a staff to warn about clef or key signature changes.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class LeadingSpacing {

	/** The element spacing of this measure leading. */
	public List<ElementSpacing> elements;
	/** The width of this leading spacing in interline spaces. */
	public float width;

}
