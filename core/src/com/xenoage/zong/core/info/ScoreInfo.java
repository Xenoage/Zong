package com.xenoage.zong.core.info;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;

/**
 * Information about a single score,
 * like title or composer.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @RequiredArgsConstructor @Data
public class ScoreInfo {

	/** Title of the work. */
	@MaybeNull private String workTitle;
	/** Number of the work. */
	@MaybeNull private String workNumber;
	/** Title of the movement. */
	@MaybeNull private String movementTitle;
	/** Number of the movement. */
	@MaybeNull private String movementNumber;
	/** List of creators (composers, arrangers, ...). */
	@NonNull @MaybeEmpty private List<Creator> creators = alist();
	/** List of rights. */
	@NonNull @MaybeEmpty private List<Rights> rights = alist();


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
