package com.xenoage.zong.musiclayout.spacing.horizontal;

import static com.xenoage.utils.pdlib.IVector.ivec;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.base.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.position.MP;


/**
 * This class contains the spacing
 * of a measure of a single staff.
 * 
 * @author Andreas Wenger
 */
public final class MeasureSpacing
{

	/** The musical position of the measure */
	@NeverNull public final MP mp;
	/** The spacings of the voices */
	@NeverEmpty public final Vector<VoiceSpacing> voiceSpacings;
	/** The spacings of the measure elements, like inner clefs */
	@NeverNull public final MeasureElementsSpacings measureElementsSpacings;
	/** The leading spacing, which may contain elements like initial clefs or key signatures */
	@MaybeNull public final LeadingSpacing leadingSpacing;
	/** A sorted list of all used beats in this measure */
	@MaybeEmpty public final Vector<Fraction> usedBeats;


	public MeasureSpacing(MP mp, Vector<VoiceSpacing> voiceSpacings,
		MeasureElementsSpacings measureElementSpacings, LeadingSpacing leadingSpacing)
	{
		this.mp = mp;
		this.voiceSpacings = voiceSpacings;
		this.measureElementsSpacings = measureElementSpacings;
		this.leadingSpacing = leadingSpacing;

		//compute the list of all used beats
		SortedList<Fraction> usedBeats = new SortedList<Fraction>(false);
		for (VoiceSpacing vs : voiceSpacings) {
			for (SpacingElement se : vs.getSpacingElements()) {
				usedBeats.add(se.beat);
			}
		}
		this.usedBeats = ivec(usedBeats.getLinkedList()).close();
	}

}
