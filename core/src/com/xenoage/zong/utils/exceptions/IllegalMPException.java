package com.xenoage.zong.utils.exceptions;

import com.xenoage.zong.core.position.MP;


/**
 * This exception is thrown when an illegal musical position is used.
 * 
 * It is a {@link RuntimeException} which needs not to be
 * caught (but it should be wherever it is useful).
 *
 * @author Andreas Wenger
 */
public class IllegalMPException
  extends RuntimeException
{
  
  private final MP pos;
  
  
  public IllegalMPException(MP pos)
  {
    this.pos = pos;
  }
  
  
  public IllegalMPException(MP pos, String message)
  {
  	super(message);
    this.pos = pos;
  }
  
  
  @Override public String getMessage()
  {
  	String superMessage = super.getMessage();
  	if (superMessage == null)
  		return "Invalid MP: " + pos;
  	else
  		return superMessage + " - Invalid MP: " + pos;
  }

}
