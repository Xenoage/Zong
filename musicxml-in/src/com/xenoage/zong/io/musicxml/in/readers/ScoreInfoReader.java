package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.iterators.It.it;

import com.xenoage.utils.collections.CList;
import com.xenoage.zong.core.info.Creator;
import com.xenoage.zong.core.info.Rights;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.musicxml.types.MxlIdentification;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.MxlTypedText;
import com.xenoage.zong.musicxml.types.MxlWork;
import com.xenoage.zong.musicxml.types.groups.MxlScoreHeader;

/**
 * This class reads general information about a score.
 *
 * @author Andreas Wenger
 */
public final class ScoreInfoReader {

	/**
	 * Reads general information about a score
	 * from the given {@link MxlScorePartwise}.
	 */
	public static ScoreInfo read(MxlScorePartwise mxlScore) {
		MxlScoreHeader mxlHeader = mxlScore.getScoreHeader();
		MxlWork mxlWork = notNull(mxlHeader.getWork(), MxlWork.empty);

		MxlIdentification mxlIdentification = mxlHeader.getIdentification();
		CList<Creator> creators = clist();
		CList<Rights> rights = clist();
		if (mxlIdentification != null) {
			for (MxlTypedText creator : it(mxlIdentification.getCreators()))
				creators.add(new Creator(creator.getValue(), creator.getType()));
			for (MxlTypedText right : it(mxlIdentification.getRights()))
				rights.add(new Rights(right.getValue(), right.getType()));
		}
		creators.close();
		rights.close();

		return new ScoreInfo(mxlWork.getWorkTitle(), mxlWork.getWorkNumber(),
			mxlHeader.getMovementTitle(), mxlHeader.getMovementNumber(), creators, rights);
	}

}
