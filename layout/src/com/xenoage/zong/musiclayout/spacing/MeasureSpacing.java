package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.layout.frames.ScoreFrame;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * The spacing information of the musical layout in a {@link ScoreFrame}.
 * 
 * This class contains the spacing of a measure of a single staff.
 * 
 * @author Andreas Wenger
 */
public final class MeasureSpacing {

	/** The musical position of the measure. */
	@NonNull public MP mp;
	/** The interline space in mm of this measure. */
	public float interlineSpace;
	/** The spacings of the voices */
	@NonEmpty public List<VoiceSpacing> voices;
	/** The spacings of the measure elements, like inner clefs */
	@NonNull public List<ElementSpacing> elements;
	/** The leading spacing, which may contain elements like initial clefs or key signatures */
	@MaybeNull public LeadingSpacing leading;
	/** A sorted list of all used beats in this measure */
	@MaybeEmpty public List<Fraction> usedBeats;

	/** The parent column. */
	public ColumnSpacing column;
	

	public MeasureSpacing(MP mp, float interlineSpace, List<VoiceSpacing> voices,
		List<ElementSpacing> elements, LeadingSpacing leading) {
		this.mp = mp;
		this.interlineSpace = interlineSpace;
		this.voices = voices;
		this.elements = elements;
		this.leading = leading;

		//compute the list of all used beats
		SortedList<Fraction> usedBeats = new SortedList<>(false);
		for (VoiceSpacing vs : voices) {
			for (ElementSpacing se : vs.elements) {
				usedBeats.add(se.beat);
			}
		}
		this.usedBeats = alist(usedBeats.getLinkedList());
		
		//set backward references
		for (VoiceSpacing voice : voices)
			voice.measure = this;
	}

}
