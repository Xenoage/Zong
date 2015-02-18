package com.xenoage.zong.musiclayout.spacer.system.fill;

import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import com.xenoage.zong.musiclayout.spacing.BeatOffset;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;

/**
 * Stretches all measures within the given system,
 * so that they use the whole useable width of the system.
 * 
 * @author Andreas Wenger
 */
public class StretchMeasures
	implements SystemFiller {

	public static final StretchMeasures stretchMeasures = new StretchMeasures();


	@Override public void compute(SystemSpacing system, float usableWidthMm) {
		//compute width of all voice spacings
		//(leading spacings are not stretched)
		float voicesWidthMm = 0;
		float leadingsWidthMm = 0;
		for (ColumnSpacing column : system.columns) {
			voicesWidthMm += column.getVoicesWidthMm();
			leadingsWidthMm += column.getLeadingWidthMm();
		}

		//compute the stretching factor for the voice spacings
		if (voicesWidthMm == 0)
			return;
		float stretch = (usableWidthMm - leadingsWidthMm) / voicesWidthMm;

		//stretch the voice spacings
		//measure columns
		for (ColumnSpacing column : system.columns) {
			
			//beat offsets
			for (int i : range(column.beatOffsets)) {
				BeatOffset bo = column.beatOffsets.get(i);
				BeatOffset stretched = bo.withOffsetMm(bo.offsetMm * stretch);
				column.beatOffsets.set(i, stretched);
			}
			for (int i : range(column.barlineOffsets)) {
				BeatOffset bo = column.barlineOffsets.get(i);
				BeatOffset stretched = bo.withOffsetMm(bo.offsetMm * stretch);
				column.barlineOffsets.set(i, stretched);
			}
			
			//measures
			for (MeasureSpacing measure : column.measures) {
				//measure elements
				for (ElementSpacing element : measure.getMeasureElementsSpacings()) {
					//stretch the offset
					element.offsetIs *= stretch;
				}
				//voices
				for (VoiceSpacing voice : measure.getVoiceSpacings()) {
					//traverse elements in reverse order, so we can align grace elements correctly
					//grace elements are not stretched, but the distance to their following full element
					//stays the same
					float lastElementOriginalOffsetIs = getLast(column.beatOffsets).offsetMm / voice.interlineSpace;
					for (int i : rangeReverse(voice.elements)) {
						ElementSpacing element = voice.elements.get(i);
						if (element.isGrace()) {
							//grace element: keep distance to following element
							float oldDistance = lastElementOriginalOffsetIs - element.offsetIs;
							lastElementOriginalOffsetIs = element.offsetIs;
							element.offsetIs = voice.elements.get(i + 1).offsetIs - oldDistance;
						}
						else {
							//normal element: stretch the offset
							lastElementOriginalOffsetIs = element.offsetIs;
							element.offsetIs *= stretch;
						}
					}
				}
			}
		}

		//full system width
		system.widthMm = usableWidthMm;
	}

}
