package com.xenoage.zong.musiclayout.notations.chord;

import com.xenoage.zong.core.music.chord.Accidental;


/**
 * The alignment of a single accidental within
 * a chord: its vertical position, its
 * horizontal offset and its type.
 *
 * @author Andreas Wenger
 */
public final class AccidentalAlignment
{
  
  private final int linePosition;
  private final float offset;
  private final Accidental.Type type;
  
  
  /**
   * Creates a new AccidentalAlignment.
   * @param linePosition  vertical position: 0 = bottom line,
   *                      3 = between 2nd and 3rd line, ...
   * @param offset        horizontal offset in interline spaces
   * @param type          flat, natural, sharp, ...
   */
  public AccidentalAlignment(int linePosition, float offset,
    Accidental.Type type)
  {
    this.linePosition = linePosition;
    this.offset = offset;
    this.type = type;
  }
  
  
  /**
   * Gets the vertical position of the accidental in half-space steps,
   * beginning at the bottom line. Some examples:
   * <ul>
   *   <li>0: note is on the bottom line</li>
   *   <li>-2: note is on the first lower leger line</li>
   *   <li>5: note is between the 3rd and 4th line from below</li>
   * </ul>
   */
  public int getLinePosition()
  {
    return linePosition;
  }
  
  
  /**
   * Gets the horizontal offset of the accidental in interline spaces.
   */
  public float getOffset()
  {
    return offset;
  }
  
  
  /**
   * Gets the type of the accidental, e.g. flat or sharp.
   */
  public Accidental.Type getType()
  {
    return type;
  }
  

}
