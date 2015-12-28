package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.layout.frames.ScoreFrame;

/**
 * The spacing information of the musical layout in a {@link ScoreFrame}.
 * 
 * This class contains the spacing of a measure of a single staff.
 * 
 * @author Andreas Wenger
 */
@Const @Getter
public final class MeasureSpacing {

	/** The musical position of the measure. */
	@NonNull private MP mp;
	/** The interline space in mm of this measure. */
	private float interlineSpace;
	/** The spacings of the voices */
	@NonEmpty private List<VoiceSpacing> voiceSpacings;
	/** The spacings of the measure elements, like inner clefs */
	@NonNull private List<ElementSpacing> measureElementsSpacings;
	/** The leading spacing, which may contain elements like initial clefs or key signatures */
	@MaybeNull private LeadingSpacing leadingSpacing;
	/** A sorted list of all used beats in this measure */
	@MaybeEmpty private List<Fraction> usedBeats;


	public MeasureSpacing(MP mp, float interlineSpace, List<VoiceSpacing> voiceSpacings,
		List<ElementSpacing> measureElementSpacings, LeadingSpacing leadingSpacing) {
		this.mp = mp;
		this.interlineSpace = interlineSpace;
		this.voiceSpacings = voiceSpacings;
		this.measureElementsSpacings = measureElementSpacings;
		this.leadingSpacing = leadingSpacing;

		//compute the list of all used beats
		SortedList<Fraction> usedBeats = new SortedList<Fraction>(false);
		for (VoiceSpacing vs : voiceSpacings) {
			for (ElementSpacing se : vs.elements) {
				usedBeats.add(se.beat);
			}
		}
		this.usedBeats = alist(usedBeats.getLinkedList());
	}

}
