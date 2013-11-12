package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.core.music.clef.Clef;


/**
 * Class for a clef stamping.
 *
 * @author Andreas Wenger
 */
public final class ClefStamping
  extends StaffSymbolStamping
{
  
  
  public ClefStamping(Clef clef,
    StaffStamping parentStaff, float xPosition, float scaling, SymbolPool symbolPool)
  {
    super(parentStaff, clef,
    	symbolPool.getSymbol(CommonSymbol.getClef(clef.getType())), null, 
      sp(xPosition, clef.getType().getLine()),
      scaling, false);
  }


}
