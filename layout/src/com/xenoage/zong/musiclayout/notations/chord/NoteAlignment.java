package com.xenoage.zong.musiclayout.notations.chord;


/**
 * The alignment of a single note within
 * a chord: its vertical position and its
 * horizontal offset and suspension.
 *
 * @author Andreas Wenger
 */
public final class NoteAlignment
{
  
  private final int linePosition;
  private final float offset;
  private final NoteSuspension suspension;
  
  
  
  /**
   * Creates a new NoteAlignment.
   * @param linePosition  vertical position: 0 = bottom line,
   *                      3 = between 2nd and 3rd line, ...
   * @param offset        horizontal offset in spaces
   * @param suspension    none, left or right side of the stem
   */
  public NoteAlignment(int linePosition, float offset, NoteSuspension suspension)
  {
    this.linePosition = linePosition;
    this.offset = offset;
    this.suspension = suspension;
  }
  
  
  /**
   * Creates a new NoteAlignment for an unsuspended note.
   * @param linePosition  vertical position: 0 = bottom line,
   *                      3 = between 2nd and 3rd line, ...
   * @param offset        horizontal offset in spaces
   */
  public NoteAlignment(int linePosition, float offset)
  {
    this(linePosition, offset, NoteSuspension.None);
  }
  
  
  /**
   * Gets the vertical position of the note in half-space steps,
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
   * Gets the horizontal offset of the note in spaces.
   */
  public float getOffset()
  {
    return offset;
  }
  
  
  /**
   * Gets the suspension of the note, that is
   * left or right side of the stem, or none if the
   * notes are aligned on the default side of the stem.
   */
  public NoteSuspension getSuspension()
  {
    return suspension;
  }
  

}
