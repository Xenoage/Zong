package com.xenoage.zong.core.info;

import static com.xenoage.utils.base.NullUtils.notNull;
import lombok.Data;
import lombok.experimental.Wither;

import com.xenoage.utils.base.annotations.Const;
import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NonNull;
import com.xenoage.utils.pdlib.IVector;
import com.xenoage.utils.pdlib.Vector;


/**
 * Information about a single score,
 * like title or composer.
 *
 * @author Andreas Wenger
 */
@Const @Data @Wither public final class ScoreInfo {

	/** Title of the work. */
	@MaybeNull private final String workTitle;
	/** Number of the work. */
	@MaybeNull private final String workNumber;
	/** Title of the movement. */
	@MaybeNull private final String movementTitle;
	/** Number of the movement. */
	@MaybeNull private final String movementNumber;
	/** List of creators (composers, arrangers, ...). */
	@NonNull @MaybeEmpty private final Vector<Creator> creators;
	/** List of rights. */
	@NonNull @MaybeEmpty private final Vector<Rights> rights;

	/** Empty instance. */
	public static final ScoreInfo empty = new ScoreInfo(null, null, null, null, new IVector<Creator>().close(),
		new IVector<Rights>().close());


	/**
	 * Gets the first mentioned composer of this score, or null
	 * if unavailable.
	 */
	public String getComposer() {
		for (Creator creator : creators) {
			if (creator.getType() != null && creator.getType().toLowerCase().equals("composer"))
				return creator.getName();
		}
		return null;
	}


	/**
	 * Gets the title of the score. This is the movement-title, or if unknown,
	 * the work-title. If both are unknown, null is returned.
	 */
	public String getTitle() {
		return notNull(movementTitle, workTitle);
	}

}
