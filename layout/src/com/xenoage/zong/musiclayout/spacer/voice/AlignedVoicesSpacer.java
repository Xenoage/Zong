package com.xenoage.zong.musiclayout.spacer.voice;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.utils.math.Fraction.fr;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Computes the {@link VoiceSpacing}s for all voices in a measure column,
 * based on the individual optimal {@link VoiceSpacing}s and the
 * common {@link BeatOffset}s of this measure column.
 * 
 * @author Andreas Wenger
 */
public class AlignedVoicesSpacer {
	
	public static final AlignedVoicesSpacer alignedVoicesSpacer = new AlignedVoicesSpacer();

	
	/**
	 * Modifies the given {@link VoiceSpacing}, based on the given beat offsets.
	 */
	public void compute(VoiceSpacing voiceSpacing, List<BeatOffset> beatOffsets) {
		
		List<ElementSpacing> spacingElements = voiceSpacing.spacingElements;
		if (spacingElements.size() == 0 || beatOffsets.size() == 0)
			return;
		
		//find the given beats, that are also used here
		List<BeatOffset> sharedBeats = computeSharedBeats(spacingElements, beatOffsets);
		
		//interpolate positions between the shared beats
		float lastGivenBeatPosition = 0;
		float lastEndElementPosition = 0;
		int firstElement = 0;
		int lastElement = -1;
		float interlineSpace = voiceSpacing.interlineSpace;
		for (int iGivenBeat : range(sharedBeats)) {
			//for each given beat: find elements before or at that beat
			for (int iElement : range(lastElement + 1, spacingElements.size() - 1)) {
				if (spacingElements.get(iElement).beat.compareTo(sharedBeats.get(iGivenBeat).getBeat()) > 0)
					break;
				lastElement++;
			}
			if (lastElement == -1)
				break;
			//compute horizontal positions and distances of the
			//given beat offsets and the current voice spacing, from the
			//last shared beat up to the current shared beat.
			float currentGivenBeatPosition = sharedBeats.get(iGivenBeat).getOffsetMm() / interlineSpace; //we calculate in interline spaces here
			float givenBeatsDistance = currentGivenBeatPosition - lastGivenBeatPosition;
			float currentEndElementPosition = spacingElements.get(lastElement).offsetIs;
			float elementsDistance = currentEndElementPosition - lastEndElementPosition;
			//interpolate the offsets of the current voice spacing
			//between the last shared beat and the current shared beat.
			//do this in reverse order, because the position of grace notes is dependent
			//on the position of the (following) main note
			float lastOriginalOffsetIs = 0;
			for (int iElement : rangeReverse(lastElement, firstElement)) {
				ElementSpacing e = spacingElements.get(iElement);
				if (!e.grace) {
					//normal element: interpolate position
					float currentElementOffset = e.offsetIs - lastEndElementPosition;
					float newElementOffset;
					if (elementsDistance != 0) {
						newElementOffset = currentElementOffset / elementsDistance * givenBeatsDistance; //scale offset according to the given beats distance
					}
					else {
						newElementOffset = currentGivenBeatPosition;
					}
					lastOriginalOffsetIs = e.offsetIs;
					e.offsetIs = newElementOffset + lastGivenBeatPosition;
				}
				else {
					//grace element: same distance to the main note as before
					float distanceBefore = lastOriginalOffsetIs - e.offsetIs;
					lastOriginalOffsetIs = e.offsetIs;
					e.offsetIs = spacingElements.get(iElement + 1).offsetIs - distanceBefore;
				}
			}
			//next range up to next shared beat
			firstElement = lastElement + 1;
			if (firstElement >= spacingElements.size())
				break;
			lastGivenBeatPosition = currentGivenBeatPosition;
			lastEndElementPosition = currentEndElementPosition;
		}
	}

	/**
	 * Returns the shared beats of the given {@link ElementSpacing}s and {@link BeatOffset}s.
	 * If there are no beats used by both lists, an empty list is returned.
	 */
	public List<BeatOffset> computeSharedBeats(List<ElementSpacing> spacingElements, List<BeatOffset> beatOffsets) {
		ArrayList<BeatOffset> ret = alist(Math.min(spacingElements.size(), beatOffsets.size()));
		int iElement = 0, iBeat = 0;
		Fraction lastAddedBeat = fr(-1);
		while (iElement < spacingElements.size() && iBeat < beatOffsets.size()) {
			Fraction elementBeat = spacingElements.get(iElement).beat;
			BeatOffset beatOffset = beatOffsets.get(iBeat);
			if (elementBeat.equals(beatOffset.beat)) {
				if (beatOffset.beat.equals(lastAddedBeat)) {
					//when this beat was already added, replace it. the
					//rightmost offset is the offset we need.
					ret.set(ret.size() - 1, beatOffset);
				}
				else {
					ret.add(beatOffset);
				}
				lastAddedBeat = beatOffset.beat;
				iElement++;
			}
			else if (elementBeat.compareTo(beatOffset.beat) > 0) {
				iBeat++;
			}
			else {
				iElement++;
			}
		}
		return ret;
	}

}
