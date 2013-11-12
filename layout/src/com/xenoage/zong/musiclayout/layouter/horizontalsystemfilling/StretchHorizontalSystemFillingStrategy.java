package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import static com.xenoage.utils.base.iterators.ReverseIterator.reverseIt;
import static com.xenoage.utils.pdlib.IVector.ivec;

import com.xenoage.utils.pdlib.IVector;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureElementsSpacings;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;


/**
 * This horizontal system filling strategy
 * stretches all measures within the given system,
 * so that they use the whole useable width
 * of the system.
 * 
 * @author Andreas Wenger
 */
public class StretchHorizontalSystemFillingStrategy
	implements HorizontalSystemFillingStrategy
{

	private static StretchHorizontalSystemFillingStrategy instance = null;


	public static StretchHorizontalSystemFillingStrategy getInstance()
	{
		if (instance == null)
			instance = new StretchHorizontalSystemFillingStrategy();
		return instance;
	}


	private StretchHorizontalSystemFillingStrategy()
	{
	}


	/**
	 * Stretches the measures of the given system, so that
	 * it uses the whole usable width of the system.
	 */
	@Override public SystemArrangement computeSystemArrangement(SystemArrangement systemArrangement,
		float usableWidth)
	{
		//compute width of all voice spacings
		//(leading spacings are not stretched)
		float voicesWidth = 0;
		float leadingsWidth = 0;
		for (ColumnSpacing mcs : systemArrangement.getColumnSpacings()) {
			voicesWidth += mcs.getVoicesWidth();
			leadingsWidth += mcs.getLeadingWidth();
		}

		//compute the stretching factor for the voice spacings
		if (voicesWidth == 0)
			return systemArrangement;
		float stretch = (usableWidth - leadingsWidth) / voicesWidth;

		//stretch the voice spacings
		//measure columns
		IVector<ColumnSpacing> newMCSpacings = ivec();
		for (ColumnSpacing column : systemArrangement.getColumnSpacings()) {
			//beat offsets
			IVector<BeatOffset> newBeatOffsets = ivec();
			for (BeatOffset oldBeatOffset : column.getBeatOffsets()) {
				//stretch the offset
				newBeatOffsets.add(oldBeatOffset.withOffsetMm(oldBeatOffset.getOffsetMm() * stretch));
			}
			IVector<BeatOffset> newBarlineOffsets = ivec();
			for (BeatOffset oldBarlineOffset : column.getBarlineOffsets()) {
				//stretch the offset
				newBarlineOffsets.add(oldBarlineOffset.withOffsetMm(oldBarlineOffset
					.getOffsetMm() * stretch));
			}
			//measures
			IVector<MeasureSpacing> newMeasureSpacings = ivec();
			for (MeasureSpacing oldMS : column.getMeasureSpacings()) {
				//measure elements
				IVector<SpacingElement> newMESElements = ivec();
				for (SpacingElement oldMES : oldMS.measureElementsSpacings.elements) {
					//stretch the offset
					newMESElements.add(oldMES.withOffset(oldMES.offset * stretch));
				}
				MeasureElementsSpacings newMES = new MeasureElementsSpacings(newMESElements.close());
				//voices
				IVector<VoiceSpacing> newVSs = ivec();
				for (VoiceSpacing oldVS : oldMS.voiceSpacings) {
					//spacing elements
					//traverse in reverse order, so we can align grace elements correctly
					//grace elements are not stretched, but the distance to their following full element
					//stays the same
					IVector<SpacingElement> newSEs = ivec();
					float lastOldOffset = Float.NaN;
					float lastNewOffset = Float.NaN;
					for (SpacingElement oldSE : reverseIt(oldVS.getSpacingElements())) {
						if (oldSE.grace && !Float.isNaN(lastOldOffset)) {
							//grace element: keep distance to following element
							float oldDistance = lastOldOffset - oldSE.offset;
							lastNewOffset = lastNewOffset - oldDistance;
						} else {
							//normal element: stretch the offset
							lastNewOffset = oldSE.offset * stretch;
						}
						lastOldOffset = oldSE.offset;
						newSEs.add(0, oldSE.withOffset(lastNewOffset));
					}
					newVSs.add(new VoiceSpacing(oldVS.getVoice(), oldVS.getInterlineSpace(), newSEs.close()));
				}
				newMeasureSpacings.add(new MeasureSpacing(oldMS.mp, newVSs.close(), newMES,
					oldMS.leadingSpacing));
			}

			newMCSpacings.add(new ColumnSpacing(column.getScore(), newMeasureSpacings.close(),
				newBeatOffsets.close(), newBarlineOffsets.close()));

		}

		//create and return the new system
		return systemArrangement.withSpacings(newMCSpacings.close(), usableWidth);
	}

}
