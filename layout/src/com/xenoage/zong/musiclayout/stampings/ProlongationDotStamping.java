package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.SP;


/**
 * Class for a prolongation dot stamping.
 * 
 * Prolongation dots belong to a staff. They have
 * a horizontal position and a line position
 * around which they are centered.
 * They are 0.3 spaces wide.
 *
 * @author Andreas Wenger
 */
public class ProlongationDotStamping
  extends StaffSymbolStamping
{
  
  
  public ProlongationDotStamping(StaffStamping parentStaff, Chord chord,
    SP position, SymbolPool symbolPool)
  {
    super(parentStaff, chord,
    	symbolPool.getSymbol(CommonSymbol.NoteDot),
    	null, position, 1, false);
  }
  
}
