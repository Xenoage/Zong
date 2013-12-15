package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.base.NullUtils.notNull;

import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.info.Creator;
import com.xenoage.zong.core.info.Rights;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.musicxml.types.MxlIdentification;
import com.xenoage.zong.musicxml.types.MxlScoreHeader;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.musicxml.types.MxlTypedText;
import com.xenoage.zong.musicxml.types.MxlWork;


/**
 * This class reads information about a score
 * from a MusicXML 2.0 document.
 *
 * @author Andreas Wenger
 */
public final class ScoreInfoReader
{
  
  
  /**
   * Reads general information about a score
   * from the given {@link MxlScorePartwise}.
   */
  public static ScoreInfo read(MxlScorePartwise mxlScore)
  {
  	MxlScoreHeader mxlHeader = mxlScore.scoreHeader;
    MxlWork mxlWork = notNull(mxlHeader.getWork(), MxlWork.empty);
    
    MxlIdentification mxlIdentification = mxlHeader.getIdentification();
    PVector<Creator> creators = PVector.pvec();
    PVector<Rights> rights = PVector.pvec();
    if (mxlIdentification != null)
    {
    	for (MxlTypedText creator : mxlIdentification.getCreators())
    		creators = creators.plus(new Creator(creator.getValue(), creator.getType()));
    	for (MxlTypedText right : mxlIdentification.getRights())
    		rights = rights.plus(new Rights(right.getValue(), right.getType()));
    }
    
    return new ScoreInfo(mxlWork.getWorkTitle(), mxlWork.getWorkNumber(),
    	mxlHeader.getMovementTitle(), mxlHeader.getMovementNumber(),
    	creators, rights);
  }

}
