package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Strategy to compute the horizontal spacing for a
 * single voice, based on optimal {@link VoiceSpacing}s
 * and the {@link BeatOffset}s of this measure column.
 * 
 * @author Andreas Wenger
 */
public class BeatOffsetBasedVoiceSpacingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes and returns the horizontal spacing for a
	 * single voice, based on the given beat offsets, that
	 * are used for the result, and the given precomputed voice spacing.
	 */
	public VoiceSpacing computeVoiceSpacing(VoiceSpacing voiceSpacing, List<BeatOffset> beatOffsets) {
		
		List<SpacingElement> oldSpacingElements = voiceSpacing.getSpacingElements();
		CList<SpacingElement> spacingElements = clist(oldSpacingElements);
		if (spacingElements.size() == 0 || beatOffsets.size() == 0)
			return voiceSpacing;
		//find the given beats, that are also used here
		List<BeatOffset> sharedBeats = computeSharedBeats(spacingElements, beatOffsets);
		//interpolate positions between the shared beats
		float lastGivenBeatPosition = 0;
		float lastEndElementPosition = 0;
		int firstElement = 0;
		int lastElement = -1;
		float interlineSpace = voiceSpacing.getInterlineSpace();
		for (int iGivenBeat = 0; iGivenBeat < sharedBeats.size(); iGivenBeat++) {
			//for each given beat: find elements before or at that beat
			for (int iElement = lastElement + 1; iElement < spacingElements.size(); iElement++) {
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
			float currentEndElementPosition = spacingElements.get(lastElement).offset;
			float elementsDistance = currentEndElementPosition - lastEndElementPosition;
			//interpolate the offsets of the current voice spacing
			//between the last shared beat and the current shared beat.
			//do this in reverse order, because the position of grace notes is dependent
			//on the position of the (following) main note
			for (int iElement = lastElement; iElement >= firstElement; iElement--) {
				SpacingElement e = spacingElements.get(iElement);
				if (!e.grace) {
					//normal element: interpolate position
					float currentElementOffset = e.offset - lastEndElementPosition;
					float newElementOffset;
					if (elementsDistance != 0) {
						newElementOffset = currentElementOffset / elementsDistance * givenBeatsDistance; //scale offset according to the given beats distance
					}
					else {
						newElementOffset = currentGivenBeatPosition;
					}
					spacingElements.set(iElement, e.withOffset(newElementOffset + lastGivenBeatPosition));
				}
				else {
					//grace element: same distance to the main note as before
					float distanceBefore = oldSpacingElements.get(iElement + 1).offset -
						oldSpacingElements.get(iElement).offset;
					spacingElements.set(iElement,
						e.withOffset(spacingElements.get(iElement + 1).offset - distanceBefore));
				}
			}
			//next range up to next shared beat
			firstElement = lastElement + 1;
			if (firstElement >= spacingElements.size())
				break;
			lastGivenBeatPosition = currentGivenBeatPosition;
			lastEndElementPosition = currentEndElementPosition;
		}
		spacingElements.close();
		
		return new VoiceSpacing(voiceSpacing.getVoice(), voiceSpacing.getInterlineSpace(),
			spacingElements);
	}

	/**
	 * Finds and returns the shared beats of the given
	 * {@link SpacingElement}s and {@link BeatOffset}s.
	 * If there are no beats used by both lists, an empty
	 * list is returned.
	 */
	public List<BeatOffset> computeSharedBeats(List<SpacingElement> spacingElements,
		List<BeatOffset> beatOffsets) {
		ArrayList<BeatOffset> ret = alist(beatOffsets.size());
		int i1 = 0, i2 = 0;
		Fraction lastAddedBeat = fr(-1);
		while (i1 < spacingElements.size() && i2 < beatOffsets.size()) {
			Fraction beat1 = spacingElements.get(i1).beat;
			BeatOffset beatOffset2 = beatOffsets.get(i2);
			Fraction beat2 = beatOffset2.getBeat();
			if (beat1.equals(beat2)) {
				if (beat2.equals(lastAddedBeat)) {
					//when this beat was already added, replace it. the
					//rightmost offset is the offset we need.
					ret.set(ret.size() - 1, beatOffset2);
				}
				else {
					ret.add(beatOffset2);
				}
				lastAddedBeat = beat2;
				i1++;
			}
			else if (beat1.compareTo(beat2) > 0) {
				i2++;
			}
			else {
				i1++;
			}
		}
		return ret;
	}

}
