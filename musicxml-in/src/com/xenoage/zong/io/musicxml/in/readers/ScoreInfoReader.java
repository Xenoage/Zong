package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.iterators.It.it;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.core.info.Creator;
import com.xenoage.zong.core.info.Rights;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.musicxml.types.MxlIdentification;
import com.xenoage.zong.musicxml.types.MxlTypedText;
import com.xenoage.zong.musicxml.types.MxlWork;
import com.xenoage.zong.musicxml.types.groups.MxlScoreHeader;

/**
 * Reads a {@link ScoreInfo} from a {@link MxlScoreHeader}.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public final class ScoreInfoReader {

	private final MxlScoreHeader mxlHeader;
	
	private ScoreInfo scoreInfo;
	
	
	public ScoreInfo read() {
		scoreInfo = new ScoreInfo();
		readWork();
		readMovement();
		readCreators();
		readRights();
		return scoreInfo;
	}

	private void readWork() {
		MxlWork mxlWork = notNull(mxlHeader.getWork(), MxlWork.empty);
		scoreInfo.setWorkTitle(mxlWork.getWorkTitle());
		scoreInfo.setWorkNumber(mxlWork.getWorkNumber());
	}
	
	private void readMovement() {
		scoreInfo.setMovementTitle(mxlHeader.getMovementTitle());
		scoreInfo.setMovementNumber(mxlHeader.getMovementNumber());
	}

	private void readCreators() {
		MxlIdentification mxlIdentification = mxlHeader.getIdentification();
		if (mxlIdentification != null) {
			for (MxlTypedText mxlCreator : it(mxlIdentification.getCreators())) {
				Creator creator = new Creator(mxlCreator.getValue(), mxlCreator.getType());
				scoreInfo.getCreators().add(creator);
			}
		}
	}
	
	private void readRights() {
		MxlIdentification mxlIdentification = mxlHeader.getIdentification();
		if (mxlIdentification != null) {
			for (MxlTypedText mxlRights : it(mxlIdentification.getRights())) {
				Rights rights = new Rights(mxlRights.getValue(), mxlRights.getType());
				scoreInfo.getRights().add(rights);
			}
		}
	}

}
