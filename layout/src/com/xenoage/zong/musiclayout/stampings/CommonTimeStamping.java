package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.zong.core.music.time.CommonTime;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;


/**
 * Class for a C time signature stamping.
 *
 * @author Andreas Wenger
 */
public class CommonTimeStamping
  extends StaffSymbolStamping
{
  
  
  public CommonTimeStamping(CommonTime commonTime,
    float positionX, StaffStamping parentStaff, SymbolPool symbolPool)
  {
    super(parentStaff, commonTime, symbolPool.getSymbol(CommonSymbol.TimeCommon), null,
    	sp(positionX, parentStaff.linesCount - 1), 1f, false);
  }

}
