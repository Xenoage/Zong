package com.xenoage.zong.musiclayout.spacing.horizontal;

import static com.xenoage.utils.collections.CList.ilist;

import java.util.List;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MP;

/**
 * This class contains the spacing of a measure of a single staff.
 * 
 * @author Andreas Wenger
 */
//TIDY: not Const
@Const @Getter public final class MeasureSpacing {
	
	/** An empty array of {@link MeasureSpacing}s. */
	public static final MeasureSpacing[] empty = new MeasureSpacing[0];

	/** The musical position of the measure. */
	@NonNull private final MP mp;
	/** The spacings of the voices */
	@NonEmpty private List<VoiceSpacing> voiceSpacings;
	/** The spacings of the measure elements, like inner clefs */
	@NonNull private final MeasureElementsSpacing measureElementsSpacings;
	/** The leading spacing, which may contain elements like initial clefs or key signatures */
	@MaybeNull private final LeadingSpacing leadingSpacing;
	/** A sorted list of all used beats in this measure */
	@MaybeEmpty private final IList<Fraction> usedBeats;


	public MeasureSpacing(MP mp, List<VoiceSpacing> voiceSpacings,
		MeasureElementsSpacing measureElementSpacings, LeadingSpacing leadingSpacing) {
		this.mp = mp;
		this.voiceSpacings = voiceSpacings;
		this.measureElementsSpacings = measureElementSpacings;
		this.leadingSpacing = leadingSpacing;

		//compute the list of all used beats
		SortedList<Fraction> usedBeats = new SortedList<Fraction>(false);
		for (VoiceSpacing vs : voiceSpacings) {
			for (ElementSpacing se : vs.spacingElements) {
				usedBeats.add(se.beat);
			}
		}
		this.usedBeats = ilist(usedBeats.getLinkedList());
	}

}
