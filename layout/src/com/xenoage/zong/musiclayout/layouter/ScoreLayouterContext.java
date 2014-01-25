package com.xenoage.zong.musiclayout.layouter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This class can be used as a parameter in all {@link ScoreLayouterStrategy}
 * classes. It contains basic information about the layouting process,
 * like the {@link Score} to be layouted and the used {@link SymbolPool}.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class ScoreLayouterContext {

	/** The score which is layouted. */
	@NonNull @Getter private final Score score;
	/** The pool of musical symbols. */
	@NonNull @Getter private final SymbolPool symbolPool;
	/** General layout preferences. */
	@NonNull @Getter private final LayoutSettings layoutSettings;
	/** True to layout the whole score, false to layout
	 * only the frames of the score frame chain. */
	@Getter private final boolean isCompleteLayout;

	/** Information about the score frames in which to layout. */
	@Getter private final IList<ScoreLayoutArea> areas;
	/** If the given areas are not enough, additional areas with this settings are used. */
	@Getter private final ScoreLayoutArea additionalArea;

	//cache
	private float maxInterlineSpace = Float.NaN;


	/**
	 * Gets the biggest interline space of the score.
	 */
	public float getMaxInterlineSpace() {
		if (maxInterlineSpace == Float.NaN) {
			for (int staff = 0; staff < score.getStavesCount(); staff++) {
				maxInterlineSpace = Math.max(maxInterlineSpace, score.getInterlineSpace(staff));
			}
		}
		return maxInterlineSpace;
	}

	/**
	 * Gets a {@link ScoreLayoutArea} for the area with the given index.
	 * If it is an area after the last explicitly known are, the additional area
	 * is returned.
	 */
	public ScoreLayoutArea getArea(int index) {
		if (index < areas.size())
			return areas.get(index);
		else
			return additionalArea;
	}

}
