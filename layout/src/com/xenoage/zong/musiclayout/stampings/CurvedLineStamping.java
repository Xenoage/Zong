package com.xenoage.zong.musiclayout.stampings;

import javax.rmi.CORBA.Tie;

import com.xenoage.zong.core.music.curvedline.CurvedLine;
import com.xenoage.zong.core.music.format.SP;


/**
 * Class for a curved line stamping, that represents a slur
 * or a tie (both have the same shape).
 * 
 * A curved line has a horizontal start and end position in mm,
 * and a vertical start and end position as a line position.
 * 
 * Additionally, there are two control points to form
 * the slur/tie as a bezier curve, each for the
 * start and the end point. These are given as a horizontal
 * offset in mm and a vertical offset in line position relative
 * to the start and end point respectively.
 *
 * @author Andreas Wenger
 */
public final class CurvedLineStamping
  extends Stamping
{
  
	/** The start position (left). */
  public final SP p1;
  
  /** The end position (right). */
  public final SP p2;
  
  /** The offset of the first control point relative to the start point. */
  public final SP c1;
  
  /** The offset of the second control point relative to the end point. */
  public final SP c2;
  
  
  /**
   * Creates a new {@link CurvedLineStamping} belonging to the given staff.
   * @param parentStaff  the staff stamping this element belongs to
   * @param curvedLine   the musical element (a {@link Tie} or a {@link Slur})
   * @param p1           t
   * @param p2           t
   * @param c1           t
   * @param c2           t
   */
  public CurvedLineStamping(StaffStamping parentStaff, CurvedLine curvedLine, SP p1, SP p2, SP c1, SP c2)
  {
    super(parentStaff, Level.Music, curvedLine, null);
    this.p1 = p1;
    this.p2 = p2;
    this.c1 = c1;
    this.c2 = c2;
  }
  
  
  /**
   * Gets the type of this stamping.
   */
  @Override public StampingType getType()
  {
  	return StampingType.CurvedLineStamping;
  }
  
}
