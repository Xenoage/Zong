package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.math.geom.Point2f.p;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.musiclayout.notation.Notation;

/**
 * Class for a text stamping belonging to a staff, e.g. for lyric
 * and directions.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class StaffTextStamping
	extends TextStamping {

	/** The text element. */
	@NonNull public final FormattedText text;
	/** The position, relative to the left border of the staff. */
	@NonNull public final SP position;
	/** The parent staff. */
	@NonNull public final StaffStamping parentStaff;
	/** The {@link Notation} (or {@link MusicElement}) this stamping belongs to. */ //TODO: clean
	@MaybeNull public final Object element;
	

	@Override public Point2f getPositionMm() {
		return p(position.xMm, parentStaff.computeYMm(position.lp));
	}

	@Override public Shape getBoundingShape() {
		float x = position.xMm;
		float y = parentStaff.computeYMm(position.lp);
		return text.getBoundingRect().move(x, y);
	}

}
